package com.example.smartpantrymanager.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/** Pre-loads the recipe collection the first time the database is created. */
final class RecipeSeeder {

    private RecipeSeeder() {}

    static void seed(SQLiteDatabase db) {
        add(db, "Scrambled Eggs",
                "1. Beat the eggs with salt.\n2. Melt butter in a pan on low heat.\n3. Stir the eggs gently until just set.",
                "egg|3|pcs", "butter|10|g", "salt|1|tsp");

        add(db, "Cheese Omelette",
                "1. Beat eggs with salt.\n2. Cook in buttered pan until almost set.\n3. Add cheese, fold and serve.",
                "egg|3|pcs", "cheese|50|g", "butter|10|g", "salt|1|tsp");

        add(db, "Mushroom Omelette",
                "1. Fry sliced mushrooms in butter.\n2. Pour in beaten eggs and salt.\n3. Fold when set.",
                "egg|3|pcs", "mushrooms|100|g", "butter|10|g", "salt|1|tsp");

        add(db, "Spinach Scramble",
                "1. Wilt spinach in oil.\n2. Add beaten eggs and salt.\n3. Stir until set.",
                "egg|3|pcs", "spinach|50|g", "oil|1|tbsp", "salt|0.5|tsp");

        add(db, "Tomato Pasta",
                "1. Boil pasta until tender.\n2. Fry onion and garlic in oil, add chopped tomatoes and salt.\n3. Simmer 15 min, toss with pasta.",
                "pasta|200|g", "tomatoes|4|pcs", "onion|1|pcs", "garlic|2|pcs", "oil|2|tbsp", "salt|1|tsp");

        add(db, "Garlic Butter Pasta",
                "1. Boil pasta.\n2. Melt butter and fry crushed garlic for 1 min.\n3. Toss with pasta and salt.",
                "pasta|200|g", "butter|30|g", "garlic|3|pcs", "salt|1|tsp");

        add(db, "Beef Mince Pasta",
                "1. Brown mince with onion and garlic in oil.\n2. Add chopped tomatoes, simmer 20 min.\n3. Serve over boiled pasta.",
                "pasta|200|g", "mince|250|g", "tomatoes|3|pcs", "onion|1|pcs", "garlic|2|pcs", "oil|1|tbsp");

        add(db, "Egg Fried Rice",
                "1. Fry chopped onion in oil.\n2. Add cooked rice and salt.\n3. Push aside, scramble eggs, then mix together.",
                "rice|300|g", "egg|2|pcs", "onion|1|pcs", "oil|2|tbsp", "salt|1|tsp");

        add(db, "Chicken Fried Rice",
                "1. Fry diced chicken until cooked.\n2. Add onion and garlic, then cooked rice and salt.\n3. Stir-fry 5 min.",
                "rice|300|g", "chicken|200|g", "onion|1|pcs", "garlic|2|pcs", "oil|2|tbsp", "salt|1|tsp");

        add(db, "Lemon Butter Rice",
                "1. Cook rice.\n2. Stir in butter, lemon juice and salt.\n3. Serve warm.",
                "rice|300|g", "lemon|1|pcs", "butter|20|g", "salt|1|tsp");

        add(db, "Pancakes",
                "1. Whisk flour, sugar, milk and eggs into a batter.\n2. Melt butter in a pan.\n3. Fry spoonfuls until golden on both sides.",
                "flour|200|g", "milk|300|ml", "egg|2|pcs", "sugar|2|tbsp", "butter|20|g");

        add(db, "French Toast",
                "1. Whisk eggs, milk and sugar.\n2. Dip bread slices.\n3. Fry in butter until golden.",
                "bread|4|pcs", "egg|2|pcs", "milk|100|ml", "butter|20|g", "sugar|1|tbsp");

        add(db, "Cheese Toastie",
                "1. Butter the outside of the bread.\n2. Put cheese between slices.\n3. Toast in a pan until golden.",
                "bread|2|pcs", "cheese|50|g", "butter|10|g");

        add(db, "Tuna Mayo Sandwich",
                "1. Mix tuna with mayonnaise.\n2. Spread on bread.\n3. Close and slice.",
                "bread|2|pcs", "tuna|100|g", "mayonnaise|2|tbsp");

        add(db, "Banana Oat Porridge",
                "1. Simmer oats in milk for 5 min.\n2. Slice banana on top.\n3. Drizzle with honey.",
                "oats|80|g", "milk|250|ml", "banana|1|pcs", "honey|1|tbsp");

        add(db, "Mashed Potatoes",
                "1. Boil peeled potatoes until soft.\n2. Drain and mash with butter, milk and salt.",
                "potatoes|500|g", "butter|30|g", "milk|100|ml", "salt|1|tsp");

        add(db, "Roast Potatoes",
                "1. Heat oven to 200C.\n2. Toss chopped potatoes in oil, salt and pepper.\n3. Roast 40 min, turning once.",
                "potatoes|600|g", "oil|3|tbsp", "salt|1|tsp", "pepper|0.5|tsp");

        add(db, "Carrot Potato Soup",
                "1. Fry onion in butter.\n2. Add chopped carrots, potatoes, salt and water.\n3. Simmer 25 min, then blend.",
                "carrots|300|g", "potatoes|300|g", "onion|1|pcs", "butter|20|g", "salt|1|tsp");

        add(db, "Bean Stew",
                "1. Fry onion and garlic in oil.\n2. Add chopped tomatoes, beans and salt.\n3. Simmer 20 min.",
                "beans|400|g", "tomatoes|3|pcs", "onion|1|pcs", "garlic|2|pcs", "oil|1|tbsp", "salt|1|tsp");

        add(db, "Cucumber Tomato Salad",
                "1. Chop cucumber, tomatoes and onion.\n2. Dress with oil, lemon juice and salt.",
                "cucumber|1|pcs", "tomatoes|2|pcs", "onion|1|pcs", "oil|1|tbsp", "lemon|1|pcs", "salt|0.5|tsp");
    }

    /** Inserts one recipe and its ingredients. Each ingredient is "name|quantity|unit". */
    private static void add(SQLiteDatabase db, String name, String steps, String... ingredients) {
        ContentValues rv = new ContentValues();
        rv.put(DatabaseHelper.COL_NAME, name);
        rv.put(DatabaseHelper.COL_RECIPE_STEPS, steps);
        long recipeId = db.insert(DatabaseHelper.TABLE_RECIPES, null, rv);

        for (String s : ingredients) {
            String[] p = s.split("\\|");
            ContentValues iv = new ContentValues();
            iv.put(DatabaseHelper.COL_RECIPE_ID, recipeId);
            iv.put(DatabaseHelper.COL_NAME, p[0]);
            iv.put(DatabaseHelper.COL_QTY, Double.parseDouble(p[1]));
            iv.put(DatabaseHelper.COL_UNIT, p[2]);
            db.insert(DatabaseHelper.TABLE_RECIPE_ING, null, iv);
        }
    }
}