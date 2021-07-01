package com.example.listasupermercado.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.listasupermercado.R;
import com.example.listasupermercado.model.Category;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private final ArrayList<Category> categories;
    private final RecyclerView.RecycledViewPool recycledRowPool;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView categoryRecycler;
        public Button categoryTitle;
        public boolean expanded;

        public MyViewHolder(View view) {
            super(view);
            categoryRecycler = view.findViewById(R.id.search_category_list);
            categoryTitle = view.findViewById(R.id.search_category_title);
            expanded = true;
        }
    }

    public SearchAdapter(ArrayList<Category> categories) {
        this.categories = categories;
        this.recycledRowPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.categoryTitle.setText(categories.get(position).getCategoryName());
        holder.categoryRecycler.setVisibility(holder.expanded ? View.VISIBLE : View.GONE);

        // Set click listener to collapse the category
        holder.categoryTitle.setOnClickListener(v -> {
            holder.expanded = !holder.expanded;
            notifyItemChanged(position);
        });

        // Set adapter for products
        holder.categoryRecycler.setLayoutManager(new LinearLayoutManager(holder.categoryRecycler.getContext(), LinearLayoutManager.HORIZONTAL, false));
        SearchRowAdapter mAdapter = new SearchRowAdapter(categories.get(position).getProducts());
        ((SimpleItemAnimator) holder.categoryRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        holder.categoryRecycler.setRecycledViewPool(recycledRowPool);
        holder.categoryRecycler.setAdapter(mAdapter);
        holder.categoryRecycler.setHasFixedSize(true);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
