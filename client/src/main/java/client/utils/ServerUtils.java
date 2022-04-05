package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.lang.reflect.Type;

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
    public static StompSession session;

    /**
     * Method that is one part of the endpoint for adding the activity to the server database.
     *
     * @param activity - activity to be added to the server database.
     */
    public void addActivity(Activity activity) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/questions") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }


    /**
     * Method that is one part of the endpoint for showing the list of all of activites
     * from the server database.
     *
     * @return - returns the list of all of activites from the server database.
     */
    public List<Activity> showAll() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/questions/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Activity>>() {
                });
    }

    /**
     * Method that is one part of the endpoint for getting a random activity from the server
     * database.
     *
     * @return - returns one question from the server database.
     */
    public Question getQuestion() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/questions/getQuestion") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Question>() {
                });
    }

    /**
     * Method that is one part of the endpoint for deleting question with specific id in the server
     * database.
     *
     * @param id - id of the question to be deleted in the server database.
     */
    public void deleteQuestion(String id) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/questions/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * Method that is one part of the endpoint for getting the current lobby from the server database.
     *
     * @return - returns an integer indicating the id of the currently opened lobby in the server database.
     */
    public int getCurrentLobby() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/game/currentOpenLobby") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(Integer.class);
    }

    /**
     * Method that is one part of the endpoint for adding the user to the server database.
     *
     * @param user - user object to be added to the server database.
     * @return - returns a user that has been added to the server database.
     */
    public User addUser(User user) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    /**
     * Method that is one part of the endpoint for updating score of a specific user in the server
     * database. NOTE: not used, might be used.
     *
     * @param user - user for which the score needs to be updated in the server database.
     */
    public void updateScore(User user) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user/updateScore") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    /**
     * Method that is one part of the endpoint for remove a specific user in the server
     * database. NOTE: not used, might be used.
     *
     * @param user - user for which the score needs to be updated in the server database.
     */
    public void removeUser(User user) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user/removePlayer") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(user, APPLICATION_JSON), User.class);
    }

    /**
     * Method that is one part of the endpoint for getting the list of users in the current lobby from
     * the server database.
     *
     * @return - list of users in the current lobby in the server database.
     */
    public List<User> getUsersInLobby() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user/currentLobby") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<User>>() {
                });
    }

    /**
     * Method that is one part of the endpoint for getting the list of users based on the lobby number
     * from the server database.
     *
     * @param lobbyNumber - lobby number indicating which lobby to access.
     * @return - returns the list of all users inside the lobby with a specific lobby number from the
     * server database.
     */
    public List<User> getUserLobbyNumber(int lobbyNumber) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user/lobby/" + lobbyNumber) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<User>>() {
                });
    }

    /**
     * Method that is one part of the endpoint for removing user from the server database.
     *
     * @param username    - username of the user to be removed from the specific lobby.
     * @param lobbyNumber - lobby number which the user needs to be removed from.
     */
    public void removeUser(String username, int lobbyNumber) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user/removePlayer/" + username + "/" + lobbyNumber) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * Method that is one part of the endpoint for starting the multiplayer game.
     */
    public void startGame() {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/game/start") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get();
    }

    /**
     * Method that is responsible for the connection of the client with the websocket that is also
     * specified in the server side.
     *
     * @param url - url of the websocket.
     * @return - returns a stomp session object which indicates the websocket connection.
     */
    public StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Method responsible for registering a message from the websocket connection.
     *
     * @param dest                     - the destination of the websocket message.
     * @param websocketMessageConsumer - list of websocket messages that are directed for the message.
     * @return - returns a subscription of the current stomp session used for websocket communication.
     */
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

    /**
     * Method responsible for sending the websocket message.
     *
     * @param dest             - destination of the message.
     * @param websocketMessage - websocket used for the bidirectional communication.
     */
    public void send(String dest, WebsocketMessage websocketMessage) {
        session.send(dest, websocketMessage);
    }

    /**
     * Method that is one part of the endpoint for getting the list of scores from the server database.
     *
     * @return - returns the list of scores for single player users from the server database.
     */
    public List<Score> getScores() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/scores/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Score>>() {
                });
    }

    /**
     * Method that is one part of the endpoint for getting the list of top 3 scores from the server database.
     *
     * @return - returns the list of top 3 scores for single player users from the server database.
     */
    public List<Score> getTopScores() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/scores/top") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Score>>() {
                });
    }

    /**
     * Method that is one part of the endpoint for adding a single player score to the server database.
     *
     * @param score - score object to be added to the server database.
     * @return - score that has been added to the server database.
     */
    public Score addScore(Score score) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/scores/") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(score, APPLICATION_JSON), Score.class);
    }

    /**
     * Method for validating if the username could be used by a user.
     *
     * @param username - username user wants to have.
     * @return - returns true if the username could be used by a user.
     */
    public boolean isValidUsername(String username) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/user/isValidUsername/" + username.toLowerCase(Locale.ROOT)) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(boolean.class);
    }
}


