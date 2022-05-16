package com.CMASProject.SplitReceiptsProject.Enteties;

import java.util.Properties;

public class Config {
    //Origin folder is where the wage receipts pdf and NamesPassword.txt are located
    //Other types of config can be added here.
    private String originFolder;
    private String destinationFolder;
    private String recieptsPdfFileName;
    private String namesAndPasswordsFileName;

    //Possible feature: allows the user to only do the split whithout protecting the files with password
    // private boolean dontProtect;
    
    public Config(Properties configProps) {
        this.originFolder               = configProps.getProperty("ORIGIN_FOLDER");
        this.destinationFolder          = configProps.getProperty("DESTINATION_FOLDER");
        this.recieptsPdfFileName        = configProps.getProperty("PDFRECEIPTS_FILENAME");
        this.namesAndPasswordsFileName  = configProps.getProperty("PASSWORDS_FILENAME");

        //TODO
    }

    private static void setConfigurations(){
        //TODO
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
