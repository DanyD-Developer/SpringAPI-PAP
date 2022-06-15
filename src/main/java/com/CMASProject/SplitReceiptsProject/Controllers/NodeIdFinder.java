package com.CMASProject.SplitReceiptsProject.Controllers;

import com.CMASProject.SplitReceiptsProject.Enteties.Person;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;

@Controller
public class NodeIdFinder {
    //This is the ID of the folder where all the oder folders are contained
    //private static final String userHomeID = "64ba469b-980b-4523-8930-426e3645c4f0";

    private static final String URL = "https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/alfresco/versions/1/nodes";

    private final RestTemplate restTemplate;
    private final String ticket;

    public NodeIdFinder(RestTemplate restTemplate, String ticket) {
        this.restTemplate = restTemplate;
        this.ticket = ticket;
    }

    private String getUserHomeID(){
        HashMap<String, String> map = getSubFoldersIDs("-root-");

        return map.get("User Homes");
    }

    public void setNodeIDs(List<Person> list){
        String userHomeID = getUserHomeID();

        //TODO Lembrar de tirar as duas linhas de codigo abaixo
        String testID = getSubFoldersIDs(userHomeID).get("test");
        HashMap<String, String> map = getSubFoldersIDs(testID);
        
        //HashMap<String, String> map = getSubFoldersIDs(userHomeID);

        //Attributes each folder id to the correspondent person.
        for(Person person : list){
            String name = person.getName().replace(" ",".").toLowerCase();
            name = specialCharacterRemoval(name);
            if(map.containsKey(name)){
                person.setNodeID(map.get(name));
            }
        }
    }

    //returns a HashMap containing all the sub folders IDs of the given node ID
    private HashMap<String, String> getSubFoldersIDs(String nodeID){
        JsonNode jsonNode = restTemplate.getForObject(format("%s/%s/children?alf_ticket=%s",URL, nodeID, ticket), JsonNode.class);
        JsonNode entries = jsonNode.get("list").get("entries");
        JsonNode pagination = jsonNode.get("list").get("pagination");

        HashMap<String, String> map = new HashMap<>();

        //Puts in thr HashMap the name and the correspondent id of the folder
        for(int i = 0; i < pagination.get("count").asInt(0); i++){
            JsonNode entry = entries.get(i).get("entry");
            map.put(entry.get("name").textValue(), entry.get("id").textValue());
        }

        return  map;
    }

    public static String specialCharacterRemoval(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}
