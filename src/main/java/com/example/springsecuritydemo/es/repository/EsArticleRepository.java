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
    ArticleEsModel findByAuthor(String author);

    ArticleEsModel findByUrl(String url);
}
