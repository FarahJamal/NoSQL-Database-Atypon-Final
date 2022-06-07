package com.example.nosql.reacord.helper;

import com.example.nosql.database.RecordProxy;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;

public class Record implements RecordDAO {
    protected List<Attributes> data;
    private String recordID = "", createdAt;

    @Override
    public String toString() {
        return "Record{" +
                "data=" + data.toArray().toString() +
                ", recordID='" + recordID + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public Record(List<Attributes> attributeList) {
        this.data = attributeList;
        this.recordID = RecordProxy.validateUUID(attributeList);
        this.createdAt = RecordProxy.validateTime(attributeList);
    }

    public List<Attributes> getAttributes() {
        return this.data;
    }

    public String getRecordID() {
        return this.recordID;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public org.json.simple.JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        for (Attributes attribute : data) {
            if (attribute.getValue() instanceof List) {
                List<Attributes> nestedAttribute = (List<Attributes>) attribute.getValue();
                JSONArray jsonArray = new JSONArray();
                if (nestedAttribute.size() > 0 && nestedAttribute.get(0) instanceof Attributes) {
                    for (Attributes attribute1 : nestedAttribute) {
                        JSONObject tempJson = new JSONObject();
                        tempJson.put(attribute1.getKey(), attribute1.getValue());
                        jsonArray.put(tempJson);
                    }
                    jsonObject.put(attribute.getKey(), jsonArray);
                } else jsonObject.put(attribute.getKey(), attribute.getValue());
            } else jsonObject.put(attribute.getKey(), attribute.getValue());
        }
        jsonObject.put("createdAt", createdAt);
        jsonObject.put("_id", recordID);
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        return jsonObject;
    }

    @Override
    public boolean isNull() {
        return false;
    }



}