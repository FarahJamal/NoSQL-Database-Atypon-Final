package com.example.nosql.controllers;

import com.example.nosql.reacord.helper.Attributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Shared {

    public static Attributes getAttribute(Object key, Map<String, Object> body) {
        if (body.get(key) instanceof List) {
            ArrayList<Attributes> nestedAttribute = new ArrayList<>();
            for (Object data : (ArrayList<?>) body.get(key)) {
                LinkedHashMap<String, Object> smallAtt = (LinkedHashMap<String, Object>) data;
                Map.Entry<String, Object> entry = smallAtt.entrySet().iterator().next();
                Attributes currentAtt = new Attributes(entry.getKey(), entry.getValue());
                nestedAttribute.add(currentAtt);
            }
            Attributes attribute = new Attributes(key.toString(), nestedAttribute);
            return attribute;
        } else {
            Attributes attribute = new Attributes(key.toString(), body.get(key));
            return attribute;
        }
    }

}
