package com.CMASProject.SplitReceiptsProject.endpoints;

import com.CMASProject.SplitReceiptsProject.enteties.Person;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequestMapping("/upload")
public class Endpoint {

    @PostMapping
    public ResponseEntity<String> getRequest(@RequestParam("filedata") File file, @RequestParam("data") Map<String,String> data){



       return ResponseEntity.ok().body("Upload Successfully");
    }

}
