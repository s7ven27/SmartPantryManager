package com.example.smartpantrymanager.util;

import android.app.Activity;
import android.content.Intent;

import com.example.smartpantrymanager.MainActivity;
import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.SettingsActivity;
import com.example.smartpantrymanager.SuggestedRecipesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public final class NavHelper {

    private NavHelper() {}

    public static void setup(Activity activity, int selectedId) {
        BottomNavigationView nav = activity.findViewById(R.id.bottomNav);
        nav.setSelectedItemId(selectedId);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedId) {
                return true;
            }

            Class<?> target;
            if (id == R.id.nav_pantry) {
                target = MainActivity.class;
            } else if (id == R.id.nav_recipes) {
                target = SuggestedRecipesActivity.class;
            } else {
                target = SettingsActivity.class;
            }

            Intent intent = new Intent(activity, target);
            // reuse the screen if it's already open instead of stacking copies
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            activity.startActivity(intent);
            // false keeps this screen's own tab highlighted
            return false;
        });
    }
}