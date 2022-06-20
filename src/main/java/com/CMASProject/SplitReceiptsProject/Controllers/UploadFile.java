package com.CMASProject.SplitReceiptsProject.Controllers;

import com.CMASProject.SplitReceiptsProject.Configuration.RestConfig;
import com.CMASProject.SplitReceiptsProject.Enteties.Config;
import com.CMASProject.SplitReceiptsProject.Enteties.FileHolder;
import com.CMASProject.SplitReceiptsProject.Enteties.Person;
import com.CMASProject.SplitReceiptsProject.Services.TicketManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.util.List;

@Controller
public class UploadFile {

    //TODO Fix the RestTemplate Injection
    private RestTemplate restTemplate = new RestTemplate();

    public void fileUpload(List<Person> persons, Config config, FileHolder fileHolder){

        try{
            TicketManager ticketManager = new TicketManager(this.restTemplate, config);

            NodeIdFinder nodeIdFinder = new NodeIdFinder(this.restTemplate,ticketManager.getTicket(), config.getAlfrescoURL());
            nodeIdFinder.assignFoldersID(persons);

            fileHolder.setFilePerPerson(new File(config.getDestinationFolder()), persons);

            for(Person person: persons) {
                if(person.getNodeID() == null){
                    continue;
                }
                String URL = "https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/alfresco/versions/1/nodes/" + person.getNodeID() + "/children?alf_ticket=" + ticketManager.getTicket();

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
                    System.out.println(person.getName() + " - File Upload successfully");
                } else {
                    System.out.println("Error: " + request.getBody() + "" + request.getStatusCode().getReasonPhrase());
                }
            }
            ticketManager.closeTicket();
        }
        catch (HttpClientErrorException e4){
            System.out.println("It was not possible to send files to Alfresco.");
            System.out.println("Error: "+e4.getStatusCode().value()+ " "+e4.getStatusText() +" - "+ makeErrorMessage(e4.getResponseBodyAsString()));
            System.out.println("Exiting Program.");
            System.exit(22);
        }
        catch (ResourceAccessException e5){
            System.out.println("It was not possible to send files to Alfresco.");
            System.out.println("Error: "+ e5.getMessage());
            System.out.println("Exiting Program.");
            System.exit(23);
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
}
