package com.example.nosql.database;

import com.example.nosql.config.FileIO;
import com.example.nosql.reacord.helper.Attributes;
import com.example.nosql.reacord.helper.Record;
import com.example.nosql.reacord.helper.RecordDAO;
import com.example.nosql.schemas.Schema;
import org.springframework.expression.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SchemasDAO implements DAO {
    private ConcurrentHashMap<String, Record> data;
    //    private LFUCache cache = LFUCache.getInstance();
    private String schemaName;
    private Schema schema;

    private SchemasDAO(String schemaName) throws IOException, ParseException, org.json.simple.parser.ParseException {
        FileIO.createFile(schemaName);
        this.schemaName = schemaName;
        this.schema = Schema.getInstance(schemaName);
        this.data = parseFile();
    }

    public static DAO getInstance(String schemaName) throws IOException, ParseException, org.json.simple.parser.ParseException {
        return new SchemasDAO(schemaName);
    }

    private ConcurrentHashMap parseFile() {
        ConcurrentHashMap<String, Record> data = new ConcurrentHashMap<>();
        try {
            ArrayList<Record> records = FileIO.fileToRecords(schemaName);
            System.out.println(records.size());
            if (records != null) {
                for (RecordDAO record : records) {
                    if (!record.isNull()) {
                        Record record1 = (Record) record;
                        data.put(record1.getRecordID(), record1);
                    }
                }
            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public Object getAll() {
        return null;
    }

    @Override
    public Boolean add(Record record) throws IOException {
        return null;
    }

    @Override
    public Object getByAttribute(Attributes attribute) {
        return null;
    }

    @Override
    public RecordDAO update(Record record) throws IOException {
        return null;
    }

    @Override
    public RecordDAO delete(String recordID) throws IOException {
        return null;
    }
}