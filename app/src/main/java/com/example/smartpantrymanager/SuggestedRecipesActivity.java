package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapter.RecipeAdapter;
import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.logic.RecipeMatcher;
import com.example.smartpantrymanager.model.Recipe;
import com.example.smartpantrymanager.util.NavHelper;
import com.example.smartpantrymanager.util.UiUtils;

import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity implements RecipeAdapter.Listener {

    private DatabaseHelper db;
    private RecipeAdapter adapter;
    private TextView textEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);
        UiUtils.applyInsets(findViewById(R.id.main));

        db = new DatabaseHelper(this);
        textEmpty = findViewById(R.id.textEmpty);

        RecyclerView list = findViewById(R.id.recyclerRecipes);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(this);
        list.setAdapter(adapter);

        NavHelper.setup(this, R.id.nav_recipes);
    }

    // re-run the matching each time, so pantry changes show straight away
    @Override
    protected void onResume() {
        super.onResume();
        List<Recipe> suggested = RecipeMatcher.getSuggestedRecipes(
                db.getAllRecipes(), db.getAllItems());
        adapter.setRecipes(suggested);
        textEmpty.setVisibility(suggested.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}