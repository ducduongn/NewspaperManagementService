package com.example.springsecuritydemo.scheduler;

import com.example.springsecuritydemo.constant.crawler.URLConstant;
import com.example.springsecuritydemo.messaging.rabbitmq.MQSender;
import com.example.springsecuritydemo.models.articles.Category;
import com.example.springsecuritydemo.repository.CategoryRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author ducduongn
 */

@Slf4j
@NoArgsConstructor
@Service
public class WebCrawler {
    @Value("${app.crawler.page-num-to-crawl}")
    private int pageNumToCrawl;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MQSender mqSender;

    @Scheduled(cron = "${interval-in-cron-article}")
    public void crawData() {
        crawlAllArticlesFromALlCategories();
    }

    @PostConstruct
    public void crawlCategories() {
        try {
            File file = new File(URLConstant.htmlPath);
            Document document = Jsoup.parse(file, "UTF-8");

            Elements menuElement = document.select("li a");

            menuElement.forEach(e -> {
                String title = e.attr("title");
                String url = e.attr("abs:href");

                if (!url.contains("javascript") &&
                        !categoryRepository.existsCategoryByName(title)) {
                    categoryRepository.save(Category.builder()
                            .name(title)
                            .url(url)
                            .build());
                }
            });
        } catch (IOException e) {
            log.error("Category IO  exception!");
        }
    }

    public void crawlAllArticlesFromALlCategories() {
        List<Category> categoryList = categoryRepository.findAll();

        for(Category category : categoryList) {
            if ( category.getUrl().length() > 0 &&
                    !category.getUrl().contains("video") &&
                !category.getUrl().contains("podcast")) {
                extractArticleUrl(category.getUrl());
            }
        }
    }


    public void extractArticleUrl(String categoryUrl) {
        try {
            Document document = Jsoup.connect(categoryUrl).get();

            Elements articlesHtmlTags = document.select("article.item-news");

            for (Element tag : articlesHtmlTags) {
                Element titleNews = tag.selectFirst(".title-news > a");

                if (titleNews != null) {
                    String articleUrl = titleNews.attr("abs:href");

                    if (articleUrl.contains(URLConstant.VN_EXPRESS_HOME)) {
                        mqSender.send(articleUrl);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error in extracting categoryUrl");
        }

        checkIfHaveNextPage(categoryUrl);
    }

    public void checkIfHaveNextPage(String currentUrl) {
        try {
            Document document = Jsoup.connect(currentUrl).get();
            //Crawl other pages of the category
            Element currentPageBtn = document
                    .select("#pagination .btn-page.active").first();
            Element nextBtn = document
                    .select("#pagination .btn-page.next-page ").first();
            if (currentPageBtn != null && nextBtn != null) {
                int currentPageNum = Integer.parseInt(currentPageBtn.text());

                if (currentPageNum <= pageNumToCrawl) {
                    log.info(currentPageNum + ": " + currentUrl);
                    String nextPageUrl = nextBtn.attr("abs:href");
                    extractArticleUrl(nextPageUrl);
                }
            }
        } catch (IOException exception) {
            log.error("Error in crawlOther page!");
        }
    }
}
