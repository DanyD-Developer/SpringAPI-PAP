package com.cmas.systems.internship.wage.receipts.splitter.infrastructure.alfresco;

import com.cmas.systems.internship.wage.receipts.splitter.domain.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlfrescoNodeIdFinder {

	private final AlfrescoClient alfrescoClient;

	public void assignFoldersID( String ticket, List<Person> list ) {

		String wagesReceiptsFolderID = getWagesReceiptsFolderID( ticket );

		Map<String, String> map = this.alfrescoClient.getNodeChildren( ticket, wagesReceiptsFolderID );

		//Attributes each WR folder id to the correspondent person.
		for ( Person person : list ) {
			String name = person.getName();
			if ( name == null ) {
				continue;
			}
			if ( map.containsKey( name ) ) {
				String personWgFolderID = alfrescoClient.getNodeChildren( ticket, map.get( name ) ).getOrDefault( "WR", null );
				if ( personWgFolderID != null ) {
					person.setNodeID( personWgFolderID );
				}
				else {
					log.error( "Person '" + name + "' does not have a WR folder!" );
					throw new RuntimeException( "Person '" + name + "' does not have a WR folder!" );
				}
			}
			else {
				log.error( "Could not find folder of '" + name + "' in alfresco! Make sure that the folder in alfresco have same name has the person ('" + name + "')." );
				throw new RuntimeException( "Could not find folder of '" + name + "' in alfresco!" );
			}
		}
	}

	private String getWagesReceiptsFolderID( String ticket ) {
		String id = alfrescoClient.getNodeChildren( ticket, "-root-" ).get( "Sites" );
		id = alfrescoClient.getNodeChildren( ticket, id ).get( "people" );
		id = alfrescoClient.getNodeChildren( ticket, id ).get( "documentLibrary" );
		id = alfrescoClient.getNodeChildren( ticket, id ).get( "12.WAGE RECEIPTS" );
		return id;
	}

}
