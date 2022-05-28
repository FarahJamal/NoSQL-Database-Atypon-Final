package com.example.nosql.database;

import com.example.nosql.reacord.helper.Attributes;
import com.example.nosql.reacord.helper.Record;
import com.example.nosql.reacord.helper.RecordDAO;

import java.io.IOException;

public interface DAO {
    public Object getAll();

    public Boolean add(Record record) throws IOException;

    public Object getByAttribute(Attributes attribute);

    public RecordDAO update(Record record) throws IOException;

    public RecordDAO delete(String recordID) throws IOException;
}
