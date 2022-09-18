package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.CategoryCommand;
import guru.springframework.spring5recipeapp.domain.Category;
import guru.springframework.spring5recipeapp.domain.Recipe;

import java.util.Set;

public interface CategoryService {
    Set<Category> findCategoriesByRecipeId(Long recipeId);
    Set<Category> findCategoriesWithoutRecipeId(Long recipeId);
    Set<Category> getCategories();
    CategoryCommand addCategoryCommand(Long aLong, CategoryCommand categoryCommand);
    CategoryCommand saveCategoryCommand(CategoryCommand categoryCommand);
}
