package com.CMASProject.SplitReceiptsProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import com.CMASProject.SplitReceiptsProject.controllers.UploadFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.CMASProject.SplitReceiptsProject.enteties.Config;
import com.CMASProject.SplitReceiptsProject.enteties.FileHolder;
import com.CMASProject.SplitReceiptsProject.enteties.Person;
import com.CMASProject.SplitReceiptsProject.enteties.Protector;
import com.CMASProject.SplitReceiptsProject.enteties.Splitter;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		
		SpringApplication.run(App.class, args);
	}
}
