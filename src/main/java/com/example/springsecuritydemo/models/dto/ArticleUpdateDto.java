package com.example.springsecuritydemo.models.dto;

import com.example.springsecuritydemo.models.articles.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author ducduongn
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArticleUpdateDto {
    private static final long serialVersionUID = 1L;

    private String author;

    private String title;

    private String content;

    private String description;
}
