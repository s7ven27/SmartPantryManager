package com.example.smartpantrymanager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.smartpantrymanager.logic.RecipeMatcher;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RecipeMatcherTest {

    private Recipe recipe(RecipeIngredient... ings) {
        Recipe r = new Recipe(1, "Test", "steps");
        r.setIngredients(Arrays.asList(ings));
        return r;
    }

    @Test
    public void allIngredientsPresent_isSuggested() {
        Recipe r = recipe(new RecipeIngredient("egg", 3, "pcs"),
                new RecipeIngredient("butter", 10, "g"));
        List<PantryItem> pantry = Arrays.asList(
                new PantryItem(1, "egg", 3, "pcs", ""),
                new PantryItem(2, "butter", 50, "g", ""));
        assertTrue(RecipeMatcher.canMake(r, pantry));
    }

    @Test
    public void oneIngredientMissing_isNotSuggested() {
        Recipe r = recipe(new RecipeIngredient("egg", 3, "pcs"),
                new RecipeIngredient("butter", 10, "g"));
        List<PantryItem> pantry = Arrays.asList(new PantryItem(1, "egg", 3, "pcs", ""));
        assertFalse(RecipeMatcher.canMake(r, pantry));
    }

    @Test
    public void notEnoughQuantity_isNotSuggested() {
        Recipe r = recipe(new RecipeIngredient("flour", 200, "g"));
        List<PantryItem> pantry = Arrays.asList(new PantryItem(1, "flour", 100, "g", ""));
        assertFalse(RecipeMatcher.canMake(r, pantry));
    }

    @Test
    public void pluralAndCaseDifferences_stillMatch() {
        Recipe r = recipe(new RecipeIngredient("tomatoes", 2, "pcs"));
        List<PantryItem> pantry = Arrays.asList(new PantryItem(1, "Tomato", 2, "pcs", ""));
        assertTrue(RecipeMatcher.canMake(r, pantry));
    }

    @Test
    public void unitConversion_kgSatisfiesGrams() {
        Recipe r = recipe(new RecipeIngredient("flour", 200, "g"));
        List<PantryItem> pantry = Arrays.asList(new PantryItem(1, "flour", 1, "kg", ""));
        assertTrue(RecipeMatcher.canMake(r, pantry));
    }
}