package com.example.springsecuritydemo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
