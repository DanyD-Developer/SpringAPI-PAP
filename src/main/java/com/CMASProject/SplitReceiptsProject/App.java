package com.CMASProject.SplitReceiptsProject;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//    	PDFTextStripper pdfStripper = null;
//        PDDocument pdDoc = null;
//        COSDocument cosDoc = null;
//        File file = new File("C:\\Users\\Daniel Duarte\\Desktop\\Projeto-web-services-Spring-Boot-JPA.pdf");
//        try {
//            // PDFBox 2.0.8 require org.apache.pdfbox.io.RandomAccessRead 
//             RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
//             PDFParser parser = new PDFParser(randomAccessFile);               
//            parser.parse();
//            cosDoc = parser.getDocument();
//            pdfStripper = new PDFTextStripper();
//            pdDoc = new PDDocument(cosDoc);
//            pdfStripper.setStartPage(1);
//            pdfStripper.setEndPage(2);
//            String parsedText = pdfStripper.getText(pdDoc);
//            System.out.println(parsedText);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } 
    	
    	try {
			PDDocument doc = PDDocument.load(new File("C:\\Users\\Daniel Duarte\\Desktop\\Projeto-web-services-Spring-Boot-JPA.pdf"));
			String text = new PDFTextStripper().getText(doc);
			System.out.println(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
