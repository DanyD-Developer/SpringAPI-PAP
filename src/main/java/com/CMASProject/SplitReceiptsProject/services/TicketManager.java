package com.CMASProject.SplitReceiptsProject.services;

import com.CMASProject.SplitReceiptsProject.AppProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;

@Service
public class TicketManager {
    private final AppProperties appProperties;
    private String URL;        // = "https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/authentication/versions/1/tickets";
    private String credentials; // = "{\"userId\":\"config\",\"password\":\"nowo123\"}";

    private String ticket;

    private final RestTemplate restTemplate;


    public TicketManager(AppProperties appProperties, RestTemplate restTemplate) {
        this.appProperties = appProperties;
        this.restTemplate = restTemplate;

        AppProperties.AlfrescoProperties pathDefault = appProperties.getAlfrescoProperties();

        URL = pathDefault.getUrl() + "/alfresco/api/-default-/public/authentication/versions/1/tickets";
        credentials = "{\"userId\":\"" + pathDefault.getUsername() + "\",\"password\":\"" + pathDefault.getPassword() + "\"}";
        requestTicket();
    }

    private void requestTicket() {
        try {
            JsonNode response = this.restTemplate.postForObject(URL, credentials, JsonNode.class);
            if (response == null) {
                System.out.println("Algo deu errado!");
                return;
            }
            this.ticket = response.get("entry").get("id").asText();
        } catch (ResourceAccessException e) {
            System.out.println("It was not possible to send files to Alfresco.");
            System.out.println("Connection Time out");
            System.out.println("Make sure you typed the URL correctly or if you have internet connection");
            System.out.println("Exiting Program.");
            System.exit(24);
        } catch (IllegalArgumentException e) {
            System.out.println("It was not possible to send files to Alfresco.");
            System.out.println("Error: " + e.getMessage());
            System.out.println("Make sure you typed the URL correctly");
            System.out.println("Exiting Program.");
            System.exit(25);
        }

    }

    public void closeTicket() {
        //System.out.println(url);
        restTemplate.delete(format("%s/-me-?alf_ticket=%s", URL, ticket));
        //System.out.println(response.toString());
    }

    public String getTicket() {
        return ticket;
    }
}
