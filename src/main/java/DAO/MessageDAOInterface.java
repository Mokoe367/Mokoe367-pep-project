package DAO;

import java.util.List;

import Model.Message;

public interface MessageDAOInterface {
    public List<Message> getAllMessages();
    public List<Message> getAllUsersMessages(int account_id);
    public Message getMessage(int message_id);
    public Message createMessage(Message message);
    public Message updateMessage(Message message);
    public boolean deleteMessage(int message_id);
}
