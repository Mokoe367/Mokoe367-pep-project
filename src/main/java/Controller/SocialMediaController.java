package Controller;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessage);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromUser);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerUser(Context context) {

        ObjectMapper om = new ObjectMapper();
        String jsonString = context.body();
    
        try {
            Account account = om.readValue(jsonString, Account.class);
            if(accountService.createAccount(account.getUsername(), account.getPassword())) {
                Account newAccount = accountService.getAccount(account.getUsername(), account.getPassword());
                context.json(newAccount);
                context.status(200);
            } else {
                context.status(400);
            }

        } catch (JsonMappingException e) {
            context.status(401);
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            context.status(401);
            e.printStackTrace();
        }
    }

    private void loginUser(Context context) {
        ObjectMapper om = new ObjectMapper();
        String jsonString = context.body();

        try {

            JsonNode jsonNode = om.readTree(jsonString);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();

            Account userAccount = accountService.getAccount(username, password);
            if(userAccount != null) {
                context.json(userAccount);
                context.status(200);
            } else {
                context.status(401);
            }

        } catch (JsonMappingException e) {
            context.status(401);
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            context.status(401);
            e.printStackTrace();
        }
    }

    private void createMessage(Context context) {
        ObjectMapper om = new ObjectMapper();
        String jsonString = context.body();

        try {
            Message newMessage = messageService.createMessage(om.readValue(jsonString, Message.class));

            if(newMessage != null) {
                context.json(newMessage);
                context.status(200);
            } else {
                System.out.println(newMessage);
                context.status(400);
            }

        } catch (JsonMappingException e) {
            context.status(401);
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            context.status(401);
            e.printStackTrace();
        }
    }

    public void getAllMessages(Context context) {
        context.json(messageService.getAllMessages());
        context.status(200);
    }

    public void getMessage(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessage(message_id);
        if(message != null) {
            context.json(message);
        } else {
            context.result("");
        }
        
        context.status(200);
    }

    public void deleteMessage(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessage(message_id);
        if(message != null) {
            context.json(message);
            messageService.deleteMessage(message_id);
        } else {
            context.result("");
        }
        
        context.status(200);
    }

    public void updateMessage(Context context) {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        ObjectMapper om = new ObjectMapper();
        String jsonString = context.body();

        try {
            Message oldMessage = messageService.getMessage(message_id);
            Message newMessage = om.readValue(jsonString, Message.class);

            if(oldMessage != null) {
                oldMessage.setMessage_text(newMessage.getMessage_text());
                if(messageService.updateMessage(oldMessage) != null) {
                    context.json(oldMessage);
                    context.status(200);
                } else {
                    context.status(400);
                }
            } else {
                context.status(400);
            }

        } catch (JsonMappingException e) {
            context.status(401);
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            context.status(401);
            e.printStackTrace();
        }
    }

    public void getAllMessagesFromUser(Context context) {
        int account_id = Integer.parseInt(Objects.requireNonNull(context.pathParam("account_id")));
        context.json(messageService.getAllUserMessages(account_id));
        context.status(200);
    }
}