package com.unigem.server.services;

import static com.unigem.server.utils.ObjectMapperUtil.mapObjectToFulfillment;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.unigem.server.utils.ErrorCodes;
import com.unigem.server.dto.GenericObjectResponse;
import com.unigem.server.dto.GenericStringResponse;
import com.unigem.server.models.ListResModel;
import com.unigem.server.models.fulfillment.FulfillmentListDataReqModel;
import com.unigem.server.models.fulfillment.FulfillmentModel;
import com.unigem.server.utils.ContractUtils;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FulfillmentService {


    public JSONArray getFulfillmentList(FulfillmentModel fulfillmentModel) {
        try {
            JSONObject query = new JSONObject();
            JSONObject selector = new JSONObject();
            selector.put("docType", "fulfillment");
            if (fulfillmentModel.getId() != null)
                selector.put("Id", fulfillmentModel.getId());
            query.put("selector", selector);

            Contract contract = ContractUtils.getContract();
            byte[] result = contract.evaluateTransaction("QueryResults", query.toString());
            return new JSONArray(new String(result));
        } catch (ContractException | IOException contractException) {
            System.out.println(contractException);
            return new JSONArray();
        }
    }

    public ResponseEntity<GenericStringResponse> register(FulfillmentModel fulfillmentModel) {
        try {
            if (fulfillmentModel == null) {
                return GenericStringResponse.response(ErrorCodes.SHORT_OF_FULFILLMENT_INFO.getCode(),
                        ErrorCodes.SHORT_OF_FULFILLMENT_INFO.getMsg());
            }
            fulfillmentModel.setId(UUID.randomUUID().toString());
            Contract contract = ContractUtils.getContract();
            contract.submitTransaction("RegisterFulfillment", new Gson().toJson(fulfillmentModel));
            return GenericStringResponse.response(fulfillmentModel.getId());
        } catch (ContractException | IOException | TimeoutException | InterruptedException contractException) {
            if (contractException.getMessage().contains("Fulfillment exists")) {
                return GenericStringResponse.response(ErrorCodes.FULFILLMENT_EXIST.getCode(),
                        ErrorCodes.FULFILLMENT_EXIST.getMsg());
            }
            if (contractException.getMessage().contains("Fulfillment does not exist")) {
                return GenericStringResponse.response(ErrorCodes.FULFILLMENT_DOES_NOT_EXIST.getCode(),
                        ErrorCodes.FULFILLMENT_DOES_NOT_EXIST.getMsg());
            }
            System.out.println(contractException);
            return GenericStringResponse.response(ErrorCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ErrorCodes.INTERNAL_SERVER_ERROR.getMsg());
        }
    }

    public ResponseEntity<GenericObjectResponse> list(FulfillmentListDataReqModel fulfillmentListDataReqModel) {
        if (fulfillmentListDataReqModel == null || fulfillmentListDataReqModel.getNum() < 1 || fulfillmentListDataReqModel.getSize() < 1
                || fulfillmentListDataReqModel.getSize() > 200) {
            return GenericObjectResponse.response(ErrorCodes.SHORT_OF_FULFILLMENT_INFO.getCode(),
                    ErrorCodes.SHORT_OF_FULFILLMENT_INFO.getMsg());
        }
        JSONArray fulfillmentArray = getFulfillmentList(mapObjectToFulfillment(fulfillmentListDataReqModel.getData()));
        int pageStartIndex = (fulfillmentListDataReqModel.getNum() - 1) * fulfillmentListDataReqModel.getSize();
        List<Object> fulfillmentList = new ArrayList<>();
        if (pageStartIndex <= fulfillmentArray.length()) {
            int pageEndIndex = Math.min(fulfillmentArray.length(), pageStartIndex + fulfillmentListDataReqModel.getSize());
            for (int i = pageStartIndex; i < pageEndIndex; i++) {
                JSONObject temp = new JSONObject(fulfillmentArray.get(i).toString()).getJSONObject("Record");
                fulfillmentList.add(new Gson().fromJson(temp.toString(), FulfillmentModel.class));
            }
        }
        return GenericObjectResponse.response(new ListResModel(fulfillmentListDataReqModel.getNum(),
                fulfillmentListDataReqModel.getSize(), fulfillmentArray.length(), fulfillmentList));
    }


    public ResponseEntity<GenericStringResponse> update(FulfillmentModel fulfillmentModel) {
        try {
            if (fulfillmentModel == null || fulfillmentModel.getId() == null) {
                return GenericStringResponse.response(ErrorCodes.SHORT_OF_FULFILLMENT_INFO.getCode(),
                        ErrorCodes.SHORT_OF_FULFILLMENT_INFO.getMsg());
            }
            Contract contract = ContractUtils.getContract();
            System.out.println(new Gson().toJson(fulfillmentModel));
            contract.submitTransaction("UpdateFulfillment", new Gson().toJson(fulfillmentModel));
            return GenericStringResponse.response();
        } catch (ContractException | IOException | TimeoutException | InterruptedException contractException) {
            if (contractException.getMessage().contains("Fulfillment does not exist")) {
                return GenericStringResponse.response(ErrorCodes.FULFILLMENT_DOES_NOT_EXIST.getCode(),
                        ErrorCodes.FULFILLMENT_DOES_NOT_EXIST.getMsg());
            }
            System.out.println(contractException);
            return GenericStringResponse.response(ErrorCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ErrorCodes.INTERNAL_SERVER_ERROR.getMsg());
        }
    }

    public ResponseEntity<GenericStringResponse> delete(FulfillmentModel fulfillmentModel) {
        try {
            if (fulfillmentModel == null || fulfillmentModel.getId() == null) {
                return GenericStringResponse.response(ErrorCodes.SHORT_OF_FULFILLMENT_INFO.getCode(),
                        ErrorCodes.SHORT_OF_FULFILLMENT_INFO.getMsg());
            }
            Contract contract = ContractUtils.getContract();
            contract.submitTransaction("DeleteFulfillment", fulfillmentModel.getId());
            return GenericStringResponse.response(null);
        } catch (ContractException | TimeoutException | InterruptedException | IOException contractException) {
            if (contractException.getMessage().contains("Fulfillment does not exist")) {
                return GenericStringResponse.response(ErrorCodes.FULFILLMENT_DOES_NOT_EXIST.getCode(),
                        ErrorCodes.FULFILLMENT_DOES_NOT_EXIST.getMsg());
            }
            System.out.println(contractException);
            return GenericStringResponse.response(ErrorCodes.INTERNAL_SERVER_ERROR.getCode(),
                    ErrorCodes.INTERNAL_SERVER_ERROR.getMsg());
        }
    }

    public FulfillmentModel getById(String id) {
        try {
            byte[] result;
            Contract contract = ContractUtils.getContract();
            result = contract.evaluateTransaction("GetFulfillment", id);
            JSONObject fulfillment = new JSONObject(new String(result));
            if (fulfillment.getString("docType").equals("fulfillment")) {
                return new Gson().fromJson(fulfillment.toString(), FulfillmentModel.class);
            } else {
                return new FulfillmentModel();
            }
        } catch (ContractException | IOException contractException) {
            System.out.println(contractException);
            return new FulfillmentModel();
        }
    }
}
