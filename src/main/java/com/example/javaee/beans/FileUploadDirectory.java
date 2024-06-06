package com.example.javaee.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadDirectory {
    private String baseDirectory;

    private Map<String, String> directories;

    public String getDirectory(String key) {
        return this.directories.get(key);
    }
}