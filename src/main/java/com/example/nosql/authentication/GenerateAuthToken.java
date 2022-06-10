package com.example.nosql.authentication;

public class GenerateAuthToken implements Authentication {
    private static GenerateAuthToken generateAuthToken;

    public GenerateAuthToken() {
    }

    public static GenerateAuthToken getInstance() {
        if (generateAuthToken == null) generateAuthToken = new GenerateAuthToken();
        return generateAuthToken;
    }


    @Override
    public Object verify(String token, String secretKey) {
        return Encryption.decrypt(token,secretKey);
    }

    @Override
    public String generate(Object payload) {
        return Encryption.encrypt(payload.toString(),"AtyponFinal");
    }
}
