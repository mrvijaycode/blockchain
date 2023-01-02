package com.unigem.server.controller;

import static com.unigem.server.utils.ObjectMapperUtil.mapToFulfillmentListDataReqModel;
import static com.unigem.server.utils.ObjectMapperUtil.mapDataToFulfillment;


import com.unigem.server.dto.GenericObjectResponse;
import com.unigem.server.dto.GenericStringResponse;
import com.unigem.server.models.fulfillment.FulfillmentModel;
import com.unigem.server.models.ReqListModel;
import com.unigem.server.models.ReqModel;
import com.unigem.server.services.FulfillmentService;
import com.unigem.server.utils.ErrorCodes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@EnableSwagger2
//@RequestMapping("trace/fulfillment/")
@RequestMapping("/")
public class FulfillmentController {

    @Autowired
    FulfillmentService fulfillmentService;

    @ResponseBody
    @GetMapping(value = "/test")
    public String test() {
        return "Welcome to Uni-gem Server";
    }

    @PostMapping(value = "/register")
    public ResponseEntity<GenericStringResponse> register(@RequestBody ReqModel reqModel) {
        return fulfillmentService.register(mapDataToFulfillment(reqModel));
    }

    @PostMapping(value = "/list")
    public ResponseEntity<GenericObjectResponse> list(@RequestBody ReqListModel reqListModel) {
        return fulfillmentService.list(mapToFulfillmentListDataReqModel(reqListModel));
    }

    @PostMapping(value = "/modify")
    public ResponseEntity<GenericStringResponse> update(@RequestBody ReqModel reqModel) {
        return fulfillmentService.update(mapDataToFulfillment(reqModel));
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<GenericStringResponse> delete(@RequestBody ReqModel reqModel) {
        return fulfillmentService.delete(mapDataToFulfillment(reqModel));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<GenericObjectResponse> getById(@PathVariable("id") String id) {
        if (id == null) {
            return GenericObjectResponse.response(ErrorCodes.SHORT_OF_FULFILLMENT_INFO.getCode(),
                    ErrorCodes.SHORT_OF_FULFILLMENT_INFO.getMsg());
        }
        FulfillmentModel fulfillment = fulfillmentService.getById(id);
        if (fulfillment.getId() != null) {
            return GenericObjectResponse.response(fulfillment);
        } else {
            return GenericObjectResponse.response(ErrorCodes.FULFILLMENT_DOES_NOT_EXIST.getCode(),
                    ErrorCodes.FULFILLMENT_DOES_NOT_EXIST.getMsg());
        }
    }
}
