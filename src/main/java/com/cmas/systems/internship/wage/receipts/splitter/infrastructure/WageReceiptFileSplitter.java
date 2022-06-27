package com.cmas.systems.internship.wage.receipts.splitter.infrastructure;

import com.cmas.systems.internship.wage.receipts.splitter.domain.WageReceiptOwner;
import com.cmas.systems.internship.wage.receipts.splitter.exceptions.SplitterException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Daniel Duarte ( daniel.duarte@cmas-systems.com )
 * @since <next-release>
 */

@Slf4j
@Component
public class WageReceiptFileSplitter {

	public Map<Integer, ByteArrayOutputStream> split( PDDocument document, Collection<WageReceiptOwner> personList ) throws SplitterException {

		Map<Integer, PDDocument> documents = new HashMap<>();

		try {
			//Create the Documents and the property to read the text
			PDFTextStripper pdfStripper = new PDFTextStripper();
			int lastNif = 0;
			PDDocument lastDoc = null;
			PDDocument doc;
			//Make a cycle to Read the document page by page
			for ( int i = 1; i <= document.getNumberOfPages(); i++ ) {
				pdfStripper.setStartPage( i );
				pdfStripper.setEndPage( i );
				String text = pdfStripper.getText( document );

				//Make a cycle per person to give the correspondents page/pages
				for ( WageReceiptOwner person : personList ) {
					if ( text.contains( person.getNif().toString() ) ) {
						PDPageTree listTree = document.getPages();
						PDPage page = listTree.get( i - 1 );
						doc = new PDDocument();

						//If equals to the last niff means he has more than 1 page
						if ( person.getNif() == lastNif ) {
							//Add a page and set the document of the person
							if ( lastDoc != null ) {
								lastDoc.addPage( page );
								documents.put( person.getNif(), lastDoc );
								doc.close();
							}
						}
						else {
							//If is not equal to the last niff he set the document of the person
							person.getPersonNameAndDate( text );
							doc.addPage( page );
							documents.put( person.getNif(), doc );
							lastDoc = doc;
							lastNif = person.getNif();
						}
					}
				}
			}

			if ( documents.values().stream().allMatch( Objects::isNull ) ) {
				//log.error( "It was not performed any split, maybe you selected the wrong pdf file or you are Missing NIFs in the Passwords file" );
				log.error("Any Split Performed.");
				throw new SplitterException( "It was not performed any split, maybe you selected the wrong pdf file or you are Missing NIFs in the Passwords file" );
			}

			return documents.entrySet().stream().collect( Collectors.toMap(
				new Function<Map.Entry<Integer, PDDocument>, Integer>() {

					@Override
					public Integer apply( Map.Entry<Integer, PDDocument> integerPDDocumentEntry ) {
						return integerPDDocumentEntry.getKey();
					}
				},
				new Function<Map.Entry<Integer, PDDocument>, ByteArrayOutputStream>() {

					@SneakyThrows
					@Override
					public ByteArrayOutputStream apply( Map.Entry<Integer, PDDocument> integerPDDocumentEntry ) {
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						integerPDDocumentEntry.getValue().save( byteArrayOutputStream );
						return byteArrayOutputStream;
					}
				} ) );
		}
		catch ( IOException e ) {
			log.error( "It was not possible to split the pdf. Error: " + e.getMessage() );
			throw new SplitterException( "It was not possible to split the pdf" );
		}
	}

}
