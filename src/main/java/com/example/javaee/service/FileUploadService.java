package com.example.javaee.service;

import com.example.javaee.beans.FileUploadDirectory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileUploadService {
    public Boolean saveFile(MultipartFile file, String directory) {
        if (file.isEmpty()) {
            return false;
        }

        try {
            Path parentPath = Paths.get(directory);
            if (!Files.exists(parentPath)) {
                Files.createDirectories(parentPath);
            }
            File savedFile = new File(directory + "/" + file.getOriginalFilename());
            file.transferTo(savedFile);
            return true;
        } catch (IOException ioException) {
            return false;
        }
    }

    public Map<Integer, Boolean> saveFiles(MultipartFile[] files, String[] directories) {
        if (files == null || directories == null) {
            return new HashMap<>();
        }

        if (files.length != directories.length) {
            return new HashMap<>();
        }

        Map<Integer, Boolean> result = new HashMap<>();
        for (int i = 0; i < files.length; ++i) {
            MultipartFile file = files[i];
            String directory = directories[i];

            Boolean createdFile = this.saveFile(file, directory);
            result.put(i, createdFile);
        }
        return result;
    }
}
