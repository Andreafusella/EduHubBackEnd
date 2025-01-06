package com.andrea.controller;

import com.andrea.dto.FileGetDto;
import com.andrea.service.FileService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FileController {
    private String uploadDir = "uploads";
    private FileService fileService = new FileService(uploadDir);

    public void registerRoutes(Javalin app) {
        app.post("/upload", this::uploadFile);
        app.get("/files", this::getFilesBySubject);
        app.get("/download/{param}", this::downloadFile);
        app.delete("/file/{id_file}", this::deleteFile);
    }

    public void getFilesBySubject(Context ctx) {
        String idSubjectStr = ctx.queryParam("id_subject");

        if (idSubjectStr != null) {
            try {
                int idSubject = Integer.parseInt(idSubjectStr);
                List<FileGetDto> fileList = fileService.getFilesBySubject(idSubject);

                if (fileList.isEmpty()) {
                    ctx.status(404).result("No files found for the given subject");
                } else {
                    ctx.json(fileList);
                }
            } catch (SQLException | NumberFormatException e) {
                ctx.status(500).result("Error fetching files: " + e.getMessage());
            }
        } else {
            ctx.status(400).result("id_subject parameter is missing");
        }
    }


    public void downloadFile(Context ctx) {

        String filePath = ctx.pathParam("param");

        if (filePath != null) {

            String basePath = "/Users/andreafusella/Desktop/Desktop/Progetti/EduHubBackEnd/";
            java.nio.file.Path path = java.nio.file.Paths.get(basePath + filePath);
            System.out.println("File path: " + path.toString());

            try {
                if (java.nio.file.Files.exists(path)) {
                    // Usa FileInputStream per leggere il contenuto del file
                    FileInputStream fileInputStream = new FileInputStream(path.toFile());

                    // Imposta la risposta per il download del file
                    ctx.result(fileInputStream);
                    ctx.contentType("application/octet-stream");
                    ctx.header("Content-Disposition", "attachment; filename=" + path.getFileName());
                } else {
                    ctx.status(404).result("File not found");
                }
            } catch (IOException e) {
                ctx.status(500).result("Error downloading file: " + e.getMessage());
            }
        } else {
            ctx.status(400).result("File path parameter is missing");
        }
    }

    public void uploadFile(Context ctx) {
        System.out.println("Caricamento file");
        String idSubjectStr = ctx.formParam("id_subject");
        String name = ctx.formParam("name");
        String description = ctx.formParam("description");

        UploadedFile uploadedFile = ctx.uploadedFile("file");

        if (uploadedFile != null && idSubjectStr != null) {
            try {
                int idSubject = Integer.parseInt(idSubjectStr);
                fileService.saveFile(uploadedFile, idSubject, name, description);
                ctx.status(200).result("File uploaded and saved successfully");
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                ctx.status(500).result("Error processing file: " + e.getMessage());
            } catch (NumberFormatException e) {
                ctx.status(400).result("Invalid id_subject format");
            }
        } else {
            ctx.status(400).result("File or id_subject missing");
        }
    }

    public void deleteFile(Context ctx) {
        String idFileStr = ctx.pathParam("id_file");

        if (idFileStr != null) {
            try {
                int idFile = Integer.parseInt(idFileStr);
                boolean isDeleted = fileService.deleteFile(idFile);

                if (isDeleted) {
                    ctx.status(200).result("File deleted successfully");
                } else {
                    ctx.status(404).result("File not found");
                }
            } catch (NumberFormatException e) {
                ctx.status(400).result("Invalid id_file parameter");
            } catch (SQLException e) {
                ctx.status(500).result("Error deleting file: " + e.getMessage());
            } catch (IOException e) {
                ctx.status(500).result("Error deleting file from filesystem: " + e.getMessage());
            }
        } else {
            ctx.status(400).result("id_file parameter is missing");
        }
    }
}
