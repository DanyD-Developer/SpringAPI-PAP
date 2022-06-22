package com.CMASProject.SplitReceiptsProject.controllers;

import com.CMASProject.SplitReceiptsProject.enteties.Person;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Controller
public class NodeIdFinder {
    //This is the ID of the folder where all the oder folders are contained
    //private static final String userHomeID = "64ba469b-980b-4523-8930-426e3645c4f0";

    private static String URL;//= "https://alfresco-nowo.cmas-systems.com/alfresco/api/-default-/public/alfresco/versions/1/nodes";

    private RestTemplate restTemplate;

    private String ticket;

    public NodeIdFinder(RestTemplate restTemplate, String ticket, String url) {
        this.restTemplate = restTemplate;
        this.ticket = ticket;
        this.URL = url + "/alfresco/api/-default-/public/alfresco/versions/1/nodes";
    }

    public NodeIdFinder() {
    }

    private String getWagesReceiptsFolderID(){
        String id = getSubFoldersIDs("-root-").get("Sites");
        id = getSubFoldersIDs(id).get("people");
        id = getSubFoldersIDs(id).get("documentLibrary");
        id = getSubFoldersIDs(id).get("12.WAGE RECEIPTS");
        return id;
    }

    public void assignFoldersID(List<Person> list){

        HashMap<String, String> map = getSubFoldersIDs(getWagesReceiptsFolderID());

        //Attributes each WR folder id to the correspondent person.
        for(Person person : list){
            String name = person.getName();
            if(name == null){ continue; }
//            name = name.replace(" ",".").toLowerCase();
//            name = specialCharacterRemoval(name);
            if(map.containsKey(name)){
                String personWgFolderID = getSubFoldersIDs(map.get(name)).getOrDefault("WR",null);
                if(personWgFolderID != null){
                    person.setNodeID(personWgFolderID);
                }
                else{
                    log.error("Person '"+name+"' does not have a WR folder!");
                    throw new RuntimeException("Person '"+name+"' does not have a WR folder!");
                }
            }
            else{
                log.error("Could not find folder of '"+name+"' in alfresco! Make sure that the folder in alfresco have same name has the person ('"+name+"').");
                throw new RuntimeException("Could not find folder of '"+name+"' in alfresco!");
            }
        }
    }

    //returns a HashMap containing all the sub folders IDs of the given node ID; Hashmap(nameFolder, folderID)
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
