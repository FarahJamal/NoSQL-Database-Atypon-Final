package com.example.nosql.authentication;


public interface Authentication {
    public Object verify(String token, String secretKey);

    public String generate(Object payload);
}

