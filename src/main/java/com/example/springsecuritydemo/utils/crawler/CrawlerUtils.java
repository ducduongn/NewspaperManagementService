package com.example.springsecuritydemo.utils.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

