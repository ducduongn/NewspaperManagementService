package com.example.springsecuritydemo.service.category;

import com.example.springsecuritydemo.models.articles.Category;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ducduongn
 */
@Service
public interface CategoryService {
    List<Category> findAll();

    Category findByName(String name);
}
