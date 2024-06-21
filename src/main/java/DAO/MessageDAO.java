package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO implements MessageDAOInterface {

    @Override
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();

        try {
            Connection connection = ConnectionUtil.getConnection();

            String sql = "select * from message;";

            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                messages.add(new Message(
                result.getInt("message_id"), 
                result.getInt("posted_by"), 
                result.getString("message_text"),
                result.getLong("time_posted_epoch")                
                ));
            }

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public List<Message> getAllUsersMessages(int account_id) {
        List<Message> messages = new ArrayList<>();

        try {
            Connection connection = ConnectionUtil.getConnection();

            String sql = "select * from message where posted_by = ?;";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, account_id);

            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                messages.add(new Message(
                result.getInt("message_id"), 
                result.getInt("posted_by"), 
                result.getString("message_text"),
                result.getLong("time_posted_epoch")                
                ));
            }

            connection.close();

        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public Message getMessage(int message_id) {
        Message message = new Message();

        try {
            Connection connection = ConnectionUtil.getConnection();

            String sql = "select * from message where message_id = ?;";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, message_id);

            ResultSet result = stmt.executeQuery();

            if(result.next()) {
                message.setMessage_id(result.getInt("message_id"));            
                message.setPosted_by(result.getInt("posted_by")); 
                message.setMessage_text(result.getString("message_text"));
                message.setTime_posted_epoch(result.getLong("time_posted_epoch"));
            } else {
                return null;
            }

            connection.close();

        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }

        return message;
    }

    @Override
    public Message createMessage(Message message) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?);";

            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());

            int result = stmt.executeUpdate();
            ResultSet key = stmt.getGeneratedKeys();
            if(result > 0 && key.next()) {
                return this.getMessage(key.getInt("message_id"));
            } else {
                return null;
            } 
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Message updateMessage(Message message) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            String sql = "update message set posted_by = ?, message_text = ?, time_posted_epoch = ? where message_id = ?;";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());
            stmt.setInt(4, message.getMessage_id());

            int result = stmt.executeUpdate();

            if(result > 0) {
                return this.getMessage(message.getMessage_id());
            }  else {
                return null;
            }
            
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteMessage(int message_id) {
        boolean wasDeleted = false;

        try {
            Connection connection = ConnectionUtil.getConnection();

            String sql = "delete from message where message_id = ?;";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, message_id);

            int result = stmt.executeUpdate();

            if(result > 0) {
                wasDeleted = true;
            } 
            
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return wasDeleted;
    }
    
}
