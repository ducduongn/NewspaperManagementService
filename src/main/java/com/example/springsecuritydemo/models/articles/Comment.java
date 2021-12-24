package com.example.springsecuritydemo.models.articles;

import com.example.springsecuritydemo.models.auth.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_comment_article_"))
    private Article article;

    @Column(name = "posted_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date postedDate;

    @Column(name = "edited_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date editedDate;


}
