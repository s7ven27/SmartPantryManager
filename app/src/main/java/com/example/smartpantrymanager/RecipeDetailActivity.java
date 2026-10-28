package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.model.RecipeIngredient;
import com.example.smartpantrymanager.util.UiUtils;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        UiUtils.applyInsets(findViewById(R.id.main));

        long id = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        Recipe recipe = new DatabaseHelper(this).getRecipe(id);
        if (recipe == null) {
            finish();
            return;
        }

        ((TextView) findViewById(R.id.textRecipeTitle)).setText(recipe.getName());

        StringBuilder sb = new StringBuilder();
        for (RecipeIngredient ing : recipe.getIngredients()) {
            sb.append("\u2022 ")
                    .append(UiUtils.formatQuantity(ing.getQuantity()))
                    .append(" ")
                    .append(ing.getUnit())
                    .append(" ")
                    .append(ing.getName())
                    .append("\n");
        }
        ((TextView) findViewById(R.id.textIngredients)).setText(sb.toString().trim());
        ((TextView) findViewById(R.id.textSteps)).setText(recipe.getSteps());

        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
    }
}