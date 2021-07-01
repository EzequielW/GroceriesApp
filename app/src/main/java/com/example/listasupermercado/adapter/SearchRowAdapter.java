package com.example.listasupermercado.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listasupermercado.R;
import com.example.listasupermercado.SearchRowDialog;
import com.example.listasupermercado.model.Product;

public class SearchRowAdapter extends RecyclerView.Adapter<SearchRowAdapter.MyViewHolder> {
    private final ArrayList<Product> products;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.search_item_img);
            title = view.findViewById(R.id.search_item_name);
        }
    }

    public SearchRowAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        int imageId = holder.image.getResources().getIdentifier(
                products.get(position).getProductImageName(),
                "drawable",
                holder.image.getContext().getPackageName());
        holder.image.setImageResource(imageId);
        holder.title.setText(products.get(position).getProductName());

        // Set dialog to add product to shop list
        holder.image.setOnClickListener( v -> {
            SearchRowDialog dialog = new SearchRowDialog();
            dialog.showDialog(holder.image.getContext(), products.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
