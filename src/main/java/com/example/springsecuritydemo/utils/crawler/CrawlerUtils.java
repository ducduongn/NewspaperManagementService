package com.example.springsecuritydemo.utils.crawler;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.articles.Category;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ducduongn
 */
@Slf4j
public class CrawlerUtils {

    public static String getArticleContent(Article article) {
        try {
            Document document = Jsoup.connect(article.getUrl()).get();

            StringBuilder articleContent = new StringBuilder();

            Elements contentParagraphs = document.select("article p");

            //set content for crawled articles
            for (Element contentParagraph : contentParagraphs) {
                if (contentParagraph.hasText()) {
                    articleContent.append(contentParagraph.text());
                    articleContent.append("\n");
                }
            }

            return articleContent.toString();
        } catch (IOException exception) {
            log.error("Error reading document url in getArticleContent!");
        }
        return null;
    }

    public static String getAuthor(Article article) {
        try {
            Document document = Jsoup.connect(article.getUrl()).get();

            Elements contentParagraphs = document.select("article p");

            //set content for crawled articles
            for (int i = 0; i < contentParagraphs.size(); i++) {
                if (i == contentParagraphs.size() - 1) {
                    if (contentParagraphs.get(i).hasText()) {
                        return contentParagraphs.get(i).text();
                    } else {
                       return contentParagraphs.get(i-1).text();
                    }
                }
            }
        } catch (IOException exception) {
            log.error("Error reading document url in getAuthor!");
        }
        return null;
    }

    public static String getDescription(Article article) {
        try {
            Document document = Jsoup.connect(article.getUrl()).get();

            Element descriptionEle = document.select(".description").first();

            if (descriptionEle != null) {
               return descriptionEle.ownText();
            }

        } catch (IOException exception) {
            log.error("Error reading document url in getDescription!");
        }
        return null;
    }

    public static String getTimeTag(Article article) {
        try {
            Document document = Jsoup.connect(article.getUrl()).get();

            Element timeTagEle = document.select(".date").first();

            if (timeTagEle != null && timeTagEle.text().contains("GMT+7")) {
               return timeTagEle.text().trim();
            }

        } catch (IOException exception) {
            log.error("Error reading document url in getTimeTag!");
        }
        return null;
    }

    public static List<String> collectCategoriesUrlFromArticle(Article article) {
        List<String> categoryUrl = new ArrayList<>();

        try {
            Document document = Jsoup.connect(article.getUrl()).get();

            Elements categoryElements= document.select((".breadcrumb li a"));

            for (Element element:categoryElements) {
                String url = element.attr("abs:href").trim();

                categoryUrl.add(url);
            }

        } catch (IOException e) {
            log.error("Error collect catogories!");
        }

        return categoryUrl;
    }
}

