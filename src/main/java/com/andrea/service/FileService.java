package com.andrea.service;

import com.andrea.dao.FileDao;
import com.andrea.dto.FileGetDto;
import io.javalin.http.UploadedFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@NoArgsConstructor
@Data

public class FileService {
    private FileDao fileDao = new FileDao();
    private String uploadDir;

    public FileService(String uploadDir) {
        this.uploadDir = uploadDir;
        // Crea la directory se non esiste
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void saveFile(UploadedFile uploadedFile, int id_subject, String name, String description) throws IOException, SQLException {
        String filePath = uploadDir + File.separator + uploadedFile.filename();

        fileDao.addFile(filePath, id_subject, name, description);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(uploadedFile.content().readAllBytes());
        }
    }

    public List<FileGetDto> getFilesBySubject(int idSubject) throws SQLException {
        return fileDao.getFilesBySubject(idSubject);
    }

    public boolean deleteFile(int idFile) throws SQLException, IOException {
        // Recupera il file path dal database
        String filePath = fileDao.getFilePathById(idFile);

        if (filePath != null) {

            java.nio.file.Path fullPath = java.nio.file.Paths.get(filePath);
            if (java.nio.file.Files.exists(fullPath)) {
                java.nio.file.Files.delete(fullPath);
            }

            // Elimina il record dal database
            return fileDao.deleteFileById(idFile);
        }

        // Se il file non esiste nel database, ritorna false
        return false;
    }
}
