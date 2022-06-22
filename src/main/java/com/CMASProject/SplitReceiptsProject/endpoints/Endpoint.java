package com.CMASProject.SplitReceiptsProject.endpoints;

import com.CMASProject.SplitReceiptsProject.AppProperties;
import com.CMASProject.SplitReceiptsProject.controllers.UploadFile;
import com.CMASProject.SplitReceiptsProject.enteties.*;
import com.CMASProject.SplitReceiptsProject.services.TicketManager;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final TicketManager ticketManager;
    private final UploadFile uploadFile;
    private final Protector protector;
    private final Splitter splitter;
    private final ObjectMapper objectMapper;


    @PostMapping
    public void getRequest(@RequestParam("filedata") MultipartFile multipartFilePDF, @RequestParam("file") MultipartFile multipartFileText) throws IOException {

        Map<String, String> passwordsMap = objectMapper.readValue(new String(multipartFileText.getBytes()), Map.class);

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

            protector.protectPdfs(personsList);

            uploadFile.fileUpload(personsList);

        } catch (Exception e) {
            log.error("Failed Loading file {}", multipartFilePDF.getOriginalFilename(), e);
            throw new RuntimeException("Failed Loading file");
        }

        DeleteFiles(new File(appProperties.getTempFolder()));

    }

    private File createTemporaryFiles(MultipartFile multipartFilePDF) {
        File temporaryFilePDF = new File(appProperties.getTempFolder() + "\\" + multipartFilePDF.getOriginalFilename());

        try (OutputStream os = Files.newOutputStream(temporaryFilePDF.toPath())) {
            //Write PDF document
            os.write(multipartFilePDF.getBytes());
            System.out.println(temporaryFilePDF.getPath());
            multipartFilePDF.transferTo(temporaryFilePDF);
            return temporaryFilePDF;
        } catch (IOException e) {
            log.error("It was not possible write the multipart file in a file");
            throw new RuntimeException("It was not possible write the multipart file in a file");
        }
    }
    public static void DeleteFiles(File folder) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            String path = folder +"\\"+ fileEntry.getName();
            File file = new File(path);
            if(file.delete()){
                log.info("Delete "+fileEntry.getName());
            }
            else{
                log.error("It Was not possible Delete the files");
                throw new RuntimeException("It Was not possible Delete the files");
            }
        }
    }
}
