package com.example.smartpantrymanager.util;

import android.content.Context;

public final class Prefs {

    private static final String FILE = "pantry_prefs";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts";

    private Prefs() {}

    public static boolean isExpiryAlertsOn(Context context) {
        return context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
                .getBoolean(KEY_EXPIRY_ALERTS, true);
    }

    public static void setExpiryAlerts(Context context, boolean on) {
        context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
                .edit().putBoolean(KEY_EXPIRY_ALERTS, on).apply();
    }
}