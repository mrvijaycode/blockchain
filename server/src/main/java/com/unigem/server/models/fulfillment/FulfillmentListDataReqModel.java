package com.unigem.server.models.fulfillment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FulfillmentListDataReqModel {
    private int num;
    private int size;
    private FulfillmentModel data;
}
