package com.andrewlevada.carephoneserver;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Driver;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Exception {
        // Init Firebase
        try {
            FileInputStream serviceAccountKey = new FileInputStream(Config.serviceAccountKeyPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountKey))
                    .setDatabaseUrl(Config.firebaseDatabaseURL)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException exception) {
            throw new Exception("No Firebase Application Credentials found!");
        }

        // Run Spring
        SpringApplication.run(Application.class, args);
    }
}
