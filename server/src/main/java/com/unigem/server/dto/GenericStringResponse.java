package com.unigem.server.dto;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericStringResponse extends GenericResponse {
    private String data;

    public static ResponseEntity<GenericStringResponse> response() {
        GenericStringResponse response = new GenericStringResponse();
        response.code = "00";
        response.msg = "";
        response.data = null;
        response.suc = true;
        return ResponseEntity.status(200).body(response);
    }

    public static ResponseEntity<GenericStringResponse> response(String code, String msg) {
        GenericStringResponse response = new GenericStringResponse();
        response.code = code;
        response.msg = msg;
        response.data = null;
        response.suc = false;
        return ResponseEntity.status(200).body(response);
    }

    public static ResponseEntity<GenericStringResponse> response(String data) {
        GenericStringResponse response = new GenericStringResponse();
        response.code = "00";
        response.msg = "";
        response.data = data;
        response.suc = true;
        return ResponseEntity.status(200).body(response);
    }
}
