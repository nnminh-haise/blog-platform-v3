package com.example.javaee.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Column(name = "is_popular", nullable = false)
    private Boolean isPopular = false;

    @OneToMany(mappedBy = "blog", fetch = FetchType.EAGER)
    private Collection<CategoryDetail> categoryDetails;

    public Long getNumberOfCategory() {
        return this.categoryDetails
                .stream()
                .filter(categoryDetail -> categoryDetail.getDeleteAt() == null)
                .map(CategoryDetail::getCategory)
                .filter(category -> category.getDeleteAt() == null)
                .count();
    }

    public List<Category> getCategories() {
        return this.categoryDetails
                .stream()
                .filter(categoryDetail -> categoryDetail.getDeleteAt() == null)
                .map(CategoryDetail::getCategory)
                .filter(category -> category.getDeleteAt() == null)
                .collect(Collectors.toList());
    }

    public List<Category> getCategories(int page, int size) {
        int offset = page * size;
        return this.categoryDetails
                .stream()
                .filter(categoryDetail -> categoryDetail.getDeleteAt() == null)
                .map(CategoryDetail::getCategory)
                .skip(offset)
                .limit(size)
                .filter(category -> category.getDeleteAt() == null)
                .collect(Collectors.toList());
    }

    public String toString() {
        return "id = " + id +
                " title = " + title +
                " description = " + description +
                " attachment = " + attachment +
                " slug = " + slug +
                " is popular = " + isPopular +
                " created at = " + super.getCreateAt().toString() +
                " updated at = " + super.getUpdateAt().toString();
    }
}
