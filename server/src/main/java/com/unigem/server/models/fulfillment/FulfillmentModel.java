package com.unigem.server.models.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FulfillmentModel {
    private String id;
    private String fulfillmentId;
    private String sellerName;
    private String sellerMobile;
    private String company;
    private String otTransName;
    private String otSrcAddress;
    private String otDriverPhoneNo;
    private String otVehicleNo;
    private long otEwayBillNo;
    private String otDate;
    private String inTransName;
    private String inSrcAddress;
    private String inDriverPhoneNo;
    private String inVehicleNo;
    private long inEwayBillNo;
    private String inDate;
    private String hash;
    private List<FulfillmentProducts> fulfillmentProducts;
    private List<FulfillmentQCData> fulfillmentQCData;
}
