package com.cmas.systems.internship.wage.receipts.splitter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties( prefix = "wage-receipt-file-splitter" )
public class WageReceiptFileSplitterProperties {

	private AlfrescoProperties alfrescoProperties;

	private String tempFolder;

	private String wageReceiptsNodeid;

	@Data
	public static class AlfrescoProperties {

		private String url;

		private String username;

		private String password;
	}
}
