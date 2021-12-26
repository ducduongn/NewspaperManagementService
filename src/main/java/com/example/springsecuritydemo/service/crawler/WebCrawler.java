package com.example.springsecuritydemo.service.crawler;

import com.example.springsecuritydemo.constant.cralwer.URLConstant;
import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.articles.Category;
import com.example.springsecuritydemo.repository.ArticleRepository;
import com.example.springsecuritydemo.repository.CategoryRepository;
import com.example.springsecuritydemo.utils.crawler.CrawlerUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CategoryRepository categoryRepository;

    private ArticleRepository articleRepository;

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void crawData() {
        crawlCategories();
        crawlAllArticlesFromALlCategories();
    }

    public void crawlCategories() {
        try {
            File file = new File(URLConstant.htmlPath);
            Document document = Jsoup.parse(file, "UTF-8", URLConstant.VN_EXPRESS_HOME);

            Elements menuElement = document.select("li a");

            menuElement.forEach(e -> {
                String title = e.attr("title");
                String url = e.attr("abs:href");

//                log.info(title + ": " + url);

                if (!url.contains("javascript") &&
                        !categoryRepository.existsCategoryByName(title)) {
                    categoryRepository.save(Category.builder()
                            .name(title)
                            .url(url)
                            .build());
                }
            });
        } catch (IOException e) {
            log.error("IO  exception!");
        }
    }

    public void crawlAllArticlesFromALlCategories() {
        List<Category> categoryList = categoryRepository.findAll();

        for(Category category : categoryList) {
            crawlArticle(category.getUrl(), 6);
        }
    }

    public void crawlArticle(String url, int pageNum) {
        try {
            Document document = Jsoup.connect(url).get();

            Elements articlesHtmlTags = document.select("article.item-news");

            for (Element tag : articlesHtmlTags) {
                Article article = new Article();

                Element titleNews = tag.selectFirst(".title-news > a");

                if (titleNews != null) {
                    article.setUrl(titleNews.attr("abs:href"));
                    article.setTitle(titleNews.text());
                } else {
                    continue;
                }

                if (article.getUrl().contains(URLConstant.VN_EXPRESS_HOME)) {
                    CrawlerUtils.getArticleContent(article);

//                    log.info(article.toString());

                    if (!articleRepository.existsByUrl(article.getUrl())) {
                        articleRepository.save(article);
                    }
                }
            }

            Element currentPageBtn = document
                    .select("#pagination .btn-page.active").first();
            Element nextBtn = document
                    .select("#pagination .btn-page.next-page ").first();
            if (currentPageBtn != null && nextBtn != null) {
                int currentPageNum = Integer.parseInt(currentPageBtn.text());

                if (currentPageNum <= pageNum) {
                    log.info(currentPageNum + ": " + url);
                    String nextPageUrl = nextBtn.attr("abs:href");
                    crawlArticle(nextPageUrl, pageNum);
                }
            }

        } catch (IOException e) {
            log.error("IO  exception!");
        }
    }


    public static void main(String[] args) {
//        crawlArticle("https://vnexpress.net/thoi-su");
    }
}
