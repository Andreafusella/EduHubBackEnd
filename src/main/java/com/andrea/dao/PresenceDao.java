package com.andrea.dao;

import com.andrea.model.Presence;
import com.andrea.utility.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PresenceDao {

    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public boolean addPresence(Presence presence) {
        String addPresence = "INSERT INTO presence (id_account, id_lesson, presence) VALUES (?, ?, ?)";

        try {
            PreparedStatement stm = connection.prepareStatement(addPresence);
            stm.setInt(1, presence.getId_account());
            stm.setInt(2, presence.getId_lesson());
            stm.setBoolean(3, presence.getPresence());

            stm.executeUpdate();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updatePresence(Presence presence) {
        String updatePresence = "UPDATE presence SET presence = ? WHERE id_account = ? AND id_lesson = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(updatePresence);
            stm.setBoolean(1, presence.getPresence());
            stm.setInt(2, presence.getId_account());
            stm.setInt(3, presence.getId_lesson());

            int rowsAffected = stm.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
