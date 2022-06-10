package com.example.nosql;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class NoSqlDatabaseAtyponFinalApplication {

    public static void main(String[] args) throws IOException {

        String currentPath = new java.io.File(".").getCanonicalPath();
        System.out.println("Current dir:" + currentPath);
        SpringApplication.run(NoSqlDatabaseAtyponFinalApplication.class, args);
    }

}
