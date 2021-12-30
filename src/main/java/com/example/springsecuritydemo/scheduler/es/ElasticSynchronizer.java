package com.example.springsecuritydemo.scheduler.es;

import com.example.springsecuritydemo.constant.Constants;
import com.example.springsecuritydemo.es.model.ArticleEsModel;
import com.example.springsecuritydemo.es.repository.EsArticleRepository;
import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author ducduongn
 */

@Service
@Slf4j
public class ElasticSynchronizer {
    private final ArticleRepository articleRepository;

    private final EsArticleRepository esArticleRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ElasticSynchronizer(ArticleRepository articleRepository,
                               EsArticleRepository esArticleRepository,
                               ModelMapper modelMapper) {
        this.articleRepository = articleRepository;
        this.esArticleRepository = esArticleRepository;
        this.modelMapper = modelMapper;
    }

    @Scheduled(cron = "${interval-sync-cron-article}")
    @Transactional
    public void sync() {
        log.info("Start syncing...");
        this.syncArticle();
        log.info("End syncing!");
    }

    public void syncArticle() {
        Specification<Article> articleSpecification = (
                (root, criteriaQuery, criteriaBuilder) -> getCreatedDatePredicate(criteriaBuilder, root));
        List<Article> articleList;
        if (esArticleRepository.count() == 0) {
            articleList = articleRepository.findAll();
        } else {
            articleList = articleRepository.findAll(articleSpecification);
        }
        for(Article article: articleList) {
            log.info("Syncing article - {}", article.getId());
            esArticleRepository.save(this.modelMapper.map(article, ArticleEsModel.class));
        }

    }

    public static Predicate getCreatedDatePredicate(CriteriaBuilder cb, Root<?> root) {
        Expression<Timestamp> currentTime;
        currentTime = cb.currentTimestamp();
        Expression<Timestamp> currentTimeMinus = cb.literal(new Timestamp(System.currentTimeMillis() -
                (Constants.INTERVAL_IN_MILLISECOND)));

        return cb.between(root.get(Constants.POSTED_DATE),
                currentTimeMinus,
                currentTime
        );
    }
}
