package com.example.springsecuritydemo.messaging.rabbitmq;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.articles.Category;
import com.example.springsecuritydemo.repository.ArticleRepository;
import com.example.springsecuritydemo.repository.CategoryRepository;
import com.example.springsecuritydemo.utils.crawler.CrawlerUtils;
import com.example.springsecuritydemo.utils.crawler.DateTimeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ducduongn
 */
@Slf4j
@Service
public class MQArticleWorker {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MQSynchronizeSender mqSynchronizeSender;

    @Transactional
    public void crawlArticle(String articleUrl) {
        log.info("Crawl url: " + articleUrl);
        List<String> urlCategoryList = CrawlerUtils.collectCategoriesUrlFromArticle(articleUrl);

        // return if category list can not be crawled
        if (urlCategoryList.size() == 0) {
            return;
        }

        List<Category> categories = new ArrayList<>();

        Article article = new Article();

        article.setUrl(articleUrl);

        for (String categoryUrl : urlCategoryList) {
            Category category = categoryRepository.findByUrl(categoryUrl)
                    .orElse(new Category());


            if (category.getUrl() != null) {
                categories.add(category);
            }
        }

        article.setUrl(articleUrl);
        article.setTitle(CrawlerUtils.getArticleTitle(articleUrl));
        article.setCategories(categories);
        article.setAuthor(CrawlerUtils.getAuthor(articleUrl));
        article.setDescription(CrawlerUtils.getDescription(articleUrl));
        article.setContent(CrawlerUtils.getArticleContent(articleUrl));
        article.setStringPostedDate(CrawlerUtils.getTimeTag(articleUrl));
        article.setPostedDate(DateTimeConverter.convertDateTimeStringToLocalDateTime(
                article.getStringPostedDate().trim()
        ));

        if ( !articleRepository.existsByUrl(articleUrl)) {
            try {
                articleRepository.save(article);
                log.info("Successfully save article: " + article.getUrl());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("Article existed" + article.getUrl());
        }
        mqSynchronizeSender.send(article);
    }

}
