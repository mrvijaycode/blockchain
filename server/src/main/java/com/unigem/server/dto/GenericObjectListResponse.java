package com.unigem.server.dto;

import java.util.List;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericObjectListResponse extends GenericResponse {
    public List<Object> data;

    public static ResponseEntity<GenericObjectListResponse> response(String code, String msg) {
        GenericObjectListResponse response = new GenericObjectListResponse();
        response.code = code;
        response.msg = msg;
        response.data = null;
        response.suc = false;
        return ResponseEntity.status(200).body(response);
    }

    public static ResponseEntity<GenericObjectListResponse> response(List<Object> data) {
        GenericObjectListResponse response = new GenericObjectListResponse();
        response.code = "00";
        response.msg = "";
        response.data = data;
        response.suc = true;
        return ResponseEntity.status(200).body(response);
    }
}
