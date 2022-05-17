package com.CMASProject.SplitReceiptsProject.Enteties;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

public class Protector {
    
	public static void protectPersonPdf(Person person, String destinationPath){
    	try  {
			AccessPermission accessPermission = new AccessPermission();
			StandardProtectionPolicy spp = new StandardProtectionPolicy(person.getPassword(), person.getPassword(),accessPermission);
			spp.setEncryptionKeyLength(128);
			spp.setPermissions(accessPermission);
			person.getDocument().protect(spp);
			person.getDocument().save(destinationPath + "Rv_"+person.getProcessDate()+" - "+person.getName());
			person.getDocument().close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
