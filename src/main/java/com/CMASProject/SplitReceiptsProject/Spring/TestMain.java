package com.CMASProject.SplitReceiptsProject.Spring;

import com.CMASProject.SplitReceiptsProject.Enteties.Person;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class TestMain {
    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
//        JsonNode response = restTemplate.postForObject("https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/authentication/versions/1/tickets","{\"userId\":\"config\",\"password\":\"nowo123\"}", JsonNode.class);
//        String a = String.valueOf(response.get("entry").get("id"));
//        System.out.println(a);

        TicketManager ticketManager = new TicketManager(new RestTemplate());
        ticketManager.requestTicket();
        NodeIdFinder nodeIdFinder = new NodeIdFinder(new RestTemplate(), ticketManager.getTicket());
        List<Person> list = new ArrayList<Person>();
        Person person1 = new Person(1,"a");
        person1.setName("Carlos Santana");
        list.add(person1);
        Person person2 = new Person(2,"b");
        person2.setName("João Pinto");
        list.add(person2);
        nodeIdFinder.Algo(list);

        for(Person person : list){
            System.out.println(person.getNodeID());
        }
    }
}
