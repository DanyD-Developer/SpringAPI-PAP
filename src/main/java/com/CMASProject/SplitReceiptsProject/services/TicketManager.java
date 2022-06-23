package com.CMASProject.SplitReceiptsProject.services;

import com.CMASProject.SplitReceiptsProject.AppProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;

@Slf4j
@Service
public class TicketManager {
    private final AppProperties appProperties;
    private final String URL;        // = "https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/authentication/versions/1/tickets";
    private final String credentials; // = "{\"userId\":\"config\",\"password\":\"nowo123\"}";

    private String ticket;

    private final RestTemplate restTemplate;


    public TicketManager(AppProperties appProperties, RestTemplate restTemplate) {
        this.appProperties = appProperties;
        this.restTemplate = restTemplate;

        URL = this.appProperties.getAlfrescoProperties().getUrl() + "/alfresco/api/-default-/public/authentication/versions/1/tickets";
        credentials = "{\"userId\":\"" + this.appProperties.getAlfrescoProperties().getUsername() + "\",\"password\":\"" + this.appProperties.getAlfrescoProperties().getPassword() + "\"}";
    }

    public void requestTicket() {
        try {
            JsonNode response = this.restTemplate.postForObject(URL, credentials, JsonNode.class);
            if (response == null) {
                log.error("Something went Wrong!");
                return;
            }
            this.ticket = response.get("entry").get("id").asText();
        } catch (ResourceAccessException e) {
            log.info("It was not possible to send files to Alfresco.");
            log.error("Connection Time out"+e);
            log.info("Make sure you typed the URL correctly or if you have internet connection");
            throw new RuntimeException("Connection Time out");
        } catch (IllegalArgumentException e) {
            log.info("It was not possible to send files to Alfresco.");
            log.error("Error: " + e.getMessage());
        }

    }

    public void closeTicket() {
        restTemplate.delete(format("%s/-me-?alf_ticket=%s", URL, ticket));
    }

    public String getTicket() {
        return ticket;
    }
}
