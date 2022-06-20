package com.CMASProject.SplitReceiptsProject.services;

import com.CMASProject.SplitReceiptsProject.enteties.Config;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;

@Service
public class TicketManager {
    private static String URL;        // = "https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/authentication/versions/1/tickets";
    private static String credentials;// = "{\"userId\":\"config\",\"password\":\"nowo123\"}";

    private String ticket;

    private RestTemplate restTemplate;

    public TicketManager(RestTemplate restTemplate, Config config){
        this.restTemplate = restTemplate;
        URL = config.getAlfrescoURL() + "/alfresco/api/-default-/public/authentication/versions/1/tickets";
        credentials = "{\"userId\":\""+config.getAlfrescoUsername()+"\",\"password\":\""+config.getAlfrescoPassword()+"\"}";
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
            System.out.println("Make sure you typed the URL correctly or if you have internet connection");
            System.out.println("Exiting Program.");
            System.exit(24);
        }
        catch (IllegalArgumentException e){
            System.out.println("It was not possible to send files to Alfresco.");
            System.out.println("Error: "+ e.getMessage());
            System.out.println("Make sure you typed the URL correctly");
            System.out.println("Exiting Program.");
            System.exit(25);
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
