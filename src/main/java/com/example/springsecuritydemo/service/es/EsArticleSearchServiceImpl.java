package com.example.springsecuritydemo.service.es;

import com.example.springsecuritydemo.es.repository.EsArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ducduongn
 */
@Service
@Slf4j
public class EsArticleSearchServiceImpl {
    private EsArticleRepository esArticleRepository;

    @Autowired
    public void setEsArticleRepository(EsArticleRepository esArticleRepository) {
        this.esArticleRepository = esArticleRepository;
    }
}
