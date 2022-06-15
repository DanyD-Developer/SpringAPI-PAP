package com.CMASProject.SplitReceiptsProject.Controllers;

import com.CMASProject.SplitReceiptsProject.Configuration.RestConfig;
import com.CMASProject.SplitReceiptsProject.Enteties.Config;
import com.CMASProject.SplitReceiptsProject.Enteties.FileHolder;
import com.CMASProject.SplitReceiptsProject.Enteties.Person;
import com.CMASProject.SplitReceiptsProject.Services.TicketManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.net.ConnectException;
import java.util.List;

@Controller
public class UploadFile {

    public static void fileUpload(List<Person> persons, Config config, FileHolder fileHolder){

        try{
            RestConfig restConfig = new RestConfig();

            TicketManager ticketManager = new TicketManager(restConfig.restTemplate());
            NodeIdFinder nodeIdFinder = new NodeIdFinder(restConfig.restTemplate(),ticketManager.getTicket());

            nodeIdFinder.setNodeIDs(persons);
            fileHolder.setFilePerPerson(new File(config.getDestinationFolder()), persons);

            for(Person person: persons) {
                String URL = "https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/alfresco/versions/1/nodes/" + person.getNodeID() + "/children?alf_ticket=" + ticketManager.getTicket();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("filedata", person.getFile());
                body.add("overwrite", true);

                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
                ResponseEntity<String> request = restConfig.restTemplate().postForEntity(URL, requestEntity, String.class);

                if (request.getStatusCode() == HttpStatus.CREATED) {
                    System.out.println("File Upload successful");
                } else {
                    System.out.println("Error: " + request.getBody() + "" + request.getStatusCode().getReasonPhrase());
                }
            }
            ticketManager.closeTicket();
        }
        catch (HttpClientErrorException e4){
            System.out.println("It was not possible to send files to Alfresco.");
            System.out.println("Error: "+e4.getStatusCode() +" - "+ makeErrorMessage(e4.getResponseBodyAsString()));
            System.out.println("Exiting Program.");
            System.exit(22);
        }
        catch (ResourceAccessException e){
            System.out.println("It was not possible to send files to Alfresco.");
            System.out.println("Connection time out.");
            System.out.println("Exiting Program.");
            System.exit(23);
        }
    }

    private static String makeErrorMessage(String JSONmensage) {
        try {
            JsonNode root = new ObjectMapper().readTree(JSONmensage);
            return root.get("error").get("errorKey").asText();
        } catch (JsonProcessingException e) {
            return JSONmensage;
        }
    }
}
