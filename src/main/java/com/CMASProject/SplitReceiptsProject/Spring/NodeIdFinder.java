package com.CMASProject.SplitReceiptsProject.Spring;

import com.CMASProject.SplitReceiptsProject.Enteties.Person;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;

@Service
public class NodeIdFinder {
    //This is the ID of the folder where all the oder folders are contained
    private static final String userHomeID = "64ba469b-980b-4523-8930-426e3645c4f0";
    private static final String URL = "https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/alfresco/versions/1/nodes";

    private final RestTemplate restTemplate;
    private final String ticket;

    public NodeIdFinder(RestTemplate restTemplate, String ticket) {
        this.restTemplate = restTemplate;
        this.ticket = ticket;
    }

    public void Algo(List<Person> list){
        JsonNode jsonNode = restTemplate.getForObject(format("%s/%s/children?alf_ticket=%s",URL, userHomeID, ticket), JsonNode.class);
        //System.out.println(jsonNode.toPrettyString());
        JsonNode entries = jsonNode.get("list").get("entries");
        JsonNode pagination = jsonNode.get("list").get("pagination");

        HashMap<String, String> map = new HashMap<String, String>();
        //Gets the name and the corresponded id of the folder
        for(int i = 0; i < pagination.get("count").asInt(0); i++){
            JsonNode entry = entries.get(i).get("entry");
            map.put(entry.get("name").textValue(), entry.get("id").textValue());
        }

        //Attributes each folder id to the correspondent person.
        for(Person person : list){
            String name = person.getName().replace(" ",".").toLowerCase();
            name = specialCharacterRemoval(name);
            if(map.containsKey(name)){
                person.setNodeID(map.get(name));
            }
        }
    }

    public static String specialCharacterRemoval(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}
