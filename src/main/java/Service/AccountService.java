package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public Account getAccount(String username, String password) {
        return accountDAO.getAccount(username, password);
    }

    public boolean createAccount(String username, String password) {
        if(username != "" && password.length() > 4) {
            if(!accountDAO.doesUsernameExist(username)) {
                Account newAccount = new Account(username, password);
                accountDAO.createAccount(newAccount);
                return true;
            }
        }

        return false;
    }
}
