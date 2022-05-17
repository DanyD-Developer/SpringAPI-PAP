package com.CMASProject.SplitReceiptsProject.Enteties;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

public class Protector {
    
	public static void protectPersonPdf(Person person, String destinationPath){
    	try  {
			AccessPermission accessPermission = new AccessPermission();
			StandardProtectionPolicy spp = new StandardProtectionPolicy("4321", person.getPassword(),accessPermission);
			spp.setEncryptionKeyLength(128);
			spp.setPermissions(accessPermission);
			person.getDocument().protect(spp);
			person.getDocument().save(destinationPath);
			person.getDocument().close();

		} catch (IOException e) {
			System.out.println("It was not possible to protect the pdfs. Error:"+e.getMessage()+"\nExiting program.");
			Runtime.getRuntime().exit(6);
		}
	}
}
