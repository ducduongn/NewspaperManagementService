package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.models.es.ArticleEsModel;
import com.example.springsecuritydemo.models.query.ResultQuery;
import com.example.springsecuritydemo.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ducduongn
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    private static final String ARTICLE_INDEX = "es_article";

    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public void setElasticsearchOperations(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public ResultQuery searchFromQuery(String query) {
        return null;
    }

    @Override
    public List<ArticleEsModel> processSearchQuery(String query) {
        log.info("Search with query {}", query);

        QueryBuilder queryBuilder = QueryBuilders
                .multiMatchQuery(query, "title", "description", "category")
                .fuzziness(Fuzziness.AUTO);

        Query searchQuery = new NativeSearchQueryBuilder().withFilter(queryBuilder).build();

        SearchHits<ArticleEsModel> articleHits = elasticsearchOperations
                .search(searchQuery, ArticleEsModel.class, IndexCoordinates.of(ARTICLE_INDEX));

        List<ArticleEsModel> articleMatch = new ArrayList<>();
        articleHits.forEach(hit -> {
            articleMatch.add(hit.getContent());
        });

        return articleMatch;
    }
}
