package com.example.javaee.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilePathBuilder {

    private String path;

    private StringBuilder pathBuilder;

    public FilePathBuilder beginWithBaseDirectory(String base) {
        this.pathBuilder = new StringBuilder(base);
        return this;
    }

    public FilePathBuilder addSubDirectory(String subDirectory) {
        this.pathBuilder
                .append("/")
                .append(subDirectory);
        return this;
    }

    public FilePathBuilder ofFile(MultipartFile file) {
        this.pathBuilder.append(file.getOriginalFilename());
        return this;
    }

    public String build() {
        this.path = this.pathBuilder.toString();
        return this.path;
    }

    public String getCurrentPath() {
        return this.pathBuilder.toString();
    }
}
