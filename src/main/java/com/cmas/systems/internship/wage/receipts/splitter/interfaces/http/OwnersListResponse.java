package com.cmas.systems.internship.wage.receipts.splitter.interfaces.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnersListResponse {
    private List<OwnerResponse> owners;
}
