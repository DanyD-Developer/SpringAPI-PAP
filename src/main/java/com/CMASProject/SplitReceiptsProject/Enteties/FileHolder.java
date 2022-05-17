package com.CMASProject.SplitReceiptsProject.Enteties;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

public class FileHolder {
    
    private PDDocument wagesReceipts;
    private List<String> nifsAndPasswords;


    public FileHolder(Config config) {
        String pdfPath = config.getOriginFolder()+"\\"+config.getRecieptsPdfFileName();
        String passwordPath = config.getOriginFolder()+"\\"+config.getNamesAndPasswordsFileName();

        PDFLoader(pdfPath);
        passwordLoader(passwordPath);
    }

    private void PDFLoader(String path){
        try{
            PDDocument document = PDDocument.load(new File(path));
            wagesReceipts = document;
        } catch (Exception e) {
            System.out.println("It was not possible to load the wages receipts pdf file. Error:"+e.getMessage()+"\nExiting program.");
			Runtime.getRuntime().exit(2);
        }
    }

    private void passwordLoader(String path){
        try{
            List<String> lines = Collections.emptyList();
            lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            nifsAndPasswords = lines;
        } catch (Exception e) {
            System.out.println("It was not possible to load the passwords file. Error:"+e.getMessage()+"\nExiting program.");
			Runtime.getRuntime().exit(3);
        }
    }

    public PDDocument getWagesReceipts() {
        return wagesReceipts;
    }
    public void setWagesReceipts(PDDocument wagesReceipts) {
        this.wagesReceipts = wagesReceipts;
    }

    public List<String> getNifsAndPasswords() {
        return nifsAndPasswords;
    }
    public void setNifsAndPasswords(List<String> nifsAndPasswords) {
        this.nifsAndPasswords = nifsAndPasswords;
    }
}
