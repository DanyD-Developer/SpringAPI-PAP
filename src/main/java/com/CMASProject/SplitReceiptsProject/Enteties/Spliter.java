package com.CMASProject.SplitReceiptsProject.Enteties;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * @author Daniel Duarte ( daniel.duarte@cmas-systems.com )
 * @since <next-release>
 */
public class Spliter {
	
	public static void spliter(PDDocument document, List<Person> list){
    	try {
    		//Create the Doucments and the property to read the text
			PDFTextStripper pdfStripper = new PDFTextStripper();
			int lastNif = 0;
			PDDocument lastDoc = null;
			PDDocument doc = null;
			//Make a cycle to Read the document page by page
			for(int i = 1; i <= document.getNumberOfPages(); i++) {
				pdfStripper.setStartPage(i);
				pdfStripper.setEndPage(i);
				String text = pdfStripper.getText(document); 
				
				//Make a cycle per person to give the correspondents page/pages
				for(int l = 0; l < list.size();l++) {
				if (text.contains(list.get(l).getNif().toString())) {
						PDPageTree listTree = document.getPages();
						PDPage page = listTree.get(i-1); 
						doc = new PDDocument();
						
					//If equals to the las niff means he have more than 1 page
					if(list.get(l).getNif() == lastNif) {
						//Add a page and set the document of the person
							lastDoc.addPage(page);
							list.get(l).setDocument(lastDoc);
							doc.close();
						}
					else {
						//If is not equal to the last niff he set the document of the person
							list.get(l).getPersonNameAndDate(text);
							doc.addPage(page);
							list.get(l).setDocument(doc);
							lastDoc = doc;
							lastNif = list.get(l).getNif();
						}
					}
				}	 
				
			}
		} catch (IOException e) {
			System.out.println("It was not possible to split the pdf. Error: "+e.getMessage()+"\nExiting program.");
			Runtime.getRuntime().exit(5);
		}
	}
}
