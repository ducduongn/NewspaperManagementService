package com.example.springsecuritydemo.service.crawler;

import com.example.springsecuritydemo.constant.cralwer.URLConstant;
import com.example.springsecuritydemo.models.articles.Category;
import com.example.springsecuritydemo.repository.category.CategoryRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author ducduongn
 */

@Slf4j
@NoArgsConstructor
@Service
public class WebCrawler {
    private CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void crawlCategories() {
        HashMap<String, String> urlCateUrlMap = new HashMap<>();
        try {
            File file = new File(URLConstant.htmlPath);
            Document document = Jsoup.parse(file, "UTF-8", URLConstant.VN_EXPRESS_HOME);

            Elements menuElement = document.select("li a");

            menuElement.forEach(e -> {
                String title = e.attr("title");
                String url = e.attr("abs:href");

                log.info(title + ": " + url);

                if (!url.contains("javascript")) {
                    if (!categoryRepository.existsCategoryByName(title)) {
                        categoryRepository.save(
                                Category.builder().name(title).url(url).build()
                        );
                    }
                }
            });

//            System.out.println(menuElement);
        } catch (IOException e) {
            log.error("IO  exception!");
        }
        System.out.println(urlCateUrlMap);
    }
}
