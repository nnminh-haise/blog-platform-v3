package com.example.javaee.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "blogs")
@AttributeOverrides({
        @AttributeOverride(name = "createAt", column = @Column(name = "create_at")),
        @AttributeOverride(name = "updateAt", column = @Column(name = "update_at")),
        @AttributeOverride(name = "deleteAt", column = @Column(name = "delete_at"))
})
public class Blog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @Column(name = "publish_at")
    private LocalDate publishAt;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "attachment", nullable = false)
    private String attachment;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "thumbnail", nullable = true)
    private String thumbnail;

    @Column(name = "sub_title", nullable = true)
    private String subTitle;

    @Column(name = "hidden_status", nullable = false)
    private Boolean hiddenStatus = false;

    @Column(name = "is_popular", nullable = false)
    private Boolean isPopular = false;

    @OneToMany(mappedBy = "blog", fetch = FetchType.EAGER)
    private Collection<CategoryDetail> categoryDetails;

    public Date createAtAsDate() {
        return this.castLocalDateTimeToDate(this.getCreateAt());
    }

    public Date updateAtAsDate() {
        return this.castLocalDateTimeToDate(this.getUpdateAt());
    }

    public Date deleteAtAsDate() {
        return this.castLocalDateTimeToDate(this.getDeleteAt());
    }

    public Date publishDateAsDate() {
        return this.castLocalDateToDate(this.getPublishAt());
    }

    private Date castLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date castLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public String toString() {
        return "id = " + id +
                " title = " + title +
                " publish at = " + publishAt +
                " description = " + description +
                " attachment = " + attachment +
                " slug = " + slug +
                " hidden status = " + hiddenStatus +
                " is popular = " + isPopular +
                " created at = " + super.getCreateAt().toString() +
                " updated at = " + super.getUpdateAt().toString();
    }
}
