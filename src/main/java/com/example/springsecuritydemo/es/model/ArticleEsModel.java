package com.example.springsecuritydemo.es.model;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author ducduongn
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "article")
public class ArticleEsModel {
    @Id
    private Long id;

    @Field(type = FieldType.Date, name = "posted_date")
    private LocalDateTime postedDate;

    @Field(type = FieldType.Text, name = "string_posted_date")
    private String stringPostedDate;

    @Field(type = FieldType.Date, name = "updated_date")
    private LocalDateTime updatedDate;

    @Field(type = FieldType.Text, name = "author")
    private String author;

    @Field(type = FieldType.Text, name = "url")
    private String url;

    @Field(type = FieldType.Text, name = "title")
    private String title;

    @Field(type = FieldType.Text, name = "content")
    private String content;

    @Field(type = FieldType.Text, name = "description")
    private String description;
}
