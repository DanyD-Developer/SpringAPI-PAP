package com.CMASProject.SplitReceiptsProject.Spring;

import com.CMASProject.SplitReceiptsProject.Enteties.Person;
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
    RestTemplate restTemplate = new RestTemplate();

    public String fileUpload(List<Person> persons,String ticket){
        for(Person person: persons){
            String URL = "https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/alfresco/versions/1/nodes/"+person.getNodeID()+"/children?alf_ticket="+ticket;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
            body.add("filedata", person.getFile());
            body.add("description", "Wage Receipt form june");

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body,headers);
            System.out.println(requestEntity);

            ResponseEntity<String> request = restTemplate.postForEntity(URL,requestEntity,String.class);
            System.out.println("response status: " + request.getStatusCode());
            System.out.println("File Upload successful");
        }
        return "Problems";
    }
}
