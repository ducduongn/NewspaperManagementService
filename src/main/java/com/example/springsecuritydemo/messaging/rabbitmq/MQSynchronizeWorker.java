package com.example.springsecuritydemo.messaging.rabbitmq;

import com.example.springsecuritydemo.es.repository.EsArticleRepository;
import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.es.ArticleEsModel;
import com.example.springsecuritydemo.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author ducduongn
 */
@Service
@Slf4j
public class MQSynchronizeWorker {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EsArticleRepository esArticleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public void synchronizeArticleFromJpaToEs(Article article) {
        TypeMap<Article, ArticleEsModel> modelTypeMap = modelMapper.createTypeMap(Article.class, ArticleEsModel.class);

        //skip id field on ArticleEsModel
        modelTypeMap.addMappings(mapper -> mapper.skip(ArticleEsModel::setId));

        ArticleEsModel articleEsModel = this.modelMapper.map(article, ArticleEsModel.class);

//        log.info("EsArticle: " + articleEsModel.getUrl());

        try {
            ArticleEsModel searchArticleEs = esArticleRepository.findByUrl(articleEsModel.getUrl());
            if (searchArticleEs == null) {
                esArticleRepository.save(articleEsModel);
                log.info("Successfully synchronize article: " + articleEsModel.getUrl());
            } else {
                log.info("Article is existed");
            }
        } catch (Exception e) {
            log.error("Error in synchronize article: " + articleEsModel.getUrl());
//            e.printStackTrace();
        }
    }
}
