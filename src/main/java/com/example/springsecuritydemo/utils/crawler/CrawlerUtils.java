package com.example.springsecuritydemo.utils.crawler;

import com.example.springsecuritydemo.models.dto.ArticleDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ducduongn
 */
@Slf4j
public class CrawlerUtils {

    public static List<String> getCategoryUrlListFromElement(Elements categoryElements) {
        List<String> urlList = new ArrayList<>();

        for (Element element:categoryElements) {
            urlList.add(element.attr("abs:href").trim());
        }

        return urlList;
    }

    public static String extractDateTimeFromElement(String dateTimeString) {

        String[] strings = dateTimeString.split(", ");

        StringBuilder newDateTimeString = new StringBuilder();

        for (int i = 1; i < strings.length; i++) {
            newDateTimeString.append(strings[i] + " ");
        }
        return newDateTimeString.toString()
                .replace("(GMT+7)", "").trim()
                .concat(":00")
                .replace("/", "-");
    }

    public static String getArticleContent(ArticleDto articleDto) {
        try {
            Document document = Jsoup.connect(articleDto.getUrl()).get();

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
            log.info("Error reading document url in getArticleContent!");
        }
        return null;
    }

    public static String getAuthor(ArticleDto articleDto) {
        try {
            Document document = Jsoup.connect(articleDto.getUrl()).get();

            StringBuilder articleContent = new StringBuilder();

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
            log.info("Error reading document url in getAuthor!");
        }
        return null;
    }
}

