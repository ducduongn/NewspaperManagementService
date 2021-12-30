package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.models.es.ArticleEsModel;
import com.example.springsecuritydemo.models.query.ResultQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ducduongn
 */
@Service
public interface SearchService {
    ResultQuery searchFromQuery(String query);

    List<ArticleEsModel> processSearchQuery(String query);
}
