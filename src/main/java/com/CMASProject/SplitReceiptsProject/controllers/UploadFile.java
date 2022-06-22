package com.CMASProject.SplitReceiptsProject.controllers;

import com.CMASProject.SplitReceiptsProject.AppProperties;
import com.CMASProject.SplitReceiptsProject.enteties.Person;
import com.CMASProject.SplitReceiptsProject.services.TicketManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class UploadFile {

    private final AppProperties appProperties;
    private final RestTemplate restTemplate;

    private final TicketManager ticketManager;

    public void fileUpload(List<Person> persons){

        try{
            ticketManager.requestTicket();
            NodeIdFinder nodeIdFinder = new NodeIdFinder(this.restTemplate,ticketManager.getTicket(), appProperties.getAlfrescoProperties().getUrl());
            nodeIdFinder.assignFoldersID(persons);

            setFilePerPerson(new File(appProperties.getTempFolder()), persons);

            for(Person person: persons) {
                if(person.getNodeID() == null){
                    continue;
                }
                String URL = appProperties.getAlfrescoProperties().getUrl()+"/alfresco/api/-default-/public/alfresco/versions/1/nodes/" + person.getNodeID() + "/children?alf_ticket=" + ticketManager.getTicket();

                //Make the headers be the read by form data
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);


                //Crate a map and save the file and the property overwrite for the post
                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("filedata", person.getFile());
                body.add("overwrite", true);

                //Make the request with the data and make a post with the URL given
                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
                ResponseEntity<String> request = this.restTemplate.postForEntity(URL, requestEntity, String.class);

                if (request.getStatusCode() == HttpStatus.CREATED) {
                    log.info(person.getName() + " - File Upload to alfresco successfully");
                }

            }
            ticketManager.closeTicket();
        }
        catch (HttpClientErrorException e4){
            log.info("It was not possible to send files to Alfresco.");
            log.error("Error " +e4.getStatusCode().value()+ " "+e4.getStatusText() +" - "+ makeErrorMessage(e4.getResponseBodyAsString()));
            throw new RuntimeException(makeErrorMessage(e4.getResponseBodyAsString()));
        }
        catch (ResourceAccessException e5){
            log.info("It was not possible to send files to Alfresco.");
            log.error("Error " +e5);
            throw new RuntimeException("Connection Time out");
        }
    }

    private static String makeErrorMessage(String JSONmensage) {
        try {
            JsonNode root = new ObjectMapper().readTree(JSONmensage);
            return root.get("error").get("briefSummary").asText();
        } catch (JsonProcessingException e) {
            return JSONmensage;
        }
    }
    public void setFilePerPerson(File folder, List<Person> persons) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            for(Person person : persons){
                if(person.getName() != null){
                    if(fileEntry.getName().contains(person.getName())) {
                        String path = folder +"\\"+ fileEntry.getName();
                        FileSystemResource file = new FileSystemResource(path);
                        person.setFile(file);
                    }
                }
            }
        }
    }
}
