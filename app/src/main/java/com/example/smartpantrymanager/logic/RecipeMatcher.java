package com.example.smartpantrymanager.logic;

import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class RecipeMatcher {

    private static final double EPSILON = 1e-9;

    private RecipeMatcher() {}

    // only recipes where every ingredient is covered by the pantry
    public static List<Recipe> getSuggestedRecipes(List<Recipe> recipes, List<PantryItem> pantry) {
        Map<String, Double> available = buildAvailability(pantry);
        List<Recipe> result = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (canMake(recipe, available)) {
                result.add(recipe);
            }
        }
        return result;
    }

    public static boolean canMake(Recipe recipe, List<PantryItem> pantry) {
        return canMake(recipe, buildAvailability(pantry));
    }

    private static boolean canMake(Recipe recipe, Map<String, Double> available) {
        List<RecipeIngredient> needed = recipe.getIngredients();
        if (needed.isEmpty()) {
            return false;
        }
        for (RecipeIngredient ing : needed) {
            Double have = available.get(key(ing.getName(), ing.getUnit()));
            double need = toBaseQuantity(ing.getQuantity(), ing.getUnit());
            // one missing or short ingredient rules the recipe out
            if (have == null || have < need - EPSILON) {
                return false;
            }
        }
        return true;
    }

    // adds up the pantry per ingredient, converted to base units
    private static Map<String, Double> buildAvailability(List<PantryItem> pantry) {
        Map<String, Double> totals = new HashMap<>();
        for (PantryItem p : pantry) {
            String k = key(p.getName(), p.getUnit());
            double qty = toBaseQuantity(p.getQuantity(), p.getUnit());
            Double current = totals.get(k);
            totals.put(k, (current == null ? 0 : current) + qty);
        }
        return totals;
    }

    private static String key(String name, String unit) {
        return normalizeName(name) + "|" + unitFamily(unit);
    }

    // lowercase, trim and drop plural endings so "Tomatoes" matches "tomato"
    static String normalizeName(String raw) {
        if (raw == null) return "";
        String n = raw.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        if (n.length() <= 3) return n;
        if (n.endsWith("ies")) return n.substring(0, n.length() - 3) + "y";
        if (n.endsWith("oes") || n.endsWith("ches") || n.endsWith("shes")
                || n.endsWith("xes") || n.endsWith("sses")) {
            return n.substring(0, n.length() - 2);
        }
        if (n.endsWith("ss") || n.endsWith("us")) return n;
        if (n.endsWith("s")) return n.substring(0, n.length() - 1);
        return n;
    }

    private static String cleanUnit(String unit) {
        return unit == null ? "" : unit.trim().toLowerCase(Locale.ROOT);
    }

    // weight, volume and count can't be compared with each other
    private static String unitFamily(String unit) {
        switch (cleanUnit(unit)) {
            case "g": case "gram": case "grams":
            case "kg": case "kilogram": case "kilograms":
                return "weight";
            case "ml": case "l": case "litre": case "litres": case "liter": case "liters":
            case "cup": case "cups": case "tbsp": case "tsp":
                return "volume";
            default:
                return "count";
        }
    }

    // converts to grams, millilitres or pieces
    private static double toBaseQuantity(double qty, String unit) {
        switch (cleanUnit(unit)) {
            case "kg": case "kilogram": case "kilograms": return qty * 1000;
            case "l": case "litre": case "litres": case "liter": case "liters": return qty * 1000;
            case "cup": case "cups": return qty * 250;
            case "tbsp": return qty * 15;
            case "tsp": return qty * 5;
            default: return qty;
        }
    }
}