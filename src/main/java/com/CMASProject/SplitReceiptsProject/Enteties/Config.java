package com.CMASProject.SplitReceiptsProject.Enteties;

import java.io.File;

public class Config {
    //Origin folder is where the wage receipts pdf and NamesPassword.txt are located
    private String originFolder;
    private String destinationFolder;

    //Possible feature: allows the user to only do the split whithout protecting the files with password
    // private boolean dontProtect;
    
    public Config(File configFile) {
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
}
