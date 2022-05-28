package com.example.nosql.controllers;

import com.example.nosql.authentication.GenerateAuthToken;
import com.example.nosql.config.HashHelper;
import com.example.nosql.database.SchemasDAO;
import com.example.nosql.reacord.helper.Attributes;
import com.example.nosql.reacord.helper.NullRecord;
import com.example.nosql.reacord.helper.Record;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class Write {

    @PostMapping("/login")
    public Object login(@RequestBody Map<String, Object> body) throws IOException, ParseException, org.json.simple.parser.ParseException {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        Attributes attribute = new Attributes("username", username);
        String hash = HashHelper.hashRecord("users", attribute.getKey());
//        LFUCache.getInstance().remove(hash);
        SchemasDAO schemaDAO = (SchemasDAO) SchemasDAO.getInstance("users");
        Object user = schemaDAO.getByAttribute(attribute);
        if (user instanceof NullRecord)
            return new ResponseEntity("invalid credentials ", HttpStatus.FORBIDDEN);
        ArrayList<Record> recordArrayList = (ArrayList<Record>) user;
        if (recordArrayList.size() > 0 ) {
            GenerateAuthToken tokenAuth = GenerateAuthToken.getInstance();
            Record record = recordArrayList.get(0);
            return tokenAuth.generate(record.getRecordID());
        } else {
            return new ResponseEntity("invalid credentials ", HttpStatus.FORBIDDEN);
        }
    }
}


