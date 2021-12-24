package com.example.springsecuritydemo.controller.crawl;

import com.example.springsecuritydemo.service.crawler.WebCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author ducduongn
 */

public class CrawlController {
    private WebCrawler webCrawler;

    public void setWebCrawler(WebCrawler webCrawler) {
        this.webCrawler = webCrawler;
    }

}
