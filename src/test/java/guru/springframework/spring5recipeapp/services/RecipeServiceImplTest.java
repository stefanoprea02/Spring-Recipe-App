package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.converters.RecipeToRecipeCommand;
import guru.springframework.spring5recipeapp.domain.Recipe;
import guru.springframework.spring5recipeapp.exceptions.NotFoundException;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {
    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @InjectMocks
    RecipeServiceImpl recipeService;

    @BeforeEach
    public void setUp(){
        //recipeService = new RecipeServiceImpl(recipeRepository);
    }

    @Test
    void getRecipesByIdTest(){
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        Recipe recipeReturned = recipeService.findById(1L);

        assertNotNull(recipeReturned, "Null recipe returned");
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void getRecipeByIdNotFound() throws Exception{
        Optional<Recipe> recipeOptional = Optional.empty();

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        assertThrows(NotFoundException.class, () -> {
            Recipe recipeReturned = recipeService.findById(1L);
        });
    }

    @Test
    public void getRecipeCommandByIdTest() throws Exception{
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand commandById = recipeService.findCommandById(1L);

        assertNotNull(commandById, "Null recipe returned");
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository, never()).findAll();

    }

    @Test
    void getRecipesTest(){
        Recipe recipe = new Recipe();
        HashSet recipesData = new HashSet();
        recipesData.add(recipe);

        when(recipeService.getRecipes()).thenReturn(recipesData);

        Set<Recipe> recipes = recipeService.getRecipes();

        assertEquals(recipes.size(), 1);
        verify(recipeRepository).findAll();
        verify(recipeRepository, never()).findById(anyLong());
    }

    @Test
    void testDeleteById(){
        Long idToDelete = Long.valueOf(2L);
        recipeService.deleteById(idToDelete);

        verify(recipeRepository).deleteById(anyLong());
    }
}