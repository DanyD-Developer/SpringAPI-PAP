package com.CMASProject.SplitReceiptsProject.Enteties;

import org.apache.pdfbox.pdmodel.PDDocument;

public class FileHolder {
    
    private PDDocument wagesReceipts;
    private String nifsAndPasswords;


    public FileHolder(Config config) {
        //TODO
    }

    private void PDFLoader(String path){
        //TODO
    }

    private void passwordLoader(String path){
        //TODO
    }

    public PDDocument getWagesReceipts() {
        return wagesReceipts;
    }
    public void setWagesReceipts(PDDocument wagesReceipts) {
        this.wagesReceipts = wagesReceipts;
    }

    public String getNifsAndPasswords() {
        return nifsAndPasswords;
    }
    public void setNifsAndPasswords(String nifsAndPasswords) {
        this.nifsAndPasswords = nifsAndPasswords;
    }
}
