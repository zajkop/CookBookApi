package com.cookbook.cookbookapi.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String category;

    private String imageUri;

    @Convert(converter = IngredientsConverter.class)
    private List<String> ingredients;

    @OneToOne
    private Kcal kcal;
}
