package com.example.springsecuritydemo.models.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ducduongn
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultQuery {
    private Float timeTook;
    private Integer numberOfResults;
    private String elements;
}
