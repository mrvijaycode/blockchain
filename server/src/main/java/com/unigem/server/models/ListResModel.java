package com.unigem.server.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListResModel {
    private int num;
    private int size;
    private long total;
    private List<Object> data;
}
