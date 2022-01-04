package com.example.springsecuritydemo.messaging.rabbitmq;

import com.example.springsecuritydemo.models.articles.Article;
import com.example.springsecuritydemo.models.articles.Category;
import com.example.springsecuritydemo.models.dto.ArticleDto;
import com.example.springsecuritydemo.repository.ArticleRepository;
import com.example.springsecuritydemo.repository.CategoryRepository;
import com.example.springsecuritydemo.utils.crawler.DateTimeConverter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ducduongn
 */
@Slf4j
@Service
@RabbitListener(queues = "article_queue")
public class MQArticleWorker1 {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @RabbitHandler
    public void receiver(ArticleDto articleDto) {

        Article article = new Article();

        article.setTitle(articleDto.getTitle());
        article.setDescription(articleDto.getDescription());
        article.setContent(articleDto.getContent());
        article.setAuthor(articleDto.getAuthor());
        article.setStringPostedDate(articleDto.getStringPostedDate());

        List<Category> categoryList = new ArrayList<>();

        for (String categoryUrl : articleDto.getCategoriesUrls()) {
            if (categoryRepository.existsCategoryByUrl(categoryUrl)) {
                Category category = categoryRepository.findByUrl(categoryUrl).get();

                categoryList.add(category);
            }
        }
        article.setCategories(categoryList);

//        article.setPostedDate(DateTimeConverter
//                .convertDateTimeStringToLocalDateTime(article.getStringPostedDate()));

        log.info("Receiver has receive a message: " + article.toString());

        if (!articleRepository.existsByUrl(article.getUrl())) {
                        articleRepository.save(article);
                        log.info("Save article successfully: " + article.getTitle());
        } else {
            log.info("Unsuccessfully saved!");
        }
    }
}
