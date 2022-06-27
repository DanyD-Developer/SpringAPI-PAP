package com.cmas.systems.internship.wage.receipts.splitter.application;

import com.cmas.systems.internship.wage.receipts.splitter.domain.WageReceiptOwner;
import com.cmas.systems.internship.wage.receipts.splitter.infrastructure.WageReceiptFileSplitter;
import com.cmas.systems.internship.wage.receipts.splitter.infrastructure.WageReceiptFileUploader;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Nelson Neves ( nelson.neves@cmas-systems.com )
 * @since <next-release>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class WageReceiptService {

	private final WageReceiptFileUploader fileUploader;

	private final WageReceiptFileSplitter receiptFileSplitter;

	private final ObjectMapper objectMapper;

	@SneakyThrows
	public void processFile( MultipartFile wageReceiptPdf, MultipartFile pwdFile ) {
		Map<String, String> passwordsMap = getPasswordsMap( pwdFile );

		//Create a list of each person (each person is created according to the NIF in the passwords file)
		Map<Integer, WageReceiptOwner> personMap = passwordsMap
			.entrySet()
			.stream()
			.map( entry -> new WageReceiptOwner( Integer.parseInt( entry.getKey() ), entry.getValue() ) )
			.collect( Collectors.toMap( WageReceiptOwner::getNif, Function.identity() ) );

		try ( PDDocument wagesReceipts = PDDocument.load( wageReceiptPdf.getBytes() ) ) {

			//Splits the pdfs and Checks if it was done any split
			Map<Integer, ByteArrayOutputStream> split = receiptFileSplitter.split( wagesReceipts, personMap.values() );

			split.forEach( ( key, value ) -> {

				//Encrypt the pdf file with the respective person's password
				ByteArrayOutputStream arrayOutputStream = protectFile( value, key, passwordsMap.get( String.valueOf( key ) ) );
				//Upload the files to alfresco
				fileUploader.fileUpload( arrayOutputStream, personMap.get( key ).getName() );

			} );

		}
		catch ( NumberFormatException e ) {
			log.error( "Failed to read NIF(s)" );
			throw new RuntimeException( "Failed to read NIF(s)" );
		}
		catch ( IOException e ) {
			log.error( "File isn't a valid pdf file" );
			throw new RuntimeException( "File isn't a valid pdf file" );
		}
		catch ( Exception e ) {
			log.error( "Failed Loading file {}", wageReceiptPdf.getOriginalFilename(), e );
			throw new RuntimeException( "Failed Loading file" );
		}
	}

	private Map<String, String> getPasswordsMap( MultipartFile pwdFile ) throws IOException {
		Map<String, String> passwordsMap;

		try {
			//Read the Json file of the passwords and save inside a Map
			passwordsMap = objectMapper.readValue( new String( pwdFile.getBytes() ), Map.class );

			boolean anyMatch = passwordsMap.entrySet().stream().anyMatch( entry ->
				ObjectUtils.isEmpty( entry.getValue() ) ||
					ObjectUtils.isEmpty( entry.getKey() ) ||
					entry.getKey().length() != 9 );

			if ( anyMatch ) {
				throw new RuntimeException( "Incorrect password(s)/nif(s) format" );
			}

		}
		catch ( JsonParseException e ) {
			log.error( "Error trying to read JSON file verify if you put the correct file " + e.getMessage() );
			throw new RuntimeException( "Error trying to read JSON file" );
		}
		catch ( MismatchedInputException e ) {
			log.error( "Error {}", e.getMessage() + " Make sure you didn't forget to put a file(s) on the body or if he file is not empty" );
			throw new RuntimeException( "Error trying to get/read the body file(s)" );
		}
		return passwordsMap;
	}

	public ByteArrayOutputStream protectFile( ByteArrayOutputStream document, Integer owner, String password ) {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			PDDocument documentToEncrypt = PDDocument.load( document.toByteArray() );
			//Encrypt the pdf document
			AccessPermission accessPermission = new AccessPermission();
			StandardProtectionPolicy spp = new StandardProtectionPolicy( password, password, accessPermission );
			spp.setEncryptionKeyLength( 256 );
			spp.setPermissions( accessPermission );
			documentToEncrypt.protect( spp );
			documentToEncrypt.save( byteArrayOutputStream );
			documentToEncrypt.close();

			return byteArrayOutputStream;
		}
		catch ( IOException e ) {
			log.error( "It was not possible to protect the pdfs of " + owner + ". Error: " + e.getMessage() );
			throw new RuntimeException( "It was not possible to protect the pdfs" );
		}
	}

}
