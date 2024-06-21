package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;
 
public class AccountDAO implements AccountDAOInterface {

    @Override
    public Account getAccount(String username, String password) {
        Account account = new Account();
        try {
            Connection connection = ConnectionUtil.getConnection();

            String sql = "select * from account where username = ? and password = ?;";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet result = stmt.executeQuery();

            if(result.next()) {
                account.setAccount_id(result.getInt("account_id"));
                account.setUsername(result.getString("username"));
                account.setPassword(result.getString("password"));
            } else {
                return null;
            }

            connection.close();

            return account;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean createAccount(Account account) {
        boolean wasAdded = false;

        try {
            Connection connection = ConnectionUtil.getConnection();

            String sql = "insert into account (username, password) values (?, ?);";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());

            int result = stmt.executeUpdate();

            if(result > 0) {
                wasAdded = true;
            } 
            
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();

        }

        return wasAdded;
    }

    @Override
    public boolean doesUsernameExist(String username) {
        boolean exists = false;

        try {
            Connection connection = ConnectionUtil.getConnection();

            String sql = "select * from account where username = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, username);

            ResultSet result = stmt.executeQuery();

            if(result.next()) {
                exists = true;
            } 
            
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return exists;
    }

    @Override
    public boolean doesUserIdExist(int account_id) {
        boolean doesExist = false;

        try {
            Connection connection = ConnectionUtil.getConnection();

            String sql = "select * from account where account_id = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, account_id);

            ResultSet result = stmt.executeQuery();

            if(result.next()) {
                doesExist = true;
            } 
            
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return doesExist;
    }
    
}
