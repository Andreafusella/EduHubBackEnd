package com.andrea.dao;

import com.andrea.dto.NewAccountDto;
import com.andrea.exception.EmailExistException;
import com.andrea.model.Account;
import com.andrea.model.AccountWithEmail;
import com.andrea.utility.database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private Connection connection = DatabaseConnection.getInstance().getConnection();

    //todo: correggere se email gia esiste con eccezione personalizzata
    public Account addAccount(NewAccountDto account) throws EmailExistException {
        String addAccount = "INSERT INTO account (name, last_name, role) VALUES (?, ?, ?)";
        String emailExist = "SELECT COUNT(*) FROM credential WHERE email = ?";
        String addCredential = "INSERT INTO credential (id_account, email, password) VALUES (?, ?, ?)";

        try {
            PreparedStatement checkEmail = connection.prepareStatement(emailExist);
            checkEmail.setString(1, account.getEmail());

            ResultSet rs = checkEmail.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Email already exist");
                throw new EmailExistException("Email already exist");
            }

            PreparedStatement newAccount = connection.prepareStatement(addAccount, Statement.RETURN_GENERATED_KEYS);

            newAccount.setString(1, account.getName());
            newAccount.setString(2, account.getLast_name());
            newAccount.setString(3, account.getRole());

            int affectedRows = newAccount.executeUpdate();
            int generateId = 0;

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = newAccount.getGeneratedKeys()){
                    if (generatedKeys.next()) {
                        generateId = generatedKeys.getInt(1);
                    }
                }
            } else {
                throw new SQLException("Creazione account fallita, nessuna riga aggiunta.");
            }

            PreparedStatement newCredential = connection.prepareStatement(addCredential);
            String hashedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());

            newCredential.setInt(1, generateId);
            newCredential.setString(2, account.getEmail());
            newCredential.setString(3, hashedPassword);

            newCredential.executeUpdate();

            Account newAccountReturn = new Account(generateId, account.getName(), account.getLast_name(), account.getRole());

            return newAccountReturn;

        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    public List<AccountWithEmail> getAllAccountWithEmail() {

        List<AccountWithEmail> listAccount = new ArrayList<>();
        String getAccount = """
        SELECT
        account.Id_Account,
                account.Name,
                account.Last_Name,
                account.Role,
                credential.Email
        FROM
        Account account
        INNER JOIN
        Credential credential
        ON
        account.Id_Account = credential.Id_Account""";

        try {
            PreparedStatement getAllAccount = connection.prepareStatement(getAccount);

            ResultSet rs = getAllAccount.executeQuery();

            while (rs.next()) {
                AccountWithEmail account = new AccountWithEmail();
                account.setId_account(rs.getInt("id_account"));
                account.setName(rs.getString("name"));
                account.setLastName(rs.getString("last_name"));
                account.setRole(rs.getString("role"));
                account.setEmail(rs.getString("email"));

                listAccount.add(account);
            }

            return listAccount;
        } catch (SQLException e) {
            return null;
        }
    }

    public String removeAccount(int id_account) {
        String remove = "DELETE FROM account WHERE id_account = ?";
        String checkEmail = "SELECT email FROM credential WHERE id_account = ?";

        try {
            PreparedStatement emailStm = connection.prepareStatement(checkEmail);
            emailStm.setInt(1, id_account);

            ResultSet rs = emailStm.executeQuery();
            String email = "";
            if (rs.next()) {
                email = rs.getString("email");
            } else {
                return null;
            }

            PreparedStatement deleteAccount = connection.prepareStatement(remove);
            deleteAccount.setInt(1, id_account);

            int affectedRows = deleteAccount.executeUpdate();

            if (affectedRows > 0) {
                return email;
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
