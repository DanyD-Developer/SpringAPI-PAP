package com.CMASProject.SplitReceiptsProject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.CMASProject.SplitReceiptsProject.Enteties.Config;
import com.CMASProject.SplitReceiptsProject.Enteties.FileHolder;
import com.CMASProject.SplitReceiptsProject.Enteties.Person;
import com.CMASProject.SplitReceiptsProject.Enteties.Protector;
import com.CMASProject.SplitReceiptsProject.Enteties.Spliter;

public class App {

	public static void main(String[] args) {
		String configFilePath = "src\\config.properties";
		Properties props = new Properties();
		
		//Load the properties file
		try (FileInputStream propsInput = new FileInputStream(configFilePath)) {
			props.load(propsInput);
		} catch (Exception e) {
			System.out.println("It was not possible to load the config file. Error:"+e.getMessage()+"\nExiting program.");
			Runtime.getRuntime().exit(1);
		}

		//Loads config
		Config config = new Config(props);
		
		//Loads the files
		FileHolder fileHolder = new FileHolder(config);

		//Create each person 
		List<Person> personsList = new ArrayList<>();
		fileHolder.getNifsAndPasswords().stream().map((nifAndPassword) -> nifAndPassword.split("###")).forEach((nifAndPassword) -> personsList.add(new Person(Integer.parseInt(nifAndPassword[0]),nifAndPassword[1])));
	

		//Splits
		Spliter.spliter(fileHolder.getWagesReceipts(), personsList);

		personsList.forEach((person) -> {if(person.getDocument() != null) {Protector.protectPersonPdf(person, config.getDestinationFolder());}});
		
		try {
			fileHolder.getWagesReceipts().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Task Finished");
	}
}
