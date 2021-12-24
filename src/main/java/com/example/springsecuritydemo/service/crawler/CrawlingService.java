package com.example.springsecuritydemo.service.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ducduongn
 */
@Service
public class CrawlingService {
    private WebCrawler crawler;

    @Autowired
    public void setCrawler(WebCrawler crawler) {
        this.crawler = crawler;
    }
}
