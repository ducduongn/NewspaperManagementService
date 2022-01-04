package com.example.springsecuritydemo.messaging.rabbitmq;

import com.example.springsecuritydemo.constant.crawler.URLConstant;
import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.articles.Category;
import com.example.springsecuritydemo.repository.ArticleRepository;
import com.example.springsecuritydemo.repository.CategoryRepository;
import com.example.springsecuritydemo.utils.crawler.CrawlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    public void crawlArticle(String url) {
        try {
            Document document = Jsoup.connect(url).get();

            Elements articlesHtmlTags = document.select("article.item-news");

            //Crawl all articles of one page of a category
            for (Element tag : articlesHtmlTags) {
                Article article = new Article();

                Element titleNews = tag.selectFirst(".title-news > a");

                Elements categoryElements= document.select((".breadcrumb li a"));

                List<String> urlCategoryList = CrawlerUtils.getCategoryUrlListFromElement(categoryElements);

                if (titleNews != null) {
                    article.setUrl(titleNews.attr("abs:href"));
                    article.setTitle(titleNews.text());
                } else {
                    continue;
                }

                List<Category> categories = new ArrayList<>();

                if (article.getUrl().contains(URLConstant.VN_EXPRESS_HOME)) {
                    for (String categoryUrl : urlCategoryList) {
                        Category category = categoryRepository.findByUrl(categoryUrl)
                                .orElse(new Category());

                        if (category.getUrl() != null) {
                            categories.add(category);
                        }
                    }
                    article.setCategories(categories);
                    article.setAuthor(CrawlerUtils.getAuthor(article));
                    article.setDescription(CrawlerUtils.getDescription(article));
                    article.setContent(CrawlerUtils.getArticleContent(article));
                    article.setStringPostedDate(CrawlerUtils.getTimeTag(article));
                }

                if (!articleRepository.existsByUrl(article.getUrl())) {
                    articleRepository.save(article);
                    log.info("Save article successfully: " + url);
                } else {
                    log.info("Existed article!");
                }
            }

        } catch (IOException e) {
            log.error("IO  exception!");
        }
    }

}
