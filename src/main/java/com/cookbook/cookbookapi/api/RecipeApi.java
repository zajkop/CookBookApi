package com.cookbook.cookbookapi.api;

import com.cookbook.cookbookapi.domain.Recipe;
import com.cookbook.cookbookapi.domain.UserData;
import com.cookbook.cookbookapi.store.RecipeRepository;
import com.cookbook.cookbookapi.store.UserRepository;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;
import static java.util.Arrays.asList;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("recipe")
@RequiredArgsConstructor
class RecipeApi {

    private final MinioClient minioClient;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @GetMapping("/all")
    List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    @PostMapping("/user/connected-meals")
    List<Recipe> findRecipesByIds(@RequestBody Object jsonRequest) {
        String idParam = String.valueOf(((LinkedHashMap) ((ArrayList) jsonRequest).get(0)).get("id"));
        List<String> ids = asList(idParam.split(","));

        List<Recipe> result = new ArrayList<>();
        ids.forEach(id -> result.add(recipeRepository.findById(parseInt(id)).orElseThrow()));
        return result;
    }

    @PostMapping("/user/register")
    UserData register(@RequestBody Object jsonRequest) {
        Map<String, String> params = ((LinkedHashMap) jsonRequest);
        String userLogin = params.get("username");
        String userPassword = params.get("password");

        UserData userData = new UserData();
        userData.setLogin(userLogin);
        userData.setPassword(userPassword);
        List<String> meals = new ArrayList<>();
        meals.add("1");
        userData.setMeals(meals);

        return userRepository.save(userData);
    }

    @GetMapping(value = "/user/{id}")
    List<Recipe> getUserMeals(@PathVariable String id) {
        List<Recipe> result = new ArrayList<>();
        UserData userData = userRepository.findById(valueOf(id)).orElseThrow();
        userData.getMeals().forEach(mealId -> {
            Recipe recipe = recipeRepository.findById(valueOf(mealId)).orElseThrow();
            result.add(recipe);
        });
        return result;
    }

    @PostMapping("/user/remove-meal")
    UserData removeMeal(@RequestBody Object jsonRequest) {
        Map<String, String> params = ((LinkedHashMap) jsonRequest);
        UserData user = userRepository.getById(valueOf(params.get("userId")));
        List<String> helper = new ArrayList<>(user.getMeals());
        helper.remove(params.get("mealId"));
        user.setMeals(helper);
        return userRepository.save(user);
    }

    @PostMapping("/user/add-meal")
    UserData addMeal(@RequestBody Object jsonRequest) {
        Map<String, String> params = ((LinkedHashMap) jsonRequest);
        UserData user = userRepository.getById(valueOf(params.get("userId")));
        List<String> helper = new ArrayList<>(user.getMeals());
        helper.add(params.get("mealId"));
        user.setMeals(helper);
        return userRepository.save(user);
    }

    @GetMapping("/breakfast")
    List<Recipe> getBreakfastRecipes() {
        return recipeRepository.findRecipeByCategory("Sniadanie");
    }

    @GetMapping("/lunch")
    List<Recipe> getLunches() {
        return recipeRepository.findRecipeByCategory("Obiad");
    }

    @GetMapping("dinner")
    List<Recipe> getDinners() {
        return recipeRepository.findRecipeByCategory("Kolacja");
    }

    @PostMapping(value = "/login")
    UserData login(@RequestBody Object jsonRequest) {
        Map<String, String> params = ((LinkedHashMap) jsonRequest);
        return userRepository.findUserByLoginAndPassword(params.get("login"), params.get("password"));
    }

    @SneakyThrows
    @GetMapping(value = "/{imageName}", produces = {"application/octet-stream"})
    byte[] getImageByName(@PathVariable String imageName) {
        return minioClient.getObject("test", imageName).readAllBytes();
    }
}
