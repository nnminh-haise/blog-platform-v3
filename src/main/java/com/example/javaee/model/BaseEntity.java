package com.example.javaee.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@MappedSuperclass
@Data
public abstract class BaseEntity {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deleteAt;

    public Date createAtAsDate() {
        return this.castLocalDateTimeToDate(this.getCreateAt());
    }

    public Date updateAtAsDate() {
        return this.castLocalDateTimeToDate(this.getUpdateAt());
    }

    public Date deleteAtAsDate() {
        return this.castLocalDateTimeToDate(this.getDeleteAt());
    }

    private Date castLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
