package com.cmas.systems.internship.wage.receipts.splitter.domain;

import lombok.Data;

@Data
public class WageReceiptOwner {

	private String name;

	private final Integer nif;

	private final String password;

	private String processDate;

	public WageReceiptOwner( Integer nif, String password ) {
		this.nif = nif;
		this.password = password;
	}

	public void getPersonNameAndDate( String documentText ) {
		String[] lines = documentText.split( "\\R" );

		for ( int i = 0; i < lines.length; i++ ) {
			if ( lines[i].contains( "Categoria:" ) ) {
				//Gets the date like this(24.05.2016) and turns into ->(Mai_2016)
				String[] date = lines[i - 8].replace( ".", "-" ).split( "-" );
				switch ( date[1] ) {
					case "01":
						date[1] = "Jan";
						break;
					case "02":
						date[1] = "Fev";
						break;
					case "03":
						date[1] = "Mar";
						break;
					case "04":
						date[1] = "Abr";
						break;
					case "05":
						date[1] = "Mai";
						break;
					case "06":
						date[1] = "Jun";
						break;
					case "07":
						date[1] = "Jul";
						break;
					case "08":
						date[1] = "Ago";
						break;
					case "09":
						date[1] = "Set";
						break;
					case "10":
						date[1] = "Out";
						break;
					case "11":
						date[1] = "Nov";
						break;
					case "12":
						date[1] = "Dez";
						break;
					default:
						break;
				}

				this.processDate = date[1] + "_" + date[2];

                /*Explanation of 'Categoria:','lines[i+3]' & 'lines[i-8]':
                the file have this pattern:
                .
                .
                'Date' <- (i - 8)
                .
                .
                .
                Categoria: <- (i)
                .
                .
                'Persons name' <- (i + 3)
                .
                .
                */
			}
			else if ( lines[i].contains( "IBAN:" ) ) {
				String name = lines[i - 3];
				String firstName = name.split( " " )[0];
				String lastName = name.split( " " )[name.split( " " ).length - 1];

				this.name = firstName + " " + lastName;
			}
		}
	}

}
