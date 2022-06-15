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
    private static final String URL = "https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/authentication/versions/1/tickets";
    private static final String credentials = "{\"userId\":\"config\",\"password\":\"nowo123\"}";

    private String ticket;
    private final RestTemplate restTemplate;

    public TicketManager(RestTemplate restTemplate) throws ResourceAccessException {
        this.restTemplate = restTemplate;
        requestTicket();
    }

    private void requestTicket(){
        JsonNode response = restTemplate.postForObject(URL, credentials, JsonNode.class);
        if (response == null) {
            System.out.println("Algo deu errado!");
            return;
        }
        this.ticket = response.get("entry").get("id").asText();
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
