package com.unigem.server.dto;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericObjectResponse extends GenericResponse {
    public Object data;

    public static ResponseEntity<GenericObjectResponse> response(String code, String msg) {
        GenericObjectResponse response = new GenericObjectResponse();
        response.code = code;
        response.msg = msg;
        response.data = null;
        response.suc = false;
        return ResponseEntity.status(200).body(response);
    }

    public static ResponseEntity<GenericObjectResponse> response(Object data) {
        GenericObjectResponse response = new GenericObjectResponse();
        response.code = "00";
        response.msg = "";
        response.data = data;
        response.suc = true;
        return ResponseEntity.status(200).body(response);
    }
}
