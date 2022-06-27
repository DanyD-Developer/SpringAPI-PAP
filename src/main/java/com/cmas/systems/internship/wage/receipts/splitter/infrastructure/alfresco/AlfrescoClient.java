package com.cmas.systems.internship.wage.receipts.splitter.infrastructure.alfresco;

import com.cmas.systems.internship.wage.receipts.splitter.WageReceiptFileSplitterProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * @author Nelson Neves ( nelson.neves@cmas-systems.com )
 * @since <next-release>
 */

@Slf4j
@Service
public class AlfrescoClient {

	private final String ticketUrl;

	private final WageReceiptFileSplitterProperties.AlfrescoProperties alfrescoProperties;

	private final RestTemplate restTemplate;

	public AlfrescoClient( WageReceiptFileSplitterProperties appProperties, RestTemplate restTemplate ) {
		this.restTemplate = restTemplate;
		this.alfrescoProperties = appProperties.getAlfrescoProperties();
		this.ticketUrl = alfrescoProperties.getUrl() + "/alfresco/api/-default-/public/authentication/versions/1/tickets";
	}

	public boolean uploadFile( String ticket, String nodeID, Resource file ) {
		String url = alfrescoProperties.getUrl() + "/alfresco/api/-default-/public/alfresco/versions/1/nodes/" + nodeID + "/children?alf_ticket=" + ticket;

		//Make the headers be the read by form data
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType( MediaType.MULTIPART_FORM_DATA );

		//Crate a map and save the file and the property overwrite for the post
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add( "filedata", file );
		body.add( "overwrite", true );

		//Make the request with the data and make a post with the URL given
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>( body, headers );
		ResponseEntity<String> request = this.restTemplate.postForEntity( url, requestEntity, String.class );
		return request.getStatusCode() == HttpStatus.CREATED;
	}

	public Map<String, String> getNodeChildren( String ticket, String nodeID ) {

		String url = format( "%s/alfresco/api/-default-/public/alfresco/versions/1/nodes/%s/children?alf_ticket=%s", alfrescoProperties.getUrl(), nodeID, ticket );

		JsonNode jsonNode = restTemplate.getForObject( url, JsonNode.class );
		JsonNode entries = jsonNode.get( "list" ).get( "entries" );
		JsonNode pagination = jsonNode.get( "list" ).get( "pagination" );

		HashMap<String, String> map = new HashMap<>();

		//Puts in thr HashMap the name and the correspondent id of the folder
		for ( int i = 0; i < pagination.get( "count" ).asInt( 0 ); i++ ) {
			JsonNode entry = entries.get( i ).get( "entry" );
			map.put( entry.get( "name" ).textValue(), entry.get( "id" ).textValue() );
		}

		return map;
	}

	public String requestTicket() {
		try {
			String credentials = "{\"userId\":\"" + this.alfrescoProperties.getUsername() + "\",\"password\":\"" + this.alfrescoProperties.getPassword() + "\"}";

			JsonNode response = this.restTemplate.postForObject( ticketUrl, credentials, JsonNode.class );
			if ( response == null ) {
				log.error( "Something went Wrong!" );
				throw new RuntimeException( "Failed to request ticket from Alfresco!" );
			}
			return response.get( "entry" ).get( "id" ).asText();
		}
		catch ( ResourceAccessException e5 ) {
			log.info( "It was not possible to send files to Alfresco." );
			log.error( "Error " + e5 );
			throw new RuntimeException( "Connection Time out " );
		}
		catch ( HttpClientErrorException e4 ) {
			log.info( "It was not possible to send files to Alfresco." );
			log.error( "Error " + e4.getStatusCode().value() + " " + e4.getStatusText() + " - " + makeErrorMessage( e4.getResponseBodyAsString() ) );
			throw new RuntimeException( makeErrorMessage( e4.getResponseBodyAsString() ) );
		}
		catch ( IllegalArgumentException e ) {
			log.info( "It was not possible to send files to Alfresco." );
			log.error( "Error: {}", e.getMessage() );
			throw new RuntimeException( format( "Error: %s", e.getMessage() ) );
		}
	}

	public void closeTicket( String ticket ) {
		restTemplate.delete( format( "%s/-me-?alf_ticket=%s", this.ticketUrl, ticket ) );
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
}
