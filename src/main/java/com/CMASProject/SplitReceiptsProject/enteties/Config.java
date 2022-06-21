package com.CMASProject.SplitReceiptsProject.enteties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.Scanner;

public class Config {
	private final Properties  configProps = new Properties();
	private final Scanner sc = new Scanner(System.in);
	private final File filePath;

	public Config(File folderpath, File FilePath) {
		this.loadProperties(FilePath, folderpath);
		this.filePath = FilePath;
		
		if(!configProps.containsKey("ORIGIN_FOLDER") || !configProps.containsKey("DESTINATION_FOLDER") || !configProps.containsKey("PDFRECEIPTS_FILENAME") || !configProps.containsKey("PASSWORDS_FILENAME")) {
			this.setInitialSetings(FilePath, folderpath);
		}
		else {
				System.out.println("Do you wish to split with the previous settings?");
				System.out.println("Origin Folder: "+ this.getOriginFolder());
				System.out.println("Destination Folder: "+ this.getDestinationFolder());
				System.out.println("Pdf Receipt file name: "+ this.getReceiptsPdfFileName());
				System.out.println("Passwords file name: "+ this.getNamesAndPasswordsFileName());
				
				System.out.print("(y/c/n): "); String option = sc.nextLine().toLowerCase();
				
				
				switch(option) {
					case "y":
					break;
					case "c":
						updateWageReceiptsName();
						break;
					case "n":
						this.setInitialSetings(FilePath, folderpath);
					break;
					default:
						break;
				}
		}
	}

	private void updateWageReceiptsName(){
		String name = "";
		while (name.isEmpty()){
			System.out.println("Type the new name of the wages receipts file.");
			name = sc.nextLine();
		}

		setReceiptsPdfFileName(name);

		storeProperties();
	}
	
	//Asks for the settings and saves them
	private void setInitialSetings(File FilePath, File Folderpath) {
		
		
		System.out.println("Insert the path to the folder where the pdf and password are located:");
		String path_origin = sc.nextLine();
		System.out.println("Insert the path to the destination folder:");
		String path_destination = sc.nextLine();
		System.out.println("Insert the pdf file name:");
		String pdfname = sc.nextLine();
		System.out.println("Insert the password file name:");
		String passwordfile = sc.nextLine();
		
		while (path_origin.isEmpty() || path_destination.isEmpty() || pdfname.isEmpty() || passwordfile.isEmpty()){
			
			System.out.println("Insert the path to the folder where the pdf and password are located:");
			path_origin = sc.nextLine();
			System.out.println("Insert the path to the destination folder:");
			path_destination = sc.nextLine();
			System.out.println("Insert the pdf file name:");
			pdfname = sc.nextLine();
			System.out.println("Insert the password file name:");
			passwordfile = sc.nextLine();
		}

		configProps.setProperty("ORIGIN_FOLDER", path_origin);
		configProps.setProperty("DESTINATION_FOLDER", path_destination);
		configProps.setProperty("PDFRECEIPTS_FILENAME", pdfname);
		configProps.setProperty("PASSWORDS_FILENAME", passwordfile);
		
		storeProperties();

		loadProperties(FilePath, Folderpath);
	}


	private void storeProperties() {
		try {
			configProps.store(Files.newOutputStream(this.filePath.toPath()), null);
		} catch (Exception e) {
			System.out.println("It was not possible to save to the config file. Error: " + e.getMessage() + "\nExiting program.");
			Runtime.getRuntime().exit(2);
		}
	}

	private void loadProperties(File FilePath, File FolderPath) {
		// Loads the properties file
		try (FileInputStream propsInput = new FileInputStream(FilePath)) {
			configProps.load(propsInput);
		} catch(FileNotFoundException e) {
			this.createConfigFile(FilePath, FolderPath);
			
		} catch (Exception e) {
			System.out.println("It was not possible to load the config file.\n Error: " + e.getMessage() + "\nExiting program.");
			Runtime.getRuntime().exit(1);
		}
	}
	
	public void createConfigFile(File FilePath, File Folderpath) {
		//Creates the folder in APPDATA\Roaming
		if (!(Folderpath.exists())) {
			if(!Folderpath.mkdir()) {
				System.out.println("It was not possible to create the Folder in APPDATA.");
			}
		}
		//Creates the Properties file and fills it with the settings
		if (!(FilePath.exists())) {
			setInitialSetings(FilePath, Folderpath);
		}
	}

	public void updateAlfrescoSettings() {
		System.out.println("Insert the URL to alfresco. (Ex: https://alfresco-nowo.cmas-systems.com)");
		String url = sc.nextLine();
		System.out.println("Insert the username.");
		String username = sc.nextLine();
		System.out.println("Insert th PassWord.");
		String password = sc.nextLine();
		while(url.isEmpty() || username.isEmpty() || password.isEmpty()){
			System.out.println("Insert the URL to alfresco. (Ex: https://alfresco-nowo.cmas-systems.com)");
			url = sc.nextLine();
			System.out.println("Insert the username.");
			username = sc.nextLine();
			System.out.println("Insert th PassWord.");
			password = sc.nextLine();
		}

		configProps.setProperty("ALFRESCO_URL",url);
		configProps.setProperty("ALFRESCO_USERNAME",username);
		configProps.setProperty("ALFRESCO_PASSWORD",password);

		storeProperties();
	}

	public String getOriginFolder() {
		return configProps.getProperty("ORIGIN_FOLDER");
	}

//	public void setOriginFolder(String originFolder) {
//		this.configProps.setProperty("ORIGIN_FOLDER", originFolder);
//	}

	public String getDestinationFolder() {
		return configProps.getProperty("DESTINATION_FOLDER");
	}

//	public void setDestinationFolder(String destinationFolder) {
//		this.configProps.setProperty("DESTINATION_FOLDER", destinationFolder);
//	}

	public String getReceiptsPdfFileName() {
		return configProps.getProperty("PDFRECEIPTS_FILENAME");
	}

	public void setReceiptsPdfFileName(String receiptsPdfFileName) {
		this.configProps.setProperty("PDFRECEIPTS_FILENAME", receiptsPdfFileName);
	}

	public String getNamesAndPasswordsFileName() {
		return configProps.getProperty("PASSWORDS_FILENAME");
	}

//	public void setNamesAndPasswordsFileName(String namesAndPasswordsFileName) {
//		this.configProps.setProperty("PASSWORDS_FILENAME", namesAndPasswordsFileName);
//	}

	public String getAlfrescoURL() {
		return configProps.getProperty("ALFRESCO_URL", "");
	}

	public String getAlfrescoUsername() {
		return configProps.getProperty("ALFRESCO_USERNAME", "");
	}

	public String getAlfrescoPassword() {
		return configProps.getProperty("ALFRESCO_PASSWORD", "");
	}
}
