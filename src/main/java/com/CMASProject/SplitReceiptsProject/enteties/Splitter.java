package com.CMASProject.SplitReceiptsProject.enteties;

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
public class Splitter {
	
	public static void splitter(PDDocument document, List<Person> list){
    	try {
    		//Create the Documents and the property to read the text
			PDFTextStripper pdfStripper = new PDFTextStripper();
			int lastNif = 0;
			PDDocument lastDoc = null;
			PDDocument doc;
			//Make a cycle to Read the document page by page
			for(int i = 1; i <= document.getNumberOfPages(); i++) {
				pdfStripper.setStartPage(i);
				pdfStripper.setEndPage(i);
				String text = pdfStripper.getText(document);

				//Make a cycle per person to give the correspondents page/pages
				for (Person person : list) {
					if (text.contains(person.getNif().toString())) {
						PDPageTree listTree = document.getPages();
						PDPage page = listTree.get(i - 1);
						doc = new PDDocument();

						//If equals to the last niff means he have more than 1 page
						if (person.getNif() == lastNif) {
							//Add a page and set the document of the person
							if(lastDoc != null){
								lastDoc.addPage(page);
								person.setDocument(lastDoc);
								doc.close();
							}
						} else {
							//If is not equal to the last niff he set the document of the person
							person.getPersonNameAndDate(text);
							doc.addPage(page);
							person.setDocument(doc);
							lastDoc = doc;
							lastNif = person.getNif();
						}
					}
				}	 
				
			}
		} catch (IOException e) {
			System.out.println("It was not possible to split the pdf.\nError: "+e.getMessage()+"\nExiting program.");
			Runtime.getRuntime().exit(5);
		}
	}
}
