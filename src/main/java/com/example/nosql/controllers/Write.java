package com.example.nosql.controllers;

import com.example.nosql.authentication.Encryption;
import com.example.nosql.authentication.GenerateAuthToken;
import com.example.nosql.cache.LFUCache;
import com.example.nosql.config.HashHelper;
import com.example.nosql.database.DatabaseDAO;
import com.example.nosql.database.SchemasDAO;
import com.example.nosql.middleware.AdminAuth;
import com.example.nosql.middleware.AuthenticationLogin;
import com.example.nosql.reacord.helper.Attributes;
import com.example.nosql.reacord.helper.NullRecord;
import com.example.nosql.reacord.helper.Record;
import com.example.nosql.schemas.SchemaBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
public class Write {
Shared shared=new Shared();
    @PostMapping("/login")
    public Object login(@RequestBody Map<String, Object> body) throws IOException, ParseException {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        Attributes attribute = new Attributes("username", username);
        Attributes attribute2 = new Attributes("password", password);

        String usernameHash = HashHelper.hashRecord("users", attribute.getKey());
        String passwordHash = HashHelper.hashRecord("users", attribute2.getKey());


        LFUCache.getInstance().remove(usernameHash);
        LFUCache.getInstance().remove(passwordHash);

        SchemasDAO schemaDAO = (SchemasDAO) SchemasDAO.getInstance("users");
        Object usernameValue = schemaDAO.getByAttribute(attribute);
        Object passwordValue = schemaDAO.getByAttribute(attribute2);


        if (usernameValue instanceof NullRecord || passwordValue instanceof  NullRecord)
            return new ResponseEntity("invalid credentials ", HttpStatus.FORBIDDEN);

        ArrayList<Record> usernameRecordArrayList = (ArrayList<Record>) usernameValue;
        ArrayList<Record> passwordRecordArrayList = (ArrayList<Record>) passwordValue;

        if (usernameRecordArrayList.size() > 0 && passwordRecordArrayList.size()>0) {
            GenerateAuthToken tokenAuth = GenerateAuthToken.getInstance();
            Record record = usernameRecordArrayList.get(0);
            return tokenAuth.generate(record.getRecordID());
        } else {
            return new ResponseEntity("invalid credentials ", HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping(
            value = "/{schema}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object add(
            @PathVariable("schema") String schema,
            @RequestBody Map<String, Object> body,
            @RequestHeader Map<String, String> headers)
            throws IOException, ParseException {
        List<Attributes> attributes = new ArrayList<>();
        String authToken = headers.get("authorization");
        System.out.println(headers);
        if (!AuthenticationLogin.isLoggedIn(authToken)) {
            return new ResponseEntity("Login first ", HttpStatus.FORBIDDEN);
        }
        for (Object key : body.keySet()) {
            Attributes attribute =shared.getAttribute(key, body);
            attributes.add(attribute);
        }
        DatabaseDAO dao = SchemasDAO.getInstance(schema);
        Record record =new Record(attributes);
        if (dao.add(record)) {
            return record.toJson().toJSONString();
        } else {
            return new ResponseEntity("Bad request",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(
            value="/schema/{schema}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object createSchema(
            @RequestBody String body,
            @PathVariable("schema") String schema,
            @RequestHeader Map<String, String> headers)
            throws IOException, ParseException {
        String authToken = headers.get("authorization");

        if (authToken.length() <= 0) return new ResponseEntity("forbidden",HttpStatus.FORBIDDEN);
        String decrypted = Encryption.decrypt(authToken, "AtyponFinal");
        if (decrypted == null) return new ResponseEntity("forbidden",HttpStatus.FORBIDDEN);
        if (!AdminAuth.checkAuth(decrypted)) return new ResponseEntity("forbidden",HttpStatus.FORBIDDEN);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(body);
        if (SchemaBuilder.createSchema(schema, json)) {
            return json;
        } else {
            return new ResponseEntity("Bad request",HttpStatus.BAD_REQUEST);
        }
    }
}


