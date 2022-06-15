package com.CMASProject.SplitReceiptsProject.Controllers;

import com.CMASProject.SplitReceiptsProject.Configuration.RestConfig;
import com.CMASProject.SplitReceiptsProject.Enteties.Config;
import com.CMASProject.SplitReceiptsProject.Enteties.FileHolder;
import com.CMASProject.SplitReceiptsProject.Enteties.Person;
import com.CMASProject.SplitReceiptsProject.Services.TicketManager;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.util.List;

@Controller
public class UploadFile {

    public static void fileUpload(List<Person> persons, Config config, FileHolder fileHolder){
        RestConfig restConfig = new RestConfig();

        TicketManager ticketManager = new TicketManager(restConfig.restTemplate());
        NodeIdFinder nodeIdFinder = new NodeIdFinder(restConfig.restTemplate(),ticketManager.getTicket());

        nodeIdFinder.setNodeIDs(persons);
        fileHolder.setFilePerPerson(new File(config.getDestinationFolder()), persons);

        for(Person person: persons){
            String URL = "https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/alfresco/versions/1/nodes/"+person.getNodeID()+"/children?alf_ticket="+ticketManager.getTicket();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
            body.add("filedata", person.getFile());
            body.add("description", "Wage Receipt form june");

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body,headers);
            ResponseEntity<String> request = restConfig.restTemplate().postForEntity(URL,requestEntity,String.class);

            System.out.println("response status: " + request.getStatusCode());
            System.out.println("File Upload successful");
        }

        ticketManager.closeTicket();
    }
}
