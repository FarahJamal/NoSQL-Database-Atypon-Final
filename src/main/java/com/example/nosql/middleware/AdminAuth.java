package com.example.nosql.middleware;

import com.example.nosql.cache.LFUCache;
import com.example.nosql.config.HashHelper;
import com.example.nosql.database.SchemasDAO;
import com.example.nosql.database.SchemasDAO;

import com.example.nosql.reacord.helper.Attributes;
import com.example.nosql.reacord.helper.Record;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class AdminAuth {
    public static boolean checkAuth(String decrypted) throws IOException, ParseException {
        SchemasDAO schemaDAO = (SchemasDAO) SchemasDAO.getInstance("users");
        Attributes idAttribute = new Attributes("_id", decrypted);
        String hash = HashHelper.hashRecord("users", idAttribute.getKey());
        LFUCache.getInstance().remove(hash);
        ArrayList<Record> user = (ArrayList) schemaDAO.getByAttribute(idAttribute);
        Record record = user.get(0);
        for (Attributes attribute : record.getAttributes()) {
            if (attribute.getKey().equals("role") && attribute.getValue().equals("admin")) {
                return true;
            }
        }
        return false;
    }
}
