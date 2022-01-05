package com.example.springsecuritydemo.es.repository;

import com.example.springsecuritydemo.models.es.ArticleEsModel;
import com.example.springsecuritydemo.models.articles.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ducduongn
 */
@Repository
public interface EsArticleRepository extends ElasticsearchRepository<ArticleEsModel, Long> {
    Article findByAuthor(String author);

    Article findByUrl(String url);

    Boolean existsByUrl(String url);
}
