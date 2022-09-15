package guru.springframework.spring5recipeapp.controllers;

import guru.springframework.spring5recipeapp.commands.IngredientCommand;
import guru.springframework.spring5recipeapp.commands.RecipeCommand;
import guru.springframework.spring5recipeapp.commands.UnitOfMeasureCommand;
import guru.springframework.spring5recipeapp.services.IngredientService;
import guru.springframework.spring5recipeapp.services.RecipeService;
import guru.springframework.spring5recipeapp.services.UnitOfMeasureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class IngredientController {

    private final RecipeService recipeService;

    private final IngredientService ingredientService;

    private final UnitOfMeasureService unitOfMeasureService;


    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("/recipe/{id}/ingredients")
    public String listIngredients(@PathVariable String id, Model model){
        RecipeCommand command = recipeService.findCommandById(Long.valueOf(id));

        model.addAttribute("recipe", command);

        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{id}/ingredient/{id2}/show")
    public String showIngredient(@PathVariable String id, @PathVariable String id2, Model model){
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndId(Long.valueOf(id), Long.valueOf(id2));

        model.addAttribute("ingredient", ingredientCommand);

        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/new")
    public String newRecipe(@PathVariable String recipeId, Model model){
        //make sure we have a good id value
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));

        //need to ret back parent id for hidden form property
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.valueOf(recipeId));
        model.addAttribute("ingredient", ingredientCommand);

        //init uom
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";

    }

    @GetMapping
    @RequestMapping("/recipe/{id}/ingredient/{id2}/update")
    public String updateIngredient(@PathVariable String id, @PathVariable String id2, Model model){
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndId(Long.valueOf(id), Long.valueOf(id2));


        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @PostMapping
    @RequestMapping("/recipe/{id}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command, @PathVariable String id){
        command.setRecipeId(Long.valueOf(id));
        IngredientCommand ingredientCommand = ingredientService.saveIngredientCommand(command);

        return "redirect:/recipe/" + ingredientCommand.getRecipeId() + "/ingredient/" + ingredientCommand.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/delete")
    public String delete(@PathVariable String recipeId, @PathVariable String id){

        ingredientService.deleteById(Long.valueOf(recipeId), Long.valueOf(id));

        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
