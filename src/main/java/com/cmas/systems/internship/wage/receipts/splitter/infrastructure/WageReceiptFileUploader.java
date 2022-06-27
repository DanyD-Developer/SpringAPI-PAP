package com.cmas.systems.internship.wage.receipts.splitter.infrastructure;

import com.cmas.systems.internship.wage.receipts.splitter.WageReceiptFileSplitterProperties;
import com.cmas.systems.internship.wage.receipts.splitter.domain.Person;
import com.cmas.systems.internship.wage.receipts.splitter.infrastructure.alfresco.AlfrescoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WageReceiptFileUploader {

	private final WageReceiptFileSplitterProperties appProperties;

	private final AlfrescoClient alfrescoClient;

	public void fileUpload( List<Person> persons, String randomName ) {

		String ticket= alfrescoClient.requestTicket(); ;

		try {
			//Read the temporary folder and set the files by Person
			setFilePathPerPerson( new File( appProperties.getTempFolder() + "\\" + randomName ), persons );

			for ( Person person : persons ) {
				assignFoldersID(ticket,person);

				if ( person.getNodeID() == null ) {
					continue;
				}

				boolean isSucceeded = alfrescoClient.uploadFile( ticket, person.getNodeID(),new FileSystemResource(person.getFilePath()));

				if ( isSucceeded ) {
					log.info( person.getName() + " - File Upload to alfresco successfully" );
				}

			}

		}
		catch ( ResourceAccessException e5 ) {
			log.info( "It was not possible to send files to Alfresco." );
			log.error( "Error " + e5 );
			throw new RuntimeException( "Connection Time out" );
		}
		finally {
			alfrescoClient.closeTicket( ticket );
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
	public void assignFoldersID( String ticket, Person person ) {

		Map<String, String> map = alfrescoClient.getNodeChildren( ticket, appProperties.getWageReceiptsNodeid() );

		//Attributes each WR folder id to the correspondent person.
		String name = person.getName();
		if ( name == null ) {
			return;
		}
		if ( map.containsKey( name ) ) {
			String personWgFolderID = alfrescoClient.getNodeChildren( ticket, map.get( name ) ).getOrDefault( "WR", null );
			if ( personWgFolderID != null ) {
				person.setNodeID( personWgFolderID );
			}
			else {
				log.error( "Person '" + name + "' does not have a WR folder!" );
			}
		}
		else {
			log.error( "Could not find folder of '" + name + "' in alfresco! Make sure that the folder in alfresco have same name has the person ('" + name + "')." );
		}
	}

}
