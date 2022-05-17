package com.CMASProject.SplitReceiptsProject.Enteties;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;

public class Spliter {
    
	public static void spliter(PDDocument document, List<Person> list){
    	try {
			PDFTextStripper pdfStripper = new PDFTextStripper();
			int lastNif = 0;
			PDDocument lastDoc = null;
			for(int i = 1; i <= document.getNumberOfPages(); i++) {
				pdfStripper.setStartPage(i);
				pdfStripper.setEndPage(i);
				String text = pdfStripper.getText(document); 
				for(int l = 0; l < list.size();l++) {
					if (text.contains(list.get(l).getNif().toString())) {
						PDPageTree listTree = document.getPages();
						PDPage page = listTree.get(i-1); 
						PDDocument doc = new PDDocument();
						if(list.get(l).getNif() == lastNif) {
							list.get(l).getPersonNameAndDate(text);
							lastDoc.addPage(page);
							list.get(l).setDocument(lastDoc);
						}
						else {
							doc.addPage(page);
							list.get(l).setDocument(doc);
							lastDoc = doc;
							lastNif = list.get(l).getNif();
						}
						doc.close();
					}
				}
				 
			}
		} catch (IOException e) {
			System.out.println("It was not possible to split the pdf. Error:"+e.getMessage()+"\nExiting program.");
			Runtime.getRuntime().exit(5);
		}
	}
}
