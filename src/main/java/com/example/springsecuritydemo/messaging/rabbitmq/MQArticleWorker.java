package com.example.springsecuritydemo.messaging.rabbitmq;

import com.example.springsecuritydemo.constant.crawler.URLConstant;
import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.articles.Category;
import com.example.springsecuritydemo.repository.ArticleRepository;
import com.example.springsecuritydemo.repository.CategoryRepository;
import com.example.springsecuritydemo.utils.crawler.CrawlerUtils;
import com.example.springsecuritydemo.utils.crawler.DateTimeConverter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.MalformedURLException;
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

    @Transactional
    public void crawlArticle(String url) {
        try {
            Document document = Jsoup.connect(url).get();

            Elements articlesHtmlTags = document.select("article.item-news");

            //Crawl all articles of one page of a category
            for (Element tag : articlesHtmlTags) {
                Article article = new Article();

                Element titleNews = tag.selectFirst(".title-news > a");

                if (titleNews != null) {
                    article.setUrl(titleNews.attr("abs:href"));
                    article.setTitle(titleNews.text());
                } else {
                    continue;
                }

                List<Category> categories = new ArrayList<>();
                List<String> urlCategoryList = CrawlerUtils.collectCategoriesUrlFromArticle(article);

                if (article.getUrl().contains(URLConstant.VN_EXPRESS_HOME)) {
                    for (String categoryUrl : urlCategoryList) {
                        Category category = categoryRepository.findByUrl(categoryUrl)
                                .orElse(new Category());


                        if (category.getUrl() != null) {
                            categories.add(category);
                        }
                    }
                    log.info("ArticleUrl: " + article.getUrl()+ " from: " + url);
                    article.setCategories(categories);
                    article.setAuthor(CrawlerUtils.getAuthor(article));
                    article.setDescription(CrawlerUtils.getDescription(article));
                    article.setContent(CrawlerUtils.getArticleContent(article));
                    article.setStringPostedDate(CrawlerUtils.getTimeTag(article));
                    article.setPostedDate(DateTimeConverter.convertDateTimeStringToLocalDateTime(
                            article.getStringPostedDate().trim()
                    ));
                }

                if (!articleRepository.existsByUrl(article.getUrl())) {
                    Article saveArticle = articleRepository.save(article);
                    log.info("Save article successfully: " + saveArticle.getUrl());
                } else {
                    log.info("Existed article!");
                }
            }

        } catch (IOException e) {
            log.error("IO  exception!");
        }
    }

}
