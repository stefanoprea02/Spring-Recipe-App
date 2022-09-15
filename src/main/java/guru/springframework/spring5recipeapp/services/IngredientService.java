package guru.springframework.spring5recipeapp.services;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.domain.Ingredient;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndId(Long aLong, Long bLong);

    IngredientCommand saveIngredientCommand(IngredientCommand command);

    void deleteById(Long aLong, Long bLong);
}
