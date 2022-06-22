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



    public FileHolder(File filePDF) {
        PDFLoader(filePDF);
    }

    private void PDFLoader(File file){

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

