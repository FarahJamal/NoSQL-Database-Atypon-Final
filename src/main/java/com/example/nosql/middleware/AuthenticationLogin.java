package com.example.nosql.middleware;

import com.example.nosql.authentication.GenerateAuthToken;
import com.example.nosql.database.SchemasDAO;
import com.example.nosql.database.SchemasDAO;
import com.example.nosql.reacord.helper.RecordDAO;

import com.example.nosql.reacord.helper.Attributes;
import com.example.nosql.reacord.helper.NullRecord;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public class AuthenticationLogin {
    private static boolean checkTokenUser(String userId) throws IOException, ParseException {
        SchemasDAO dao = (SchemasDAO) SchemasDAO.getInstance("users");
        Attributes attribute = new Attributes("_id", userId);
        Object result = dao.getByAttribute(attribute);
        if (result instanceof NullRecord) {
            return false;
        }
        List<RecordDAO> records = (List<RecordDAO>) result;
        return records.size() > 0;
    }

    public static boolean isLoggedIn(String token) throws IOException, ParseException {
        if (token == null) return false;
        if (token.length() <= 0) return false;
        GenerateAuthToken generateAuthToken = GenerateAuthToken.getInstance();
        String decrypted = (String) generateAuthToken.verify(token, "AtyponFinal");
        if (decrypted == null) return false;
        return checkTokenUser(decrypted);
    }
}
