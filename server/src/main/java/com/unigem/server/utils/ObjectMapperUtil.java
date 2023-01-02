package com.unigem.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unigem.server.models.ReqListModel;
import com.unigem.server.models.ReqModel;
import com.unigem.server.models.fulfillment.FulfillmentModel;
import com.unigem.server.models.fulfillment.FulfillmentListDataReqModel;


final public class ObjectMapperUtil {

    // Fulfillment
    public static FulfillmentModel mapDataToFulfillment(ReqModel data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(data.getData(), FulfillmentModel.class);
        } catch (NullPointerException nullPointerException) {
            return new FulfillmentModel();
        }
    }

    public static FulfillmentModel mapObjectToFulfillment(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(data, FulfillmentModel.class);
        } catch (NullPointerException nullPointerException) {
            return new FulfillmentModel();
        }
    }

    public static FulfillmentListDataReqModel mapToFulfillmentListDataReqModel(ReqListModel data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(data.getData(), FulfillmentListDataReqModel.class);
        } catch (NullPointerException nullPointerException) {
            return new FulfillmentListDataReqModel();
        }
    }
}
