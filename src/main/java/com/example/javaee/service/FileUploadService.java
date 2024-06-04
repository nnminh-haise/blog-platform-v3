package com.example.javaee.service;

import com.example.javaee.beans.FileUploadDirectory;
import org.springframework.stereotype.Service;

@Service
public class FileUploadService {
    private final FileUploadDirectory fileUploadDirectory;

    public FileUploadService(FileUploadDirectory fileUploadDirectory) {
        this.fileUploadDirectory = fileUploadDirectory;
    }


}
