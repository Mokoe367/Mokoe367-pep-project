package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public Message createMessage(Message message) {
        if(message.getMessage_text().length() < 255 && message.getMessage_text() != "" && accountDAO.doesUserIdExist(message.getPosted_by())) {
            Message newMessage = messageDAO.createMessage(message);
            if(newMessage != null) {
                return newMessage;
            }
        }

        return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessage(int message_id) {
        return messageDAO.getMessage(message_id);
    }

    public Message deleteMessage(int message_id) {
        Message deletedMessage = messageDAO.getMessage(message_id);
        messageDAO.deleteMessage(message_id);

        return deletedMessage;
    }

    public Message updateMessage(Message message) {
        if(message.getMessage_text().length() < 255 && message.getMessage_text() != "" && accountDAO.doesUserIdExist(message.getPosted_by())) {
            Message updatedMessage = messageDAO.createMessage(message);
            if(updatedMessage != null) {
                return updatedMessage;
            }
        }

        return null;
    }

    public List<Message> getAllUserMessages(int account_id) {
        return messageDAO.getAllUsersMessages(account_id);
    }

}
