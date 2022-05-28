package com.example.nosql.config;

public class HashHelper {

    public static String hashAll(String schema) {
        return schema;
    }

    public static String hashRecord(String schema, String id) {
        return schema + "." + id;
    }
}
