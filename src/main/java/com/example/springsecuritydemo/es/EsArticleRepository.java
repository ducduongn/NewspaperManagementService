package com.example.springsecuritydemo.es;

import com.example.springsecuritydemo.models.articles.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ducduongn
 */
@Repository
public interface EsArticleRepository extends ElasticsearchRepository<Article, Long> {

}
