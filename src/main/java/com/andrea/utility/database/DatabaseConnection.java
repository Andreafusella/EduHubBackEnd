package com.andrea.utility.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private final String URL = "jdbc:postgresql://localhost:8001/EduHub";
    private final String userDB = "postgres";
    private final String pwsDB = "password";

    private DatabaseConnection(){
        try {

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, userDB, pwsDB);
            connection.setAutoCommit(true);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
