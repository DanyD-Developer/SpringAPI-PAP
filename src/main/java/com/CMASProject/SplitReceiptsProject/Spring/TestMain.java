package com.CMASProject.SplitReceiptsProject.Spring;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.client.RestTemplate;

public class TestMain {
    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
//        JsonNode response = restTemplate.postForObject("https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/authentication/versions/1/tickets","{\"userId\":\"config\",\"password\":\"nowo123\"}", JsonNode.class);
//        String a = String.valueOf(response.get("entry").get("id"));
//        System.out.println(a);
        TicketManager ticketManager = new TicketManager(new RestTemplate());
        ticketManager.requestTicket();
        System.out.println(ticketManager.getTicket());
        ticketManager.closeTicket();
    }
}
