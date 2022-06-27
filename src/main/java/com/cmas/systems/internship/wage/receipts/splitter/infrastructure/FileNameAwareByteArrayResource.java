package com.cmas.systems.internship.wage.receipts.splitter.infrastructure;

import org.springframework.core.io.ByteArrayResource;

/**
 * @author Nelson Neves ( nelson.neves@cmas-systems.com )
 * @since <next-release>
 */
public class FileNameAwareByteArrayResource extends ByteArrayResource {

	private final String fileName;

	public FileNameAwareByteArrayResource( String fileName, byte[] byteArray ) {
		super( byteArray, null );
		this.fileName = fileName;
	}

	@Override
	public String getFilename() {
		return fileName;
	}

}
