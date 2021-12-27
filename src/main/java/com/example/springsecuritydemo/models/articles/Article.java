package com.example.springsecuritydemo.models.articles;

import com.example.springsecuritydemo.models.auth.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "posted_date")
    private LocalDateTime postedDate;

    @Column(name = "string_posted_date")
    private String stringPostedDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "author",
            length = 65535,
            columnDefinition = "text")
    private String author;

    @Column(name="url", unique = true)
    private String url;

    @Column(name = "title")
    private String title;

    @Column(name = "content",
            length = 65535,
            columnDefinition = "text")
    private String content;

    @Column(name = "description",
            length = 65535,
            columnDefinition = "text")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "article_category",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
}
