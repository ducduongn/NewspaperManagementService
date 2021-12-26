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

            Element articleElement = document.select("article.fck_detail").first();

            Elements contentParagraphs = document.select("article p");

            Element descriptionEle = document.select(".description").first();

            if (descriptionEle != null) {
                article.setDescription(descriptionEle.ownText());
            }

            for (int i = 0; i < contentParagraphs.size(); i++) {
                if (contentParagraphs.get(i).hasText()) {
                    articleContent.append(contentParagraphs.get(i).text());
                    articleContent.append("\n");
                }
                if (i == contentParagraphs.size() - 1) {
                    if (contentParagraphs.get(i).hasText()) {
                        article.setAuthor(contentParagraphs.get(i).text());
                    } else {
                        article.setAuthor(contentParagraphs.get(i-1).text());
                    }
                }
            }

            article.setContent(articleContent.toString());


        } catch (IOException e) {
            log.error("Error connecting to url!");
        } catch (NullPointerException e) {
            log.error("Author is null!");
        }
    }
}

