package com.example.listasupermercado.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listasupermercado.R;
import com.example.listasupermercado.model.Category;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private ArrayList<Category> categories;
    private int visibleCategory;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView categoryRecycler;
        public TextView categoryTitle;

        public MyViewHolder(View view) {
            super(view);
            categoryRecycler = (RecyclerView) view.findViewById(R.id.search_category_list);
            categoryTitle = (TextView) view.findViewById(R.id.search_category_title);
        }
    }

    public SearchAdapter(ArrayList<Category> categories) {
        this.categories = categories;
        this.visibleCategory = 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        holder.categoryTitle.setText(categories.get(position).getCategoryName());

        // Set click listener to hide the category
        holder.categoryTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != visibleCategory){
                    int prev = visibleCategory;
                    visibleCategory = position;
                    notifyItemChanged(prev);
                    notifyItemChanged(visibleCategory);
                }
            }
        });

        // Check if its the selected category and set visibility
        if(position == visibleCategory){
            holder.categoryRecycler.setVisibility(View.VISIBLE);
        }
        else{
            holder.categoryRecycler.setVisibility(View.GONE);
        }

        // Set adapter for products
        holder.categoryRecycler.setLayoutManager(new LinearLayoutManager(holder.categoryRecycler.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.categoryRecycler.setItemAnimator(new DefaultItemAnimator());
        SearchRowAdapter mAdapter = new SearchRowAdapter(categories.get(position).getProducts(), holder.categoryRecycler.getContext());
        holder.categoryRecycler.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
