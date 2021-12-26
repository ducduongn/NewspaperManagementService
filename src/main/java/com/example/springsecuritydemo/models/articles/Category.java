package com.example.springsecuritydemo.models.articles;

import com.example.springsecuritydemo.models.auth.User;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * @author ducduongn
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private Set<Article> articles;
}
