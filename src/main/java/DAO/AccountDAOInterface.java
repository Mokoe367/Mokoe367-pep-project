package DAO;

import Model.Account;

public interface AccountDAOInterface {
    public Account getAccount(String username, String password);
    public boolean createAccount(Account account);
    public boolean doesUsernameExist(String username);
    public boolean doesUserIdExist(int account_id);
}
