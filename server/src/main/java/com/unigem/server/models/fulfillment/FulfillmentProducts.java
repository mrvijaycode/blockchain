package com.unigem.server.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FulfillmentProducts {
    private String product;
    private double hsn;
    private double productQty;
    private double rate;
}
