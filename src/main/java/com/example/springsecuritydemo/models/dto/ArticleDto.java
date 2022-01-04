package com.example.springsecuritydemo.models.dto;

import com.example.springsecuritydemo.models.articles.Category;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author ducduongn
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArticleDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String stringPostedDate;

    private String author;

    private String url;

    private String title;

    private String content;

    private String description;

    List<Category> categories;
}
