package com.example.studylessbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@SpringBootApplication
public class StudyLessBotApplication {

    public static void main(String[] args) {


        configureJUL();
        SpringApplication.run(StudyLessBotApplication.class, args);
    }

    private static void configureJUL() {
        try {
            LogManager.getLogManager().readConfiguration(
                    StudyLessBotApplication.class.getClassLoader().getResourceAsStream("logging.properties")
            );
        } catch (IOException e) {
        }
    }

}
