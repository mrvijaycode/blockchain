package com.unigem.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericResponse {
    public String code;
    public String msg;
    public boolean suc;

    public GenericResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
