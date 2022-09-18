package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.CategoryCommand;
import guru.springframework.spring5recipeapp.converters.CategoryCommandToCategory;
import guru.springframework.spring5recipeapp.converters.CategoryToCategoryCommand;
import guru.springframework.spring5recipeapp.domain.Category;
import guru.springframework.spring5recipeapp.domain.Ingredient;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.repositories.CategoryRepository;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService{

    private final CategoryCommandToCategory categoryCommandToCategory;
    private final CategoryToCategoryCommand categoryToCategoryCommand;
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;

    public CategoryServiceImpl(CategoryCommandToCategory categoryCommandToCategory, CategoryToCategoryCommand categoryToCategoryCommand, CategoryRepository categoryRepository, RecipeRepository recipeRepository) {
        this.categoryCommandToCategory = categoryCommandToCategory;
        this.categoryToCategoryCommand = categoryToCategoryCommand;
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public CategoryCommand saveCategoryCommand(CategoryCommand categoryCommand) {
        Category detachedCategory = categoryCommandToCategory.convert(categoryCommand);
        Category savedCategory = categoryRepository.save(detachedCategory);

        return categoryToCategoryCommand.convert(savedCategory);
    }

    @Override
    public Set<Category> getCategories() {
        Set<Category> categories = new HashSet<>();

        categoryRepository.findAll().iterator().forEachRemaining(categories::add);

        return categories;
    }

    @Override
    public Set<Category> findCategoriesByRecipeId(Long recipeId) {
        Set<Category> allCategories = this.getCategories();
        Set<Category> categories = new HashSet<>();

        for (Category category : allCategories){
            for(Recipe recipe : category.getRecipes()){
                if(recipe.getId() == recipeId) {
                    categories.add(category);
                    break;
                }
            }
        }

        return categories;
    }

    @Override
    public Set<Category> findCategoriesWithoutRecipeId(Long recipeId){
        Set<Category> allCategories = this.getCategories();
        Set<Category> categories = new HashSet<>();

        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        Recipe recipe = new Recipe();

        if(optionalRecipe.isPresent()){
            recipe = optionalRecipe.get();
        }

        for(Category category : allCategories){
            if(!category.getRecipes().contains(recipe)) {
                categories.add(category);
            }
        }

        return categories;
    }

    @Override
    public CategoryCommand addCategoryCommand(Long aLong, CategoryCommand categoryCommand){
        Optional<Recipe> optionalRecipe = recipeRepository.findById(aLong);

        if(!optionalRecipe.isPresent()){
            log.debug("Recipe not found!");
        }

        Set<Category> categories = new HashSet<>();
        categoryRepository.findAll().iterator().forEachRemaining(categories::add);

        Recipe recipe = optionalRecipe.get();
        Category category = categories.iterator().next();
        for(Category category1 : categories){
            if(category1.getId() == categoryCommand.getId()){
                category = category1;
                break;
            }
        }
        recipe.getCategories().add(category);
        category.getRecipes().add(recipe);

        Recipe savedRecipe = recipeRepository.save(recipe);

        return categoryCommand;
    }
}
