package com.example.nosql.reacord.helper;


import org.json.simple.JSONObject;

public interface RecordDAO {

    public JSONObject toJson();

    public boolean isNull();
}
