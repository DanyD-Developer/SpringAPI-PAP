package com.cmas.systems.internship.wage.receipts.splitter.interfaces.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerResponse {
    private String name;
    private String message;

}
