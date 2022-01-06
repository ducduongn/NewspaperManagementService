package com.example.springsecuritydemo.models.es;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ducduongn
 */
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Document(indexName = "es_article")
public class ArticleEsModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field(type = FieldType.Date, name = "posted_date", format = DateFormat.date_time)
    private LocalDateTime postedDate;

    @Field(type = FieldType.Text, name = "string_posted_date")
    private String stringPostedDate;

    @Field(type = FieldType.Date, name = "updated_date", format = DateFormat.date_time)
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
