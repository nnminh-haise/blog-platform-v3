package com.example.javaee.model;

import lombok.*;

import jakarta.persistence.*;
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
@Table(name = "categories")
@AttributeOverrides({
        @AttributeOverride(name = "createAt", column = @Column(name = "create_at")),
        @AttributeOverride(name = "updateAt", column = @Column(name = "update_at")),
        @AttributeOverride(name = "deleteAt", column = @Column(name = "delete_at"))
})
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private Collection<CategoryDetail> categoryDetails;

    public Long getNumberOfBlog() {
        return this.categoryDetails
                .stream()
                .filter(categoryDetail -> categoryDetail.getDeleteAt() == null)
                .map(CategoryDetail::getBlog)
                .filter(blog -> blog.getDeleteAt() == null)
                .count();
    }

    public List<Blog> getRelatedBlogs() {
        return this.categoryDetails
                .stream()
                .filter(categoryDetail -> categoryDetail.getDeleteAt() == null)
                .map(CategoryDetail::getBlog)
                .filter(blog -> blog.getDeleteAt() == null)
                .collect(Collectors.toList());
    }

    public List<Blog> getRelatedBlogs(int page, int size) {
        int offset = page * size;
        return this.categoryDetails
                .stream()
                .filter(categoryDetail -> categoryDetail.getDeleteAt() == null)
                .map(CategoryDetail::getBlog)
                .skip(offset)
                .limit(size)
                .filter(blog -> blog.getDeleteAt() == null)
                .collect(Collectors.toList());
    }

    public String toString() {
        return "id = " + id + " name = " + name + " slug = " + slug;
    }
}
