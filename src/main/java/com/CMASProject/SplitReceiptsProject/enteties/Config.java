package com.CMASProject.SplitReceiptsProject.enteties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.Scanner;

public class Config {
	private final String folderpath = System.getenv().get("APPDATA") + "\\TemporaryFolder";
	private final ObjectMapper mapper = new ObjectMapper();
	private JsonNode credentials;

	public String getFolderpath() {
		return folderpath;
	}

	private void CreateTemporaryFolder(String path){

	}
}
