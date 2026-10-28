package com.example.smartpantrymanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.R;
import com.example.smartpantrymanager.model.PantryItem;
import com.example.smartpantrymanager.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    public interface Listener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final Listener listener;

    public PantryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.textName.setText(item.getName());
        holder.textQuantity.setText(UiUtils.formatQuantity(item.getQuantity()) + " " + item.getUnit());

        String expiry = item.getExpiryDate();
        if (expiry == null || expiry.isEmpty()) {
            holder.textExpiry.setVisibility(View.GONE);
        } else {
            holder.textExpiry.setVisibility(View.VISIBLE);
            holder.textExpiry.setText(
                    holder.itemView.getContext().getString(R.string.expires, expiry));
        }

        holder.itemView.setOnClickListener(v -> listener.onEdit(item));
        holder.buttonDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textName;
        final TextView textQuantity;
        final TextView textExpiry;
        final ImageButton buttonDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textExpiry = itemView.findViewById(R.id.textExpiry);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}