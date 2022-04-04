/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Activity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import server.api.QuestionController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@EntityScan(basePackages = {"commons", "server"})
public class Main {

    /**
     * Main method for the server side.
     *
     * @param args - parameters of the main method.
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /**
     * Runner method
     *
     * @param questionRepository - specifying the question repository.
     * @return - returns a command line runner.
     */
    @Bean
    CommandLineRunner runner(QuestionController questionRepository) {
        return args -> {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            TypeReference<List<Activity>> typeReference = new TypeReference<List<Activity>>() {
            };
            InputStream inputStream = new FileInputStream(new File("server/src/main/java/server/activities.json"));
            try {
                List<Activity> users = mapper.readValue(inputStream, typeReference);
                questionRepository.save((List<Activity>) users);
                System.out.println("Users Saved!");
            } catch (IOException e) {
                System.out.println("Unable to save users: " + e.getMessage());
            }
        };
    }
}