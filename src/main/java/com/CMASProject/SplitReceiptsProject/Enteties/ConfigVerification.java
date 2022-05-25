package com.CMASProject.SplitReceiptsProject.Enteties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ConfigVerification {

	public static void Verification(File FilePath, String ConfigPath, Properties props) {

		if (FilePath.exists()) {

			String originFolder = props.getProperty("ORIGIN_FOLDER");
			String destinationFolder = props.getProperty("DESTINATION_FOLDER");
			String recieptsPdfFileName = props.getProperty("PDFRECEIPTS_FILENAME");
			String namesAndPasswordsFileName = props.getProperty("PASSWORDS_FILENAME");
			try {
				if (originFolder.isEmpty() || destinationFolder.isEmpty() || recieptsPdfFileName.isEmpty()
						|| namesAndPasswordsFileName.isEmpty()) {
					FileWriter myWriter = new FileWriter(FilePath);
					myWriter.write("ORIGIN_FOLDER=");
					myWriter.write("\nDESTINATION_FOLDER=");
					myWriter.write("\nPDFRECEIPTS_FILENAME=");
					myWriter.write("\nPASSWORDS_FILENAME=");
					myWriter.close();
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
