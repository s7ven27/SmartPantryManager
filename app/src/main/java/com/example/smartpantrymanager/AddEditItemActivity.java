package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.util.UiUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;

public class AddEditItemActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "item_id";

    private DatabaseHelper db;
    private long itemId = -1;

    private TextInputLayout layoutName;
    private TextInputLayout layoutQuantity;
    private TextInputEditText editName;
    private TextInputEditText editQuantity;
    private TextInputEditText editExpiry;
    private Spinner spinnerUnit;
    private ArrayAdapter<CharSequence> unitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_item);
        UiUtils.applyInsets(findViewById(R.id.main));

        db = new DatabaseHelper(this);

        layoutName = findViewById(R.id.layoutName);
        layoutQuantity = findViewById(R.id.layoutQuantity);
        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiry = findViewById(R.id.editExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);

        unitAdapter = ArrayAdapter.createFromResource(
                this, R.array.units, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);

        editExpiry.setOnClickListener(v -> showDatePicker());
        editExpiry.setOnLongClickListener(v -> {
            editExpiry.setText("");
            return true;
        });

        findViewById(R.id.buttonSave).setOnClickListener(v -> saveItem());
        findViewById(R.id.buttonCancel).setOnClickListener(v -> finish());

        // an id in the intent means we were opened to edit an existing item
        itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            loadItem();
        }
    }

    private void loadItem() {
        PantryItem item = db.getItem(itemId);
        if (item == null) {
            finish();
            return;
        }
        ((android.widget.TextView) findViewById(R.id.textTitle)).setText(R.string.title_edit_item);
        editName.setText(item.getName());
        editQuantity.setText(UiUtils.formatQuantity(item.getQuantity()));
        editExpiry.setText(item.getExpiryDate());
        int position = unitAdapter.getPosition(item.getUnit());
        if (position >= 0) {
            spinnerUnit.setSelection(position);
        }
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) ->
                editExpiry.setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day)),
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void saveItem() {
        String name = editName.getText().toString().trim();
        String qtyText = editQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String expiry = editExpiry.getText().toString().trim();

        layoutName.setError(null);
        layoutQuantity.setError(null);
        boolean valid = true;

        if (name.isEmpty()) {
            layoutName.setError("Name is required");
            valid = false;
        } else if (!name.matches(".*[A-Za-z].*")) {
            layoutName.setError("Name must contain letters");
            valid = false;
        } else if (name.length() > 40) {
            layoutName.setError("Name is too long (max 40 characters)");
            valid = false;
        }

        double quantity = 0;
        if (qtyText.isEmpty()) {
            layoutQuantity.setError("Quantity is required");
            valid = false;
        } else {
            try {
                quantity = Double.parseDouble(qtyText);
                if (quantity <= 0) {
                    layoutQuantity.setError("Quantity must be more than 0");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                layoutQuantity.setError("Enter a valid number");
                valid = false;
            }
        }

        if (!valid) {
            return;
        }

        if (itemId == -1) {
            db.addItem(new PantryItem(0, name, quantity, unit, expiry));
            Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
        } else {
            db.updateItem(new PantryItem(itemId, name, quantity, unit, expiry));
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}