package com.CMASProject.SplitReceiptsProject;

import java.io.FileInputStream;
import java.util.Properties;

import com.CMASProject.SplitReceiptsProject.Enteties.Config;

public class App {

	public static void main(String[] args) {
		String configFilePath = "src\\config.properties";
		Properties props = new Properties();
		
		//Load the properties file
		try (FileInputStream propsInput = new FileInputStream(configFilePath)) {
			props.load(propsInput);
		} catch (Exception e) {
			System.out.println("It was not possible to load the config file. Error:"+e.getMessage()+"Exiting program.");
			Runtime.getRuntime().exit(1);
		}

		Config conf = new Config(props);
	}
}
