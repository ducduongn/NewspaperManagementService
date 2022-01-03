package com.example.springsecuritydemo.scheduler;

import com.example.springsecuritydemo.constant.crawler.URLConstant;
import com.example.springsecuritydemo.messaging.rabbitmq.MQSender;
import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.articles.Category;
import com.example.springsecuritydemo.models.dto.ArticleDto;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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

//    @Autowired
//    private ArticleRepository articleRepository;

    @Autowired
    private MQSender mqSender;

    @Scheduled(cron = "${interval-in-cron-article}")
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
            if (!category.getUrl().contains("video") &&
                !category.getUrl().contains("podcast")) {
                crawlArticle(category.getUrl(), pageNumToCrawl);
            }
        }
    }

    public void crawlArticle(String url, int pageNum) {
        try {
            Document document = Jsoup.connect(url).get();

            Elements articlesHtmlTags = document.select("article.item-news");

            //Crawl all articles of one page of a category
            for (Element tag : articlesHtmlTags) {
                Article article = new Article();
                ArticleDto articleDto = new ArticleDto();

                Element titleNews = tag.selectFirst(".title-news > a");

                if (titleNews != null) {
                    articleDto.setUrl(titleNews.attr("abs:href"));
                    articleDto.setTitle(titleNews.text());
                } else {
                    continue;
                }

                if (articleDto.getUrl().contains(URLConstant.VN_EXPRESS_HOME)) {
                    getArticleContent(articleDto);

                    mqSender.send(articleDto);
                }
                crawlOtherPage(document, url, pageNum);
            }

        } catch (IOException e) {
            log.error("IO  exception!");
        }
    }

    public void crawlOtherPage(Document document, String currentUrl, int pageNum) {
        //Crawl other pages of the category
        Element currentPageBtn = document
                .select("#pagination .btn-page.active").first();
        Element nextBtn = document
                .select("#pagination .btn-page.next-page ").first();
        if (currentPageBtn != null && nextBtn != null) {
            int currentPageNum = Integer.parseInt(currentPageBtn.text());

            if (currentPageNum <= pageNum) {
                log.info(currentPageNum + ": " + currentUrl);
                String nextPageUrl = nextBtn.attr("abs:href");
                crawlArticle(nextPageUrl, pageNum);
            }
        }
    }

    public void getArticleContent(ArticleDto articleDto) {
        try {
            Document document = Jsoup.connect(articleDto.getUrl()).get();

            StringBuilder articleContent = new StringBuilder();

            Elements contentParagraphs = document.select("article p");

            Element descriptionEle = document.select(".description").first();

            Elements categoryElements= document.select((".breadcrumb li a"));

            Element timeTagEle = document.select(".date").first();

            //Set description for crawled article
            if (descriptionEle != null) {
                articleDto.setDescription(descriptionEle.ownText());
            }

            if (timeTagEle != null && timeTagEle.text().contains("GMT+7")) {
                articleDto.setStringPostedDate(timeTagEle.text().trim());
            }

            //set content for crawled articles
            for (int i = 0; i < contentParagraphs.size(); i++) {
                if (contentParagraphs.get(i).hasText()) {
                    articleContent.append(contentParagraphs.get(i).text());
                    articleContent.append("\n");
                }
                if (i == contentParagraphs.size() - 1) {
                    if (contentParagraphs.get(i).hasText()) {
                        articleDto.setAuthor(contentParagraphs.get(i).text());
                    } else {
                        articleDto.setAuthor(contentParagraphs.get(i-1).text());
                    }
                }
            }
            articleDto.setContent(articleContent.toString());

            //Mapping categories
            List<String> urlCategoryList = CrawlerUtils.getCategoryUrlListFromElement(categoryElements);

            List<Category> categories = new ArrayList<>();

            log.info("Url list: " + urlCategoryList);

            for(String categoryUrl:urlCategoryList) {
                Category category = categoryRepository.findByUrl(categoryUrl)
                        .orElse(new Category());


                if (category.getId() != null) {
                    log.info("Category: " + category.getId() + "-" + category.getName());
                    categories.add(category);
                }
            }
            articleDto.setCategories(categories);

        } catch (IOException e) {
            log.error("Error connecting to url!");
        } catch (NullPointerException e) {
            log.error("Null pointer!");
        } catch (DateTimeParseException e) {
            log.error("Error parsing DateTime!");
        } catch (IllegalArgumentException e) {
            log.error("Malformed URL!");
        }
    }
}
