package com.andrea.auth.dao;

import com.andrea.auth.dto.LoginDto;
import com.andrea.utility.database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AuthDao {

    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public List<Object> findUser (LoginDto loginDto) {
        String findUser = "SELECT id_account, password FROM credential WHERE email = ?";
        String findRole = "SELECT role FROM account WHERE id_account = ?";

        try {
            PreparedStatement account = connection.prepareStatement(findUser);

            account.setString(1, loginDto.getEmail());

            ResultSet rs = account.executeQuery();
            List<Object> list = new ArrayList<>();
            if (rs.next()) {

                int id_account = rs.getInt("id_account");

                PreparedStatement role = connection.prepareStatement(findRole);
                role.setInt(1, id_account);
                ResultSet rs2 = role.executeQuery();

                if (rs2.next()) {
                    String roleAccount = rs2.getString("role");

                    String hashedPassword = rs.getString("password");
                    boolean flag = BCrypt.checkpw(loginDto.getPassword(), hashedPassword);
                    list.add(id_account);
                    list.add(flag);
                    list.add(roleAccount);

                    return list;
                } else {
                    throw new SQLException("Ruolo non trovato per l'account ID: " + id_account);
                }

            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
