package com.CMASProject.SplitReceiptsProject.Services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;

import static java.lang.String.format;

@Service
public class TicketManager {
    private static String URL;        // = "https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/authentication/versions/1/tickets";
    private static String credentials = "{\"userId\":\"config\",\"password\":\"nowo123\"}";

    private String ticket;

    private RestTemplate restTemplate;

    public TicketManager(RestTemplate restTemplate, String url){
        this.restTemplate = restTemplate;
        this.URL = url+ "/alfresco/api/-default-/public/authentication/versions/1/tickets";
        requestTicket();
    }

    public TicketManager() {
    }

    private void requestTicket(){
        try{
            JsonNode response = this.restTemplate.postForObject(URL, credentials, JsonNode.class);
            if (response == null) {
                System.out.println("Algo deu errado!");
                return;
            }
            this.ticket = response.get("entry").get("id").asText();
        }catch (ResourceAccessException e){
            System.out.println("It was not possible to send files to Alfresco.");
            System.out.println("Connection Time out");
            System.out.println("Exiting Program.");
            System.exit(24);
        }

    }

    public void closeTicket(){
        //System.out.println(url);
        restTemplate.delete(format("%s/-me-?alf_ticket=%s", URL, ticket));
        //System.out.println(response.toString());
    }

    public String getTicket() {
        return ticket;
    }
}
