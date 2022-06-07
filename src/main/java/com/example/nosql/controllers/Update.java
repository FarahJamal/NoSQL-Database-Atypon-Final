package com.example.nosql.controllers;

import com.example.nosql.database.DatabaseDAO;
import com.example.nosql.database.SchemasDAO;
import com.example.nosql.middleware.AuthenticationLogin;
import com.example.nosql.reacord.helper.Attributes;
import com.example.nosql.reacord.helper.NullRecord;
import com.example.nosql.reacord.helper.Record;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class Update {
    private static Attributes getAttribute(Object key, Map<String, Object> body) {
        if (body.get(key) instanceof List) {
            ArrayList<Attributes> nestedAttribute = new ArrayList<>();
            for (Object data : (ArrayList<?>) body.get(key)) {
                LinkedHashMap<String, Object> smallAtt = (LinkedHashMap<String, Object>) data;
                Map.Entry<String, Object> entry = smallAtt.entrySet().iterator().next();
                Attributes currentAtt = new Attributes(entry.getKey(), entry.getValue());
                nestedAttribute.add(currentAtt);
            }
            Attributes attribute = new Attributes(key.toString(), nestedAttribute);
            return attribute;
        } else {
            Attributes attribute = new Attributes(key.toString(), body.get(key));
            return attribute;
        }
    }

    @PutMapping("/{schema}/{id}")
    public Object update(
            @RequestBody Map<String, Object> body,
            @RequestHeader Map<String, String> headers,
            @PathVariable("schema") String schema,
            @PathVariable("id") String id)
            throws IOException, ParseException {
        String authToken = headers.get("authorization");
        if (!AuthenticationLogin.isLoggedIn(authToken)) {
            return new ResponseEntity("Login first ", HttpStatus.FORBIDDEN);
        }
        DatabaseDAO dao = SchemasDAO.getInstance(schema);
        Attributes idAttribute = new Attributes("_id", id);
        if (dao.getByAttribute(idAttribute) instanceof NullRecord) {
            return new ResponseEntity("user not found", HttpStatus.NOT_FOUND);
        }
        List<Attributes> attributes = new ArrayList<>();
        for (Object key : body.keySet()) {
            Attributes attribute = getAttribute(key, body);
            attributes.add(attribute);
        }
        attributes.add(idAttribute);
        Record record = new Record(attributes);
        return dao.update(record).toJson();
    }
}
