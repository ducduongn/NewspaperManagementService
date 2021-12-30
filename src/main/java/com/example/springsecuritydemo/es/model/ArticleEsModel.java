package com.example.springsecuritydemo.es.model;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author ducduongn
 */
@Document(indexName = "article")
public class ArticleEsModel {
    @Id
    private Long id;

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

    public ArticleEsModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public String getStringPostedDate() {
        return stringPostedDate;
    }

    public void setStringPostedDate(String stringPostedDate) {
        this.stringPostedDate = stringPostedDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
