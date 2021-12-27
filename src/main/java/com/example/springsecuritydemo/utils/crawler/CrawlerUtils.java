package com.example.springsecuritydemo.utils.crawler;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.repository.CategoryRepository;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class CrawlerUtils {
    private CategoryRepository categoryRepository;

    @Autowired
    public CrawlerUtils(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public static List<String> getCategoryUrlListFromElement(Elements categoryElements) {
        List<String> urlList = new ArrayList<>();

        for (Element element:categoryElements) {
            urlList.add(element.attr("abs:href").trim());
        }

        return urlList;
    }

    public static String extractDateTieFromElement(Element element) {
        String originalDateTimeString = element.text();

        String[] strings = originalDateTimeString.split(", ");

        StringBuilder dateTimeString = new StringBuilder();

        for (int i = 1; i < strings.length; i++) {
            dateTimeString.append(strings[i] + " ");
        }
        return dateTimeString.toString()
                .replace("(GMT+7)", "").trim()
                .concat(":00")
                .replace("/", "-");
    }
}

