package com.CMASProject.SplitReceiptsProject.Enteties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Scanner;

public class Config {
	// Origin folder is where the wage receipts pdf and NamesPassword.txt are
	// located
	// Other types of config can be added here.
	private String originFolder;
	private String destinationFolder;
	private String recieptsPdfFileName;
	private String namesAndPasswordsFileName;

	// Possible feature: allows the user to only do the split whithout protecting
	// the files with password
	// private boolean dontProtect;

	public Config(Properties configProps, File FilePath) {
			this.originFolder = configProps.getProperty("ORIGIN_FOLDER");
			this.destinationFolder = configProps.getProperty("DESTINATION_FOLDER");
			this.recieptsPdfFileName = configProps.getProperty("PDFRECEIPTS_FILENAME");
			this.namesAndPasswordsFileName = configProps.getProperty("PASSWORDS_FILENAME");
		try {
			if (originFolder.isEmpty() || destinationFolder.isEmpty() || recieptsPdfFileName.isEmpty()
					|| namesAndPasswordsFileName.isEmpty()) {
				setConfigurations(configProps, FilePath);
			}
		}
		catch(NullPointerException e){
			String path = System.getenv().get("APPDATA") + "\\SplitProject";
			File Folderpath = new File(path);
			
			ConfigWritter.Write(FilePath, Folderpath);
		}
	}

	private void setConfigurations(Properties configProps, File FilePath) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Insert the path to the folder where the pdf and password are located:");
		String path_origin = sc.nextLine();
		System.out.println("Insert the path to the destination folder:");
		String path_destination = sc.nextLine();
		System.out.println("Insert the pdf file name:");
		String pdfname = sc.nextLine();
		System.out.println("Insert the password file name:");
		String passwordfile = sc.nextLine();
		
		while (path_origin.isEmpty() || path_destination.isEmpty() || pdfname.isEmpty() || passwordfile.isEmpty()){
			System.out.println("Insert Again: ");
			
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
		sc.close();
		try {
			configProps.store(new FileOutputStream(FilePath), null);
		} catch (Exception e) {
			System.out.println("It was not possible to save to the config file. Error: " + e.getMessage() + "\nExiting program.");
			Runtime.getRuntime().exit(2);
		}

		// Load the properties file
		try (FileInputStream propsInput = new FileInputStream(FilePath)) {
			configProps.load(propsInput);
		} catch (Exception e) {
			System.out.println(
					"It was not possible to load the config file. Error: " + e.getMessage() + "\nExiting program.");
			Runtime.getRuntime().exit(1);
		}

		this.originFolder = configProps.getProperty("ORIGIN_FOLDER");
		this.destinationFolder = configProps.getProperty("DESTINATION_FOLDER");
		this.recieptsPdfFileName = configProps.getProperty("PDFRECEIPTS_FILENAME");
		this.namesAndPasswordsFileName = configProps.getProperty("PASSWORDS_FILENAME");
	}

	public String getOriginFolder() {
		return originFolder;
	}

	public void setOriginFolder(String originFolder) {
		this.originFolder = originFolder;
	}

	public String getDestinationFolder() {
		return destinationFolder;
	}

	public void setDestinationFolder(String destinationFolder) {
		this.destinationFolder = destinationFolder;
	}

	public String getRecieptsPdfFileName() {
		return recieptsPdfFileName;
	}

	public void setRecieptsPdfFileName(String recieptsPdfFileName) {
		this.recieptsPdfFileName = recieptsPdfFileName;
	}

	public String getNamesAndPasswordsFileName() {
		return namesAndPasswordsFileName;
	}

	public void setNamesAndPasswordsFileName(String namesAndPasswordsFileName) {
		this.namesAndPasswordsFileName = namesAndPasswordsFileName;
	}
}
