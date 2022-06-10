package com.example.nosql.schemas;


import com.example.nosql.config.ResourcesPath;
import com.example.nosql.reacord.helper.Attributes;
import com.example.nosql.reacord.helper.Record;
import org.json.simple.JSONObject;
import org.springframework.expression.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.simple.parser.JSONParser;

public class Schema {
    private JSONObject schema;

    private Schema(String schemaName) throws IOException, ParseException, org.json.simple.parser.ParseException {
        this.schema = parseJson(schemaName);
    }

    public static Schema getInstance(String schemaName) throws IOException, ParseException, org.json.simple.parser.ParseException {
        return new Schema(schemaName);
    }

    private JSONObject parseJson(String schemaName) throws IOException, ParseException, org.json.simple.parser.ParseException {
        FileReader fileReader =
                new FileReader(ResourcesPath.getProductionSchemaResource() + schemaName + ".json");
        JSONParser jsonParser = new JSONParser();
        Object json = jsonParser.parse(fileReader);
        return (JSONObject) json;
    }

    public Boolean isValidRecord(Record record) {
        ArrayList<Attributes> attributes = (ArrayList<Attributes>) record.getAttributes();
        for (Attributes attribute : attributes) {
            if (!schema.containsKey(attribute.getKey())) {
                return false;
            } else {
                String type = (String) schema.get(attribute.getKey());
                if (Objects.equals(type, "String")) {
                    if (!(attribute.getValue() instanceof String)) return false;
                }
                if (Objects.equals(type, "Integer")) {
                    if (!(attribute.getValue() instanceof Integer)) return false;
                }
                if (Objects.equals(type, "List")) {
                    if (!(attribute.getValue() instanceof List)) return false;
                }
                if (Objects.equals(type, "Boolean")) {
                    System.out.println(attribute.getValue());
                    if (!(attribute.getValue() instanceof Boolean)) return false;
                }
                if (Objects.equals(type, "Double")) {
                    if (!(attribute.getValue() instanceof Double)) return false;
                }
                if (Objects.equals(type, "Character")) {
                    if (!(attribute.getValue() instanceof Character)) return false;
                }
            }
        }
        return true;
    }
}
