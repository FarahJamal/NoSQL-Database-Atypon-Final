package com.example.nosql.database;

import com.example.nosql.cache.LFUCache;
import com.example.nosql.config.FileIO;
import com.example.nosql.config.HashHelper;
import com.example.nosql.reacord.helper.Attributes;
import com.example.nosql.reacord.helper.NullRecord;
import com.example.nosql.reacord.helper.Record;
import com.example.nosql.reacord.helper.RecordDAO;
import com.example.nosql.schemas.Schema;
import org.json.JSONArray;
import org.springframework.expression.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SchemasDAO implements DatabaseDAO {
    private ConcurrentHashMap<String, Record> data;
        private LFUCache cache = LFUCache.getInstance();
    private String schemaName;
    private Schema schema;

    private SchemasDAO(String schemaName) throws IOException, ParseException, org.json.simple.parser.ParseException {
        FileIO.createFile(schemaName);
        this.schemaName = schemaName;
        this.schema = Schema.getInstance(schemaName);
        this.data = parseFile();
    }

    public static DatabaseDAO getInstance(String schemaName) throws IOException, ParseException, org.json.simple.parser.ParseException {
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
        String hashing= HashHelper.hashAll(schemaName);
        Object cacheResult=cache.get(hashing);
        if(cacheResult==null){
            JSONArray jsonArray=new JSONArray();
            for(Object record:data.values()){
                Record recordValue= (Record) record;
                jsonArray.put(recordValue.toJson());
            }
            cache.put(hashing,jsonArray);
            return  jsonArray;

        }
        return cacheResult;
    }

    @Override
    public Boolean add(Record record) throws IOException {
        if (!schema.isValidRecord(record)) {
            System.out.println("false");

            return false;
        }
        String hash = HashHelper.hashRecord(schemaName, record.getRecordID());
        data.put(record.getRecordID(), record);
        cache.put(hash, record);
        cache.remove(HashHelper.hashAll(schemaName));
        FileIO.writeToJson(schemaName, data.values()); // synchronized
        System.out.println("true");
        return true;
    }

    @Override
    public Object getByAttribute(Attributes attribute) {
        String hash = HashHelper.hashRecord(schemaName, attribute.getKey());
        Object cacheResult = cache.get(hash);
        if (cacheResult == null) {
            ArrayList<Record> result = new ArrayList<>();
            for (Record record : data.values()) {
                for (Attributes currentAttribute : record.getAttributes()) {
                    if (attribute.equals(currentAttribute)) {
                        result.add(record);
                    }
                }
            }
            if (result.size() == 0) {
                return NullRecord.getInstance();
            }
            if (result.size() > 0) cache.put(hash, result);
            return result;
        } else return cacheResult;
    }

    @Override
    public RecordDAO update(Record record) throws IOException {
        if (record == null) throw new NullPointerException("record cannot be null");
        Record oldRecord = data.get(record.getRecordID());
        synchronized (this) {
            for (Attributes attribute : record.getAttributes()) {
                for (Attributes oldAttribute : oldRecord.getAttributes()) {
                    if (attribute.getKey().equals(oldAttribute.getKey())) {
                        oldAttribute.setValue(attribute.getValue());
                    }
                }
            }
            add(oldRecord);
            FileIO.writeToJson(schemaName, data.values());

            return oldRecord;
        }
    }

    @Override
    public RecordDAO delete(String recordID) throws IOException {
        if (recordID == null) throw new NullPointerException("ID cannot be null");
        if (data.get(recordID) == null) return NullRecord.getInstance();
        String hash = HashHelper.hashRecord(schemaName, recordID);
        String schemaHash = HashHelper.hashAll(schemaName);
        Record record = data.get(recordID);
        data.remove(recordID);
        cache.remove(hash);
        cache.remove(schemaHash);
        FileIO.writeToJson(schemaName, data.values());
        return record;
    }
}
