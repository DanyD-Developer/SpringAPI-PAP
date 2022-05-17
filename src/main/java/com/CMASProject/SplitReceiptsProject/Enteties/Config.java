package com.CMASProject.SplitReceiptsProject.Enteties;

import java.io.FileOutputStream;
import java.io.IOException;
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

    public Config(Properties configProps) {
        this.originFolder = configProps.getProperty("ORIGIN_FOLDER");
        this.destinationFolder = configProps.getProperty("DESTINATION_FOLDER");
        this.recieptsPdfFileName = configProps.getProperty("PDFRECEIPTS_FILENAME");
        this.namesAndPasswordsFileName = configProps.getProperty("PASSWORDS_FILENAME");

        if(originFolder == "" || destinationFolder == "" || recieptsPdfFileName == "" || namesAndPasswordsFileName == ""){
            setConfigurations(configProps);
        }
    }

    private static void setConfigurations(Properties configProps) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert the path to the folder where the pdf and password are located:");
        String path_origin = sc.nextLine();
        System.out.println("Insert the path to the destination folder:");
        String path_destination = sc.nextLine();
        System.out.println("Insert the pdf file name:");
        String pdfname = sc.nextLine();
        System.out.println("Insert the password file name:");
        String passwordfile = sc.nextLine();
        
        configProps.setProperty("ORIGIN_FOLDER", path_origin);
        configProps.setProperty("DESTINATION_FOLDER", path_destination);
        configProps.setProperty("DESTINATION_FOLDER", pdfname);
        configProps.setProperty("PASSWORDS_FILENAME", passwordfile);

        try {
            configProps.store(new FileOutputStream("src\\config.properties"),null);
        } catch (Exception e) {
            System.out.println("It was not possible to save to the config file. Error:"+e.getMessage()+"\nExiting program.");
			Runtime.getRuntime().exit(2);
        }
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
