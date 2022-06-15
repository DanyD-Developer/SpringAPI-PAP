package com.CMASProject.SplitReceiptsProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.CMASProject.SplitReceiptsProject.Controllers.NodeIdFinder;
import com.CMASProject.SplitReceiptsProject.Configuration.RestConfig;
import com.CMASProject.SplitReceiptsProject.Services.TicketManager;
import com.CMASProject.SplitReceiptsProject.Controllers.UploadFile;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.CMASProject.SplitReceiptsProject.Enteties.Config;
import com.CMASProject.SplitReceiptsProject.Enteties.FileHolder;
import com.CMASProject.SplitReceiptsProject.Enteties.Person;
import com.CMASProject.SplitReceiptsProject.Enteties.Protector;
import com.CMASProject.SplitReceiptsProject.Enteties.Spliter;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		
		//SpringApplication.run(App.class, args);
		
		String path = System.getenv().get("APPDATA") + "\\SplitProject";
		String configFilePath = path + "\\config.properties";
		File FilePath = new File(configFilePath);
		File Folderpath = new File(path);
		
		//Loads config
		Config config = new Config(Folderpath, FilePath);
		
		//Loads the files
		FileHolder fileHolder = new FileHolder(config);

		//Create each person 
		List<Person> personsList = new ArrayList<>();
		try {
			fileHolder.getNifsAndPasswords().stream().map((nifAndPassword) -> nifAndPassword.split("###")).forEach((nifAndPassword) -> personsList.add(new Person(Integer.parseInt(nifAndPassword[0]),nifAndPassword[1])));
		} catch (NumberFormatException e) {
			System.out.println("Error when reading the NIFs. Error: "+e.getMessage()+"\nExiting Program");
			Runtime.getRuntime().exit(7);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("One of the NIFs does not contains password. Error: "+e.getMessage()+"\nExiting Program");
			Runtime.getRuntime().exit(8);
		}
	
 
		//Splits
		Spliter.spliter(fileHolder.getWagesReceipts(), personsList);
		
		//Checks if it was made any split
		splitVerification(personsList);
		
		personsList.forEach((person) -> {if(person.getDocument() != null) {Protector.protectPersonPdf(person, config.getDestinationFolder());}});

		Scanner sc = new Scanner(System.in);

		//Question to the user if he wants to send the files to alfresco
		System.out.println("Do you wish to send the files to alfresco?");
		System.out.print("(y/n): "); String answer = sc.nextLine().toLowerCase();
		switch(answer){
			case "y":
				UploadFile.fileUpload(personsList, config, fileHolder);
				break;
			default:
				break;
		}

		try {
			fileHolder.getWagesReceipts().close();
		} catch (IOException e) {System.out.println("Error: "+e.getMessage());}


		System.out.println("Task Finished");
		System.exit(0);
		sc.close();
	}


	/**
	 * @param personsList
	 */
	private static void splitVerification(List<Person> personsList) {
		int f = 0;
		for(Person p : personsList) {
			if(p.getDocument() == null) {
				f++;
			}

		}
		
		if(f == personsList.size()) {
			System.out.println("It was not performed any split, maybe you selected the wrong pdf file OR \nYou Are Missing NIFs in the Passwords file");
			Runtime.getRuntime().exit(18);
		}
	}


}
