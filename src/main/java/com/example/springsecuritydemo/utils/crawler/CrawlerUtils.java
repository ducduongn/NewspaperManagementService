package com.example.springsecuritydemo.utils.crawler;

import com.example.springsecuritydemo.models.articles.Article;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author ducduongn
 */
@Slf4j
public class CrawlerUtils {
    public static void getArticleContent(Article article) {
        try {
            Document document = Jsoup.connect(article.getUrl()).get();

            StringBuilder articleContent = new StringBuilder();

            Elements contentParagraphs = document.select("article > p");

            for (Element paragraph: contentParagraphs) {
                if (paragraph.hasText()) {
                    articleContent.append(paragraph.text());
                    articleContent.append("\n");
                }
            }

            article.setContent(articleContent.toString());

        } catch (IOException e) {
            log.error("Error connecting to url!");
        }
    }
}

