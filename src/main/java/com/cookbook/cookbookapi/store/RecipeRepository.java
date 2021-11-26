package com.cookbook.cookbookapi.store;

import com.cookbook.cookbookapi.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findRecipeByCategory(String category);
}
