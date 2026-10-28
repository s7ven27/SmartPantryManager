package com.example.smartpantrymanager.util;

import android.app.Activity;
import android.widget.Toast;

import com.example.smartpantrymanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public final class NavHelper {

    private NavHelper() {}

    public static void setup(Activity activity, int selectedId) {
        BottomNavigationView nav = activity.findViewById(R.id.bottomNav);
        nav.setSelectedItemId(selectedId);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == selectedId) {
                return true;
            }
            // real screens are added in later steps
            Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show();
            return false;
        });
    }
}