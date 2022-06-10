package com.example.nosql.database;
import com.example.nosql.reacord.helper.Attributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RecordProxy {
    public static String validateUUID(List<Attributes> attributeList) {
        for (Attributes attribute : attributeList) {
            if (attribute.getKey().equals("_id")) return (String) attribute.getValue();
        }
        return UUID.randomUUID().toString();
    }

    public static String validateTime(List<Attributes> attributeList) {
        for (Attributes attribute : attributeList) {
            if (attribute.getKey().equals("createdAt")) return (String) attribute.getValue();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
}
