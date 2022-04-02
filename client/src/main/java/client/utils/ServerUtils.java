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
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import commons.*;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

    public static String SERVER = "";

    public void getQuotesTheHardWay() throws IOException {
        var url = new URL("http://localhost:8080/api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public Activity getByID(String id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/questions/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Activity>() {
                });
    }

    public void addActivity(Activity activity) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/questions") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }

    public List<Activity> showAll() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/questions/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Activity>>() {
                });
    }

    public Question getQuestion() {
            return ClientBuilder.newClient(new ClientConfig()) //
                            .target(SERVER).path("api/questions/getQuestion") //
                            .request(APPLICATION_JSON) //
                            .accept(APPLICATION_JSON) //
                            .get(new GenericType<Question>() {
                            });
        }

    public void deleteQuestion(String id) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/questions/"+ id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    public int getCurrentLobby() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/game/currentOpenLobby") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Integer.class);
    }

    public User addUser(User user) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    public void updateScore(User user) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user/updateScore") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    public List<User> getUsersInLobby() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user/currentLobby") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<User>>() {});
    }

    public List<User> getUserLobbyNumber(int lobbyNumber) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user/lobby/"+ lobbyNumber) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<User>>() {});
    }

    public void removeUser(String username, int lobbyNumber) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user/removePlayer/"+ username + "/" + lobbyNumber) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    public void startGame() {
        System.out.println("SENDING GET TO questions/start");
         ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/game/start") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get();
    }

    private StompSession session = connect("ws://localhost:8080/websocket");

    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    public StompSession.Subscription registerForMessages(String dest, Consumer<WebsocketMessage> websocketMessageConsumer) {
        StompSession.Subscription subscription = session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return WebsocketMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                websocketMessageConsumer.accept((WebsocketMessage) payload);
            }
        });
        return subscription;
    }

    public void send(String dest, WebsocketMessage websocketMessage) {
        System.out.println("SENDING " + websocketMessage);
        session.send(dest, websocketMessage);
    }

    public List<Score> getScores() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/scores/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Score>>() {
                });
    }

    public List<Score> getTopScores() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/scores/top") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Score>>() {
                });
    }


    public Score addScore(Score score) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/scores/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(score, APPLICATION_JSON), Score.class);
    }

    public boolean isValidUsername(String username) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user/isValidUsername/" + username.toLowerCase(Locale.ROOT)) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(boolean.class);
    }


}


