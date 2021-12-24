package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.models.articles.Category;
import com.example.springsecuritydemo.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ducduongn
 */
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories() {
        List<Category> categoryList = categoryService.findAll();

        if (categoryList.size() > 0) {
            return ResponseEntity.ok(
                    categoryList
            );
        }
        return ResponseEntity.ok("There is no category");
    }
}
