package com.CMASProject.SplitReceiptsProject.Enteties;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

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
	}
}
