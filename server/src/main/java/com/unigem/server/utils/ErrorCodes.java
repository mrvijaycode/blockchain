package com.unigem.server.utils;

public enum ErrorCodes {
    UNKNOWN_ERROR("UNKNOWN_ERROR", "UNKNOWN_ERROR"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal Server Error"),
    FULFILLMENT_EXIST("FLAE001", "Fulfillment already exist"),
    FULFILLMENT_DOES_NOT_EXIST("FLAE002", "Fulfillment does not exist"),
    SHORT_OF_FULFILLMENT_INFO("FLAE003", "Fulfilment does not exist");

    private final String code;
    private final String msg;

    ErrorCodes(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
