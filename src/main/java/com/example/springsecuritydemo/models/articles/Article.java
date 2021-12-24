package com.example.springsecuritydemo.models.articles;

import com.example.springsecuritydemo.models.auth.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "article")
@Builder
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_article_author"))
    private User author;

}
