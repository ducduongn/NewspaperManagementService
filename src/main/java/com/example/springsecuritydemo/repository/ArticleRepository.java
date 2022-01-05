package com.example.springsecuritydemo.repository;

import com.example.springsecuritydemo.models.articles.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author ducduongn
 */

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAll();

    Optional<Article> findById(Long aLong);

    Optional<Article> findByUrl(String url);

    Boolean existsByTitle(String title);

    Boolean existsByUrl(String url);
}
