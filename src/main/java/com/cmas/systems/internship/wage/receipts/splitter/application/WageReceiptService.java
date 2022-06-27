package com.cmas.systems.internship.wage.receipts.splitter.application;

import com.cmas.systems.internship.wage.receipts.splitter.domain.WageReceiptOwner;
import com.cmas.systems.internship.wage.receipts.splitter.exceptions.PasswordsException;
import com.cmas.systems.internship.wage.receipts.splitter.exceptions.ProtectorException;
import com.cmas.systems.internship.wage.receipts.splitter.exceptions.SplitterException;
import com.cmas.systems.internship.wage.receipts.splitter.exceptions.UploadException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
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
	public ResponseEntity<String> processFile(MultipartFile wageReceiptPdf, MultipartFile pwdFile ) {
		ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
		//ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");

		AtomicBoolean hasErrors = new AtomicBoolean(false);
		AtomicReference<String> message = new AtomicReference<>("{");

		try ( PDDocument wagesReceipts = PDDocument.load( wageReceiptPdf.getBytes() ) ) {

			Map<String, String> passwordsMap = getPasswordsMap( pwdFile );

			//Create a Map of each owner (each owner is created according to the NIF in the passwords file)
			//Key: NIF | Value: owner
			Map<Integer, WageReceiptOwner> personMap = passwordsMap
					.entrySet()
					.stream()
					.map( entry -> new WageReceiptOwner( Integer.parseInt( entry.getKey() ), entry.getValue() ) )
					.collect( Collectors.toMap( WageReceiptOwner::getNif, Function.identity() ) );

			//Splits the pdfs and Checks if it was done any split
			Map<Integer, ByteArrayOutputStream> split = receiptFileSplitter.split( wagesReceipts, personMap.values() );

			//Key:NIF | Value: document  (every owner here has a document)
			split.forEach( ( key, value ) -> {
				String ownerName = personMap.get( key ).getName();
				try {
					//Encrypt the pdf file with the respective person's password
					ByteArrayOutputStream arrayOutputStream = protectFile( value, key, passwordsMap.get( String.valueOf( key ) ) );
					//Upload the files to alfresco
					fileUploader.fileUpload( arrayOutputStream, ownerName );

				}catch (ProtectorException | UploadException e){
					if(hasErrors.get()){
						message.set(message.get() + ",");
					}
					hasErrors.set(true);
					message.set(message.get()+"\""+ ownerName +"\""+":{\"status\":\"" + e.getMessage() + "\"}");
					return;
				}
				message.set(message.get()+"\""+ ownerName +"\""+":{\"status\":\"Upload Successful!\"}");
			} );
		}catch (SplitterException | PasswordsException e){
			hasErrors.set(true);
			message.set("{\"error\":\""+e.getMessage()+"\"");
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

		if(hasErrors.get()){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message.get()+"}");
		}

		return ResponseEntity.status(HttpStatus.OK).body(message.get()+"}");
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
				throw new PasswordsException( "Incorrect password(s)/nif(s) format" );
			}

		}
		catch ( JsonParseException e ) {
			log.error( "Error trying to read JSON file." + e.getMessage() );
			throw new PasswordsException( "Error trying to read JSON file verify if you put the correct file." );
		}
		catch ( MismatchedInputException e ) {
			log.error( "Error {}", e.getMessage());
			throw new PasswordsException( "Error trying to get/read the body file(s). Make sure you didn't forget to put a file(s) on the body or if the file is not empty" );
		}
		return passwordsMap;
	}

	public ByteArrayOutputStream protectFile( ByteArrayOutputStream document, Integer owner, String password ) throws ProtectorException {
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
			throw new ProtectorException( "It was not possible to protect the pdfs" );
		}
	}

}
