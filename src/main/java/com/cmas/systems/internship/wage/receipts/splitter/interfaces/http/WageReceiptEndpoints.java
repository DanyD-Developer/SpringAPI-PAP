package com.cmas.systems.internship.wage.receipts.splitter.interfaces.http;

import com.cmas.systems.internship.wage.receipts.splitter.application.WageReceiptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping( "/wage-receipts" )
public class WageReceiptEndpoints {

	private final WageReceiptService wageReceiptService;

	@PostMapping( "/upload" )
	public ResponseEntity<String> getRequest(@RequestParam( "wageReceiptPdf" ) MultipartFile wageReceiptPdf, @RequestParam( "pwdFile" ) MultipartFile pwdFile ) throws JsonProcessingException {
		return ResponseEntity.ok(new ObjectMapper().writeValueAsString(wageReceiptService.processFile( wageReceiptPdf, pwdFile )));
	}

}
