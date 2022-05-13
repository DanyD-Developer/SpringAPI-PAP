package com.CMASProject.SplitReceiptsProject.Enteties;

import org.apache.pdfbox.pdmodel.PDDocument;

public class Person {
    private String name;
    private Integer nif;
    private String password;

    private PDDocument document;

    public Person(Integer nif, String password) {
        this.nif = nif;
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getNif() {
        return nif;
    }
    public void setNif(Integer nif) {
        this.nif = nif;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public PDDocument getDocument() {
        return document;
    }
    public void setDocument(PDDocument document) {
        this.document = document;
    }

    
}
