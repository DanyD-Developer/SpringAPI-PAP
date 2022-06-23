package com.cmas.systems.internship.wage.receipts.splitter.infrastructure;

import com.cmas.systems.internship.wage.receipts.splitter.WageReceiptFileSplitterProperties;
import com.cmas.systems.internship.wage.receipts.splitter.infrastructure.alfresco.AlfrescoNodeIdFinder;
import com.cmas.systems.internship.wage.receipts.splitter.domain.Person;
import com.cmas.systems.internship.wage.receipts.splitter.infrastructure.alfresco.AlfrescoClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WageReceiptFileUploader {

	private final WageReceiptFileSplitterProperties appProperties;

	private final AlfrescoClient alfrescoClient;

	private final AlfrescoNodeIdFinder nodeIdFinder;

	public void fileUpload( List<Person> persons, String randomName ) {

		String ticket = alfrescoClient.requestTicket();

		try {

			nodeIdFinder.assignFoldersID( ticket, persons );

			//Read the temporary folder and set the files by Person
			setFilePathPerPerson( new File( appProperties.getTempFolder() + "\\" + randomName ), persons );

			for ( Person person : persons ) {
				if ( person.getNodeID() == null ) {
					continue;
				}

				boolean isSucceeded = alfrescoClient.uploadFile( ticket, person.getNodeID(),new FileSystemResource(person.getFilePath()));

				if ( isSucceeded ) {
					log.info( person.getName() + " - File Upload to alfresco successfully" );
				}

			}

		}
		/*
		catch ( HttpClientErrorException e4 ) {
			log.info( "It was not possible to send files to Alfresco." );
			log.error( "Error " + e4.getStatusCode().value() + " " + e4.getStatusText() + " - " + makeErrorMessage( e4.getResponseBodyAsString() ) );
			throw new RuntimeException( makeErrorMessage( e4.getResponseBodyAsString() ) );
		}
		*/

		catch ( ResourceAccessException e5 ) {
			log.info( "It was not possible to send files to Alfresco." );
			log.error( "Error " + e5 );
			throw new RuntimeException( "Connection Time out" );
		}
		finally {
			alfrescoClient.closeTicket( ticket );
		}
	}

	private static String makeErrorMessage( String JSONmensage ) {
		try {
			JsonNode root = new ObjectMapper().readTree( JSONmensage );
			return root.get( "error" ).get( "briefSummary" ).asText();
		}
		catch ( JsonProcessingException e ) {
			return JSONmensage;
		}
	}

	public void setFilePathPerPerson(File folder, List<Person> persons ) {
		for ( final File fileEntry : Objects.requireNonNull( folder.listFiles() ) ) {
			for ( Person person : persons ) {
				if ( person.getName() != null ) {
					if ( fileEntry.getName().contains( person.getName() ) ) {
						String path = folder + "\\" + fileEntry.getName();
						person.setFilePath(path);
					}
				}
			}
		}
	}
}
