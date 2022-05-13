package com.CMASProject.SplitReceiptsProject;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class App {

	private static final String OUTPUT_DIR = "C:\\Users\\Daniel Duarte\\Desktop\\Ecrypted\\Images\\";

	public static void main(String[] args) {

//		//Criação de cada pagina em png
//		try(PDDocument doc = PDDocument.load(new File("C:\\Users\\Daniel Duarte\\Desktop\\Ecrypted\\example_rv_2015_projeto_split_docs.pdf"))) {		
//			PDFRenderer renderer = new PDFRenderer(doc);
//			for(int p = 0; p < doc.getNumberOfPages();p++) {
//				BufferedImage image = renderer.renderImage(p,2.5f);
//				ImageIO.write(image, "JPEG", new File("C:\\Users\\Daniel Duarte\\Desktop\\Ecrypted\\Pagina " + (p + 1) + ".png"));
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		try (final PDDocument document = PDDocument.load(
				new File("C:\\Users\\Daniel Duarte\\Desktop\\Ecrypted\\example_rv_2015_projeto_split_docs.pdf"))) {

			PDPageTree list = document.getPages();
			PDPage page = list.get(1);  
			PDResources pdResources = page.getResources();
			int i = 1;
			for (COSName name : pdResources.getXObjectNames()) {
				PDXObject o = pdResources.getXObject(name);
				if (o instanceof PDImageXObject) {
					PDImageXObject image = (PDImageXObject) o;
					String filename = OUTPUT_DIR + "extracted-image-" + i + ".png";
					ImageIO.write(image.getImage(), "png", new File(filename));
					i++;
				}
			}

		} catch (IOException e) {
			System.err.println("Exception while trying to create pdf document - " + e);
		}

		// Leitura de uma pagina png
//		String imagePath = "C:\\Users\\Daniel Duarte\\Desktop\\Ecrypted\\" + "Amam.jpg";
//
//		AsposeOCR api = new AsposeOCR();
//
//		try {
//			String result = api.RecognizePage(imagePath);
//			System.out.println("Result: " + result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		// Encrypting Document
//    	try(PDDocument doc = PDDocument.load(new File("C:\\Users\\Daniel Duarte\\Desktop\\example_rv_2015_projeto_split_docs.pdf"))) {
//			
//    		AccessPermission accessPermission = new AccessPermission();
//			StandardProtectionPolicy spp = new StandardProtectionPolicy("4321","12345",accessPermission);
//			spp.setEncryptionKeyLength(128);
//			spp.setPermissions(accessPermission);
//			doc.protect(spp);
//			doc.save("C:\\Users\\Daniel Duarte\\Desktop\\Ecrypted\\Encrypted.pdf");
//			doc.close();
//			
//		} 
//    	catch (IOException e) {		
//			e.printStackTrace();
//		}
	}

}
