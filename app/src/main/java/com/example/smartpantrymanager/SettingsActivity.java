package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.smartpantrymanager.util.NavHelper;
import com.example.smartpantrymanager.util.Prefs;
import com.example.smartpantrymanager.util.UiUtils;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        UiUtils.applyInsets(findViewById(R.id.main));

        SwitchCompat switchExpiry = findViewById(R.id.switchExpiry);
        switchExpiry.setChecked(Prefs.isExpiryAlertsOn(this));
        switchExpiry.setOnCheckedChangeListener((button, checked) ->
                Prefs.setExpiryAlerts(this, checked));

        NavHelper.setup(this, R.id.nav_settings);
    }
}