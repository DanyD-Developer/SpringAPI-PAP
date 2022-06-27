package com.cmas.systems.internship.wage.receipts.splitter.infrastructure;

import com.cmas.systems.internship.wage.receipts.splitter.WageReceiptFileSplitterProperties;
import com.cmas.systems.internship.wage.receipts.splitter.infrastructure.alfresco.AlfrescoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WageReceiptFileUploader {

	private final WageReceiptFileSplitterProperties appProperties;

	private final AlfrescoClient alfrescoClient;

	public void fileUpload( ByteArrayOutputStream byteArrayOutputStream, String personName ) {

		String ticket = alfrescoClient.requestTicket();

		try {
			String nodeId = assignFoldersID( ticket, personName );

			boolean isSucceeded = alfrescoClient.uploadFile( ticket, nodeId, new FileNameAwareByteArrayResource( "rv_" + personName + "_teste.pdf", byteArrayOutputStream.toByteArray() ) );

			if ( isSucceeded ) {
				log.info( personName + " - File Upload to alfresco successfully" );
			}

		}
		catch ( ResourceAccessException e ) {
			log.info( "It was not possible to send files to Alfresco." );
			log.error( "Error " + e );
			throw new RuntimeException( "Connection Time out" );
		}
		finally {
			alfrescoClient.closeTicket( ticket );
		}
	}

	public String assignFoldersID( String ticket, String personName ) {

		Map<String, String> map = alfrescoClient.getNodeChildren( ticket, appProperties.getWageReceiptsNodeid() );

		//Attributes each WR folder id to the correspondent person.
		if ( personName == null ) {
			return null;
		}
		if ( map.containsKey( personName ) ) {
			String personWgFolderID = alfrescoClient.getNodeChildren( ticket, map.get( personName ) ).getOrDefault( "WR", null );
			if ( personWgFolderID != null ) {
				return personWgFolderID;
			}
			else {
				log.error( "Person '" + personName + "' does not have a WR folder!" );
			}
		}
		else {
			log.error( "Could not find folder of '" + personName + "' in alfresco! Make sure that the folder in alfresco have same name has the person ('" + personName + "')." );
		}
		return null;
	}

}
