package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.CategoryCommand;
import guru.springframework.spring5recipeapp.converters.CategoryCommandToCategory;
import guru.springframework.spring5recipeapp.converters.CategoryToCategoryCommand;
import guru.springframework.spring5recipeapp.domain.Category;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CategoryToCategoryCommand categoryToCategoryCommand;

    @Mock
    CategoryCommandToCategory categoryCommandToCategory;

    @InjectMocks
    CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getCategories(){
        Category cat1 = new Category();
        cat1.setId(1L);
        Category cat2 = new Category();
        cat2.setId(2L);
        Set<Category> categoriesData = new HashSet<>(){{add(cat1); add(cat2);}};

        when(categoryService.getCategories()).thenReturn(categoriesData);

        Set<Category> categories = categoryService.getCategories();

        assertEquals(categories.size(), 2);
        verify(categoryRepository).findAll();
    }

    @Test
    void findCategoriesByRecipeId(){
        Category category = new Category();
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        category.getRecipes().add(recipe);

        Set<Category> categoriesData = new HashSet<>();
        categoriesData.add(category);

        when(categoryService.getCategories()).thenReturn(categoriesData);

        Set<Category> categories = categoryService.findCategoriesByRecipeId(1L);

        assertEquals(categories.size(), categoriesData.size());
        verify(categoryRepository).findAll();
    }
}