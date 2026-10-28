package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.adapter.PantryAdapter;
import com.example.smartpantrymanager.db.DatabaseHelper;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.util.NavHelper;
import com.example.smartpantrymanager.util.UiUtils;
import com.example.smartpantrymanager.util.Prefs;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PantryAdapter.Listener {

    private DatabaseHelper db;
    private PantryAdapter adapter;
    private TextView textEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        UiUtils.applyInsets(findViewById(R.id.main));

        db = new DatabaseHelper(this);
        textEmpty = findViewById(R.id.textEmpty);

        RecyclerView list = findViewById(R.id.recyclerPantry);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(this);
        list.setAdapter(adapter);

        findViewById(R.id.fabAdd).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditItemActivity.class)));

        NavHelper.setup(this, R.id.nav_pantry);
    }

    // reload every time we come back, so changes made on other screens show up
    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        List<PantryItem> items = db.getAllItems();
        adapter.setHighlightExpiring(Prefs.isExpiryAlertsOn(this));
        adapter.setItems(items);
        textEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEdit(PantryItem item) {
        Intent intent = new Intent(this, AddEditItemActivity.class);
        intent.putExtra(AddEditItemActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onDelete(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_item)
                .setMessage("Remove " + item.getName() + " from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteItem(item.getId());
                    loadItems();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}