package com.CMASProject.SplitReceiptsProject.enteties;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.FileSystemResource;

public class FileHolder {
    
    private PDDocument wagesReceipts;
    private List<String> nifsAndPasswords;


    public FileHolder(Config config) {
        String pdfPath = config.getOriginFolder()+ "\\";
        pdfPath += config.getReceiptsPdfFileName()+ ".pdf";
        
        String passwordPath = config.getOriginFolder()+"\\"+config.getNamesAndPasswordsFileName()+".txt"; 
        
        
        PDFLoader(pdfPath);
        passwordLoader(passwordPath);
    }

    private void PDFLoader(String path){
        try{
            wagesReceipts = PDDocument.load(new File(path));
        } catch (Exception e) {
            System.out.println("It was not possible to load the wages receipts pdf file. Error: "+e.getMessage()+"\nExiting program.");
			Runtime.getRuntime().exit(3);
        }
    }

    private void passwordLoader(String path){
        try{
            List<String> lines;
            lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            if(!(lines.isEmpty())) {
            	nifsAndPasswords = lines;
            }
            else {
            	throw new Exception(" The password file is Empty");
            }
        } catch (Exception e) {
            System.out.println("It was not possible to load the passwords file. Error: "+e.getMessage()+"\nExiting program.");
			Runtime.getRuntime().exit(4);
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

    public void setFilePerPerson(File folder, List<Person> persons) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            for(Person person : persons){
                if(person.getName() != null){
                    if(fileEntry.getName().contains(person.getName())) {
                        String path = folder +"\\"+ fileEntry.getName();
                        FileSystemResource file = new FileSystemResource(path);
                        person.setFile(file);
                    }
                }
            }
        }
    }
}
