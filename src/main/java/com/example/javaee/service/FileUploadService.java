package com.example.javaee.service;

import com.example.javaee.beans.FileUploadDirectory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileUploadService {
    private final FileUploadDirectory fileUploadDirectory;

    public FileUploadService(FileUploadDirectory fileUploadDirectory) {
        this.fileUploadDirectory = fileUploadDirectory;
    }

    public String saveFile(MultipartFile file, String directory) {
        if (file.isEmpty()) {
            return "";
        }

        StringBuilder fullPath = new StringBuilder(this.fileUploadDirectory.getBaseDirectory() + "/");
        if (directory != null && this.fileUploadDirectory.getDirectory(directory) != null) {
            fullPath.append(this.fileUploadDirectory.getDirectory(directory)).append("/");
        }
        fullPath.append(file.getOriginalFilename());
        try {
            File savedFile = new File(fullPath.toString());
            if (!savedFile.getParentFile().exists()) {
                Files.createDirectory(Paths.get(savedFile.getParentFile().toURI()));
            }
            file.transferTo(savedFile);
            return fullPath.toString();
        }
        catch (IOException ioException) {
            return "";
        }
    }

    public Map<Integer, String> saveFiles(MultipartFile[] files, String[] directories) {
        if (files == null || directories == null) {
            return new HashMap<>();
        }

        if (files.length != directories.length) {
            return new HashMap<>();
        }

        Map<Integer, String> result = new HashMap<>();
        for (int i = 0; i < files.length; ++i) {
            MultipartFile file = files[i];
            String directory = directories[i];

            String createdFile = this.saveFile(file, directory);
            result.put(i, createdFile);
        }
        return result;
    }


}
