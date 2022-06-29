package com.cmas.systems.internship.wage.receipts.splitter.interfaces.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nelson Neves ( nelson.neves@cmas-systems.com )
 * @since <next-release>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WageReceiptOwnerResult {

	private String name;

	private String message;

}
