package com.cmas.systems.internship.wage.receipts.splitter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class WageReceiptFileSplitterApplication {

	public static void main( String[] args ) {
		SpringApplication.run( WageReceiptFileSplitterApplication.class, args );
	}
}
