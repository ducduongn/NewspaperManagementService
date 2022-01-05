package com.example.springsecuritydemo.models.dto;

import com.example.springsecuritydemo.models.articles.Category;
import lombok.*;

import java.util.List;

/**
 * @author ducduongn
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ArticleUpdateDto {
    private static final long serialVersionUID = 1L;

    private String author;

    private String title;

    private String content;

    private String description;
}
