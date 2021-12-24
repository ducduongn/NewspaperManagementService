package com.example.springsecuritydemo.models.articles;

import com.example.springsecuritydemo.models.auth.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "posted_date")
    private Date postedDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "author")
    private String author;

    @Column(name="url")
    private String url;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "content",
            length = 65535,
            columnDefinition = "text")
    private String content;

    @Column(name = "description",
            length = 65535,
            columnDefinition = "text")
    private String description;
}
