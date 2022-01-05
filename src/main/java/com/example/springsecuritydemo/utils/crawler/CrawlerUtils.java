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

    public static String getArticleTitle(String articleUrl) {
        try {
            Document document = Jsoup.connect(articleUrl).get();

            Element articleElement = document.select(".title-detail").first();

            if (articleElement != null) {
                return articleElement.text();
            }

        } catch (IOException exception) {
            log.error("Error reading document url in get article url!");
        }
        return null;
    }

    public static String getArticleContent(String articleUrl) {
        try {
            Document document = Jsoup.connect(articleUrl).get();

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

    public static String getAuthor(String categoryUrl) {
        try {
            Document document = Jsoup.connect(categoryUrl).get();

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

    public static String getDescription(String categoryUrl) {
        try {
            Document document = Jsoup.connect(categoryUrl).get();

            Element descriptionEle = document.select(".description").first();

            if (descriptionEle != null) {
               return descriptionEle.ownText();
            }

        } catch (IOException exception) {
            log.error("Error reading document url in getDescription!");
        }
        return null;
    }

    public static String getTimeTag(String categoryUrl) {
        try {
            Document document = Jsoup.connect(categoryUrl).get();

            Element timeTagEle = document.select(".date").first();

            if (timeTagEle != null && timeTagEle.text().contains("GMT+7")) {
               return timeTagEle.text().trim();
            }

        } catch (IOException exception) {
            log.error("Error reading document url in getTimeTag!");
        }
        return null;
    }

    public static List<String> collectCategoriesUrlFromArticle(String categoryUrl) {
        List<String> categoryUrlList = new ArrayList<>();

        try {
            Document document = Jsoup.connect(categoryUrl).get();

            Elements categoryElements= document.select((".breadcrumb li a"));

            for (Element element:categoryElements) {
                String url = element.attr("abs:href").trim();

                categoryUrlList.add(url);
            }

        } catch (IOException e) {
            log.error("Error collect catogories!");
        }

        return categoryUrlList;
    }
}

