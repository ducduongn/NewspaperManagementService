package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.models.es.ArticleEsModel;
import com.example.springsecuritydemo.models.dto.ArticleDto;
import com.example.springsecuritydemo.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ducduongn
 */
@RestController
@RequestMapping("/api/v1/search/")
@Slf4j
public class SearchController {
    private SearchService searchService;

    private ModelMapper modelMapper;

    @Autowired
    public void setService(SearchService searchService) {
        this.searchService = searchService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    @GetMapping("/articles")
    List<ArticleDto> searchByTitle(@RequestParam(name = "query") String query) {
        log.info("Search article by query: " + query);

        List<ArticleEsModel> articleEsModels = searchService.processSearchQuery(query);
//        log.info("Search article results: " + articleEsModels);

        List<ArticleDto> articleDtos = new ArrayList<>();

        articleEsModels.forEach(esModel -> {
            articleDtos.add(modelMapper.map(esModel, ArticleDto.class));
            log.info(esModel.getTitle());
        });

        return articleDtos;
    }
}

