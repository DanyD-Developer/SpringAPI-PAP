package com.CMASProject.SplitReceiptsProject.endpoints;

import com.CMASProject.SplitReceiptsProject.AppProperties;
import com.CMASProject.SplitReceiptsProject.controllers.UploadFile;
import com.CMASProject.SplitReceiptsProject.enteties.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/upload")
public class Endpoint {

    private final AppProperties appProperties;

    private final UploadFile uploadFile;
    private final Protector protector;
    private final Splitter splitter;
    private final ObjectMapper objectMapper;


    @PostMapping
    public ResponseEntity<String> getRequest(@RequestParam("filedata") MultipartFile multipartFilePDF, @RequestParam("file") MultipartFile multipartFileText) throws IOException {

        Map<String, String> passwordsMap;
        try{
            //Read the Json file of the passwords and save inside a Map
            passwordsMap = objectMapper.readValue(new String(multipartFileText.getBytes()), Map.class);
        }
        catch (JsonParseException e){
            log.error("Error trying to read JSON file verify if you put the correct file "+e.getMessage());
            throw new RuntimeException("Error trying to read JSON file");
        }catch (MismatchedInputException e){
            log.error("Error {}",e.getMessage() +" Make sure you didn't forget to put a file(s) on the body or if he file is not empty");
            throw new RuntimeException("Error trying to get the body file(s)");
        }

        //Create the Temporary files
        File file = createTemporaryFiles(multipartFilePDF);

        try (PDDocument wagesReceipts = PDDocument.load(file)) {
            //Create a list of each person (each person is created according to the NIF in the passwords file)
            List<Person> personsList = passwordsMap
                    .entrySet()
                    .stream()
                    .map(entry -> new Person(Integer.parseInt(entry.getKey()), entry.getValue()))
                    .collect(Collectors.toList());

            //Splits the pdfs and Checks if it was done any split
            splitter.split(wagesReceipts, personsList);

            //Encrypt the pdf file with the respective person's password
            protector.protectPdfs(personsList);

            //Upload the files to alfresco
            uploadFile.fileUpload(personsList);

        } catch (Exception e) {
            log.error("Failed Loading file {}", multipartFilePDF.getOriginalFilename(), e);
            throw new RuntimeException("Failed Loading file");
        }

        //Delete the Temporary Files
        DeleteTemporaryFiles(new File(appProperties.getTempFolder()));

        return ResponseEntity.ok().body("Upload Successfully");

    }

    private File createTemporaryFiles(MultipartFile multipartFilePDF) {
        File temporaryFilePDF = new File(appProperties.getTempFolder() + "\\" + multipartFilePDF.getOriginalFilename());

        try (OutputStream os = Files.newOutputStream(temporaryFilePDF.toPath())) {
            //Write PDF document
            os.write(multipartFilePDF.getBytes());
            multipartFilePDF.transferTo(temporaryFilePDF);
            return temporaryFilePDF;
        } catch (IOException e) {
            log.error("Could not write multipart file inside an empty file, make sure you insert the file in the body.");
            throw new RuntimeException("Could not write multipart file inside an empty file.");
        }
    }
    public static void DeleteTemporaryFiles(File folder) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            String path = folder +"\\"+ fileEntry.getName();
            File file = new File(path);
            if(file.delete()){
                log.info("Delete "+fileEntry.getName());
            }
            else{
                log.error("It Was not possible Delete the files.");
                throw new RuntimeException("It Was not possible Delete the files.");
            }
        }
    }
}
