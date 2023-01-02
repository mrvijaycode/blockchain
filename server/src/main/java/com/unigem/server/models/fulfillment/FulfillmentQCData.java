package com.unigem.server.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FulfillmentQCData {
    private String customerName;
    private String customerMobile;
    private double quantity;
}
