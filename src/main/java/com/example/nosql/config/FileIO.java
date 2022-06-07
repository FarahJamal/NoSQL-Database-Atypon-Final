package com.example.nosql.config;


import com.example.nosql.reacord.helper.Attributes;
import com.example.nosql.reacord.helper.Record;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileIO {
    public static JSONObject toJsonObject(Collection<Record> recordList) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Object record : recordList) {
            Record tempRecord = (Record) record;
            jsonArray.add(tempRecord.toJson());
        }
        jsonObject.put("data", jsonArray);
        return jsonObject;
    }

    public static ArrayList<Record> fileToRecords(String fileName)
            throws IOException, ParseException {
        FileReader fileReader =
                new FileReader(ResourcesPath.getDevelopmentDataResource() + fileName + ".json");

        JSONParser jsonParser = new JSONParser();
        JSONObject object = (JSONObject) jsonParser.parse(fileReader);
        ArrayList<Record> records = new ArrayList<>();
        for (Object jsonObject : (List<Object>) object.get("data")) {
            Record record = FileIO.parseRecord((JSONObject) jsonObject);
            System.out.println(record.getRecordID());
            records.add(record);
        }
        return records;
    }

    private static Record parseRecord(JSONObject jsonObject) {
        ArrayList<Attributes> attributes = new ArrayList<>();
        for (Object object : jsonObject.keySet()) {
            Attributes attribute =
                    new Attributes(object.toString(), jsonObject.get(object.toString()));
            attributes.add(attribute);
        }
        return new Record(attributes);
    }

    public static synchronized void writeToJson(String fileName, Collection<Record> recordList)
            throws IOException {
        JSONObject jsonObject = FileIO.toJsonObject(recordList);
        write(fileName, jsonObject);
    }

    private static void write(String fileName, JSONObject jsonObject) throws IOException {
        FileWriter fileWriter =
                new FileWriter(ResourcesPath.getDevelopmentDataResource() + fileName + ".json");
        try {
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFile(String fileName) throws IOException {
        System.out.println(ResourcesPath.getDevelopmentDataResource() + fileName + ".json");
        File file = new File(ResourcesPath.getDevelopmentDataResource() + fileName + ".json");
        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());
        } else {
            System.out.println("File already exists.");
        }
    }
}
