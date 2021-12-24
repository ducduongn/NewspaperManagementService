package com.example.springsecuritydemo.repository.category;

import com.example.springsecuritydemo.models.articles.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author ducduongn
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();

    Optional<Category> findById(Long aLong);

    Optional<Category> findByName(String name);

    Boolean existsCategoryByName(String name);
}
