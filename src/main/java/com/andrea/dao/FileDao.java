package com.andrea.dao;

import com.andrea.dto.FileGetDto;
import com.andrea.utility.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileDao {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public boolean addFile(String file_path, int id_subject, String name, String description) {
        String query = "INSERT INTO file (file_path, id_subject, upload_date, name, description) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setString(1, file_path);
            stm.setInt(2, id_subject);
            stm.setDate(3, Date.valueOf(java.time.LocalDate.now()));
            stm.setString(4, name);
            stm.setString(5, description);

            int rowsAffected = stm.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FileGetDto> getFilesBySubject(int idSubject) throws SQLException {
        List<FileGetDto> listFile = new ArrayList<>();
        String sql = "SELECT id_file, file_path, upload_date, name, description FROM file WHERE id_subject = ? ORDER BY upload_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idSubject);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FileGetDto file = new FileGetDto();
                    file.setId_file(rs.getInt("id_file"));
                    file.setFile_path(rs.getString("file_path"));
                    file.setUpload_date(rs.getObject("upload_date", LocalDate.class));
                    file.setName(rs.getString("name"));
                    file.setDescription(rs.getString("description"));

                    listFile.add(file);
                }
            }
        }
        return listFile;
    }

    public String getFilePathById(int idFile) throws SQLException {
        String query = "SELECT file_path FROM file WHERE id_file = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idFile);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("file_path");
                }
            }
        }
        return null;
    }

    // Metodo per eliminare il file dal database
    public boolean deleteFileById(int idFile) throws SQLException {
        String query = "DELETE FROM file WHERE id_file = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idFile);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
