package com.example.smartpantrymanager.util;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public final class UiUtils {

    private UiUtils() {}

    // keeps content out from under the status and navigation bars
    public static void applyInsets(View root) {
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }

    // shows 3.0 as "3" and 2.5 as "2.5"
    public static String formatQuantity(double q) {
        if (q == Math.floor(q)) {
            return String.valueOf((long) q);
        }
        return String.valueOf(q);
    }
}