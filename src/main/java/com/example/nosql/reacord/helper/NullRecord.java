package com.example.nosql.reacord.helper;


import org.json.simple.JSONObject;

public class NullRecord implements RecordDAO {
    private static final NullRecord nullRecord = new NullRecord();

    private NullRecord() {}

    public static NullRecord getInstance() {
        return nullRecord;
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject();
    }

    @Override
    public boolean isNull() {
        return true;
    }

    public String getRecordID() {
        return "";
    }

    public Object getAttributes() {
        return null;
    }
}
