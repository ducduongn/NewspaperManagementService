package com.example.springsecuritydemo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author ducduongn
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private Long id;

    private LocalDateTime postedDate;

    private String stringPostedDate;

    private LocalDateTime updatedDate;

    private String author;

    private String url;

    private String title;

    private String content;

    private String description;
}
