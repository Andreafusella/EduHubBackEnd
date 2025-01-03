package com.andrea.dao;

import com.andrea.dto.NewAccountDto;
import com.andrea.dto.PresenceDto;
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

    public AccountWithEmail addAccount(NewAccountDto account) throws EmailExistException {
        String addAccount = "INSERT INTO account (name, last_name, role) VALUES (?, ?, ?)";
        String emailExist = "SELECT COUNT(*) FROM credential WHERE email = ?";
        String addCredential = "INSERT INTO credential (id_account, email, password) VALUES (?, ?, ?)";
        String addAvatar = "INSERT INTO settingaccount (id_account, avatar) VALUES (?, ?)";
        String addPresence = "";

        try {

            //controllo se esiste già l'email
            PreparedStatement checkEmail = connection.prepareStatement(emailExist);
            checkEmail.setString(1, account.getEmail());

            ResultSet rs = checkEmail.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Email already exist");
                throw new EmailExistException("Email already exist");
            }

            //creazione riga nuovo account
            PreparedStatement newAccount = connection.prepareStatement(addAccount, Statement.RETURN_GENERATED_KEYS);

            newAccount.setString(1, account.getName());
            newAccount.setString(2, account.getLast_name());
            newAccount.setString(3, account.getRole());

            int affectedRows = newAccount.executeUpdate();
            int generateId = 0;

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = newAccount.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generateId = generatedKeys.getInt(1);
                    }
                }
            } else {
                throw new SQLException("Account creation failed, no rows added");
            }

            //creazione riga credenziali
            PreparedStatement newCredential = connection.prepareStatement(addCredential);
            String hashedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());

            newCredential.setInt(1, generateId);
            newCredential.setString(2, account.getEmail());
            newCredential.setString(3, hashedPassword);

            newCredential.executeUpdate();

            AccountWithEmail newAccountReturn = new AccountWithEmail(generateId, account.getName(), account.getLast_name(), account.getRole(), account.getEmail(), account.getAvatar());

            //creazione riga avatar
            PreparedStatement newAvatar = connection.prepareStatement(addAvatar);

            newAvatar.setInt(1, generateId);
            newAvatar.setInt(2, account.getAvatar());

            newAvatar.executeUpdate();

            return newAccountReturn;

        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    public List<AccountWithEmail> getAllAccountWithEmailWithParam(String role) {

        List<AccountWithEmail> listAccount = new ArrayList<>();
        String getAccount = """
                SELECT
                    account.Id_Account,
                    account.Name,
                    account.Last_Name,
                    account.Role,
                    credential.Email,
                    settingaccount.Avatar
                FROM
                    Account account
                INNER JOIN
                    Credential credential
                    ON account.Id_Account = credential.Id_Account
                INNER JOIN
                    SettingAccount settingaccount
                    ON account.Id_Account = settingaccount.Id_Account
                WHERE
                    account.Role = ?
                ORDER BY
                    account.Id_Account DESC;""";

        try {
            PreparedStatement getAllAccount = connection.prepareStatement(getAccount);

            getAllAccount.setString(1, role);

            ResultSet rs = getAllAccount.executeQuery();

            while (rs.next()) {
                AccountWithEmail account = new AccountWithEmail();
                account.setId_account(rs.getInt("id_account"));
                account.setName(rs.getString("name"));
                account.setLastName(rs.getString("last_name"));
                account.setRole(rs.getString("role"));
                account.setEmail(rs.getString("email"));
                account.setAvatar(rs.getInt("avatar"));

                listAccount.add(account);
            }

            return listAccount;
        } catch (SQLException e) {
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
                    credential.Email,
                    settingaccount.Avatar
                FROM
                    Account account
                INNER JOIN
                    Credential credential
                    ON account.Id_Account = credential.Id_Account
                INNER JOIN
                    SettingAccount settingaccount
                    ON account.Id_Account = settingaccount.Id_Account
                ORDER BY
                    account.Id_Account DESC;""";
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
                account.setAvatar(rs.getInt("avatar"));

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

    //get student
    public AccountWithEmail getStudent(int id_account) {
        String findStudent = """
                SELECT
                    account.Id_Account,
                    account.Name,
                    account.Last_Name,
                    account.Role,
                    credential.Email,
                    settingaccount.Avatar
                FROM
                    Account account
                INNER JOIN
                    Credential credential
                    ON account.Id_Account = credential.Id_Account
                INNER JOIN
                    SettingAccount settingaccount
                    ON account.Id_Account = settingaccount.Id_Account
                WHERE
                    account.Id_Account = ?
                    AND account.Role = 'Student';""";

        try {
            PreparedStatement getStudent = connection.prepareStatement(findStudent);

            getStudent.setInt(1, id_account);

            ResultSet rs = getStudent.executeQuery();

            if (rs.next()) {
                AccountWithEmail account = new AccountWithEmail();
                account.setId_account(rs.getInt("id_account"));
                account.setName(rs.getString("name"));
                account.setLastName(rs.getString("last_name"));
                account.setRole(rs.getString("role"));
                account.setEmail(rs.getString("email"));
                account.setAvatar(rs.getInt("avatar"));

                return account;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AccountWithEmail> getStudentsByCourse(int id_course) {
        String findStudentsByCourse = """
                SELECT
                    account.Id_Account,
                    account.Name,
                    account.Last_Name,
                    account.Role,
                    credential.Email,
                    settingaccount.Avatar
                FROM
                    Account account
                INNER JOIN
                    Credential credential ON account.Id_Account = credential.Id_Account
                INNER JOIN
                    SettingAccount settingaccount ON account.Id_Account = settingaccount.Id_Account
                INNER JOIN
                    Enrolled enrolled ON account.Id_Account = enrolled.Id_Account
                WHERE
                    enrolled.Id_Course = ?
                    AND account.Role = 'Student';""";

        try {
            PreparedStatement getStudents = connection.prepareStatement(findStudentsByCourse);
            getStudents.setInt(1, id_course);

            ResultSet rs = getStudents.executeQuery();

            List<AccountWithEmail> students = new ArrayList<>();
            while (rs.next()) {
                AccountWithEmail account = new AccountWithEmail();
                account.setId_account(rs.getInt("id_account"));
                account.setName(rs.getString("name"));
                account.setLastName(rs.getString("last_name"));
                account.setRole(rs.getString("role"));
                account.setEmail(rs.getString("email"));
                account.setAvatar(rs.getInt("avatar"));

                students.add(account);
            }

            if (students.isEmpty()) {
                return null;
            } else {
                return students;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AccountWithEmail> getStudentsNotInCourse(int id_course) {

        String findStudentsNotInCourse = """
                SELECT
                    account.Id_Account,
                    account.Name,
                    account.Last_Name,
                    account.Role,
                    credential.Email,
                    settingaccount.Avatar
                FROM
                    Account account
                INNER JOIN
                    Credential credential ON account.Id_Account = credential.Id_Account
                INNER JOIN
                    SettingAccount settingaccount ON account.Id_Account = settingaccount.Id_Account
                LEFT JOIN
                    Enrolled enrolled ON account.Id_Account = enrolled.Id_Account AND enrolled.Id_Course = ?
                WHERE
                    account.Role = 'Student'
                    AND enrolled.Id_Account IS NULL
                ORDER BY
                    account.Id_Account DESC;""";

        try {
            PreparedStatement getStudentsNotInCourse = connection.prepareStatement(findStudentsNotInCourse);
            getStudentsNotInCourse.setInt(1, id_course);

            ResultSet rs = getStudentsNotInCourse.executeQuery();

            List<AccountWithEmail> studentsNotInCourse = new ArrayList<>();
            while (rs.next()) {
                AccountWithEmail account = new AccountWithEmail();
                account.setId_account(rs.getInt("id_account"));
                account.setName(rs.getString("name"));
                account.setLastName(rs.getString("last_name"));
                account.setRole(rs.getString("role"));
                account.setEmail(rs.getString("email"));
                account.setAvatar(rs.getInt("avatar"));

                studentsNotInCourse.add(account);
            }

            if (studentsNotInCourse.isEmpty()) {
                return null;
            } else {
                return studentsNotInCourse;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching students not in course", e);
        }
    }

    public AccountWithEmail getStudentByEmail(String email) {

        String findStudentByEmail = """
                SELECT
                    account.Id_Account,
                    account.Name,
                    account.Last_Name,
                    account.Role,
                    credential.Email,
                    settingaccount.Avatar
                FROM
                    Account account
                INNER JOIN
                    Credential credential ON account.Id_Account = credential.Id_Account
                INNER JOIN
                    SettingAccount settingaccount ON account.Id_Account = settingaccount.Id_Account
                WHERE
                    credential.Email = ?
                    AND account.Role = 'Student';""";

        try {
            PreparedStatement getStudentByEmail = connection.prepareStatement(findStudentByEmail);
            getStudentByEmail.setString(1, email);

            ResultSet rs = getStudentByEmail.executeQuery();

            if (rs.next()) {
                AccountWithEmail account = new AccountWithEmail();
                account.setId_account(rs.getInt("id_account"));
                account.setName(rs.getString("name"));
                account.setLastName(rs.getString("last_name"));
                account.setRole(rs.getString("role"));
                account.setEmail(rs.getString("email"));
                account.setAvatar(rs.getInt("avatar"));

                return account;
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching student by email", e);
        }
    }

    public List<PresenceDto> getStudentByPresence(int id_lesson) {
        List<PresenceDto> listAccount = new ArrayList<>();
        String findStudent = """
                SELECT a.id_account, a.name, a.last_name, a.role, c.email, s.avatar, p.presence
                FROM account a
                JOIN presence p ON a.id_account = p.id_account
                JOIN settingaccount s ON a.id_account = s.id_account
                JOIN credential c ON a.id_account = c.id_account
                WHERE p.id_lesson = ? AND a.role = 'Student';
                 """;

        try {
            PreparedStatement stm = connection.prepareStatement(findStudent);
            stm.setInt(1, id_lesson);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                PresenceDto account = new PresenceDto();
                account.setId_account(rs.getInt("id_account"));
                account.setName(rs.getString("name"));
                account.setLastName(rs.getString("last_name"));
                account.setRole(rs.getString("role"));
                account.setEmail(rs.getString("email"));
                account.setAvatar(rs.getInt("avatar"));
                account.setPresence(rs.getBoolean("presence"));

                listAccount.add(account);
            }

            if (listAccount.isEmpty()) {
                return null;
            } else {
                return listAccount;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
