package com.cmas.systems.internship.wage.receipts.splitter.infrastructure;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.cmas.systems.internship.wage.receipts.splitter.WageReceiptFileSplitterProperties;
import com.cmas.systems.internship.wage.receipts.splitter.domain.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class WageReceiptFileProtector {

	private final WageReceiptFileSplitterProperties appProperties;

	public void protectPersonPdf(Person person, String randomName){
    	try  {
			String destinationPath = appProperties.getTempFolder() + "\\" +randomName;
			if(person.getDocument() != null){
				//Read the PDF files and save on the person
				person.getDocument().save(destinationPath + "\\Rv_"+person.getProcessDate()+" - "+person.getName()+".pdf" );
				PDDocument document = PDDocument.load(new File(destinationPath + "\\Rv_"+person.getProcessDate()+" - "+person.getName()+".pdf" ));
				person.setDocument(document);

				//Encrypt the pdf document
				AccessPermission accessPermission = new AccessPermission();
				StandardProtectionPolicy spp = new StandardProtectionPolicy(person.getPassword(), person.getPassword(),accessPermission);
				spp.setEncryptionKeyLength(256);
				spp.setPermissions(accessPermission);
				person.getDocument().protect(spp);
				String fileName = "Rv_"+person.getProcessDate()+" - "+person.getName()+".pdf";
				person.getDocument().save(destinationPath + "\\"+fileName);
				person.getDocument().close();
				document.close();
				log.info("Created - '"+fileName+"'");
			}
		} catch (IOException e) {
			log.error("It was not possible to protect the pdfs of "+ person.getName()+". Error: "+e.getMessage());
			throw new RuntimeException("It was not possible to protect the pdfs");
		}
	}
}
