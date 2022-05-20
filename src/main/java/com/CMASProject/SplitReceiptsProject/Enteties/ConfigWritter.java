package com.CMASProject.SplitReceiptsProject.Enteties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConfigWritter {

	public static void Write(File FilePath, File Folderpath) {
		if (!(Folderpath.exists())) {
			if (Folderpath.mkdir()) {
			}
		}
		if (!(FilePath.exists())) {
			try {
				if (FilePath.createNewFile()) {
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
		else {
			try {
				FileWriter myWriter = new FileWriter(FilePath);
				String data = "";
				Scanner myReader = new Scanner(FilePath);
				while (myReader.hasNextLine()) {
					data = myReader.nextLine();
				}
				myReader.close();
				if (!(data.contains("ORIGIN_FOLDER=")) || !(data.contains("DESTINATION_FOLDER="))
						|| !(data.contains("PDFRECEIPTS_FILENAME=")) || !(data.contains("PASSWORDS_FILENAME="))) {
					myWriter.write("ORIGIN_FOLDER=");
					myWriter.write("\nDESTINATION_FOLDER=");
					myWriter.write("\nPDFRECEIPTS_FILENAME=");
					myWriter.write("\nPASSWORDS_FILENAME=");
					myWriter.close();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
