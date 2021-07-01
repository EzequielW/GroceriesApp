package com.example.listasupermercado.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listasupermercado.R;
import com.example.listasupermercado.model.ProductDetail;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder>{
    private final ArrayList<ProductDetail> productDetails;
    private final ArrayList<Integer> checkedItems;
    private final OnItemClickListener listener;
    private boolean actionModeEnabled;

    public interface OnItemClickListener {
        void onLongItemClick();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;
        public TextView quantity;
        public MaterialCardView card;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.cart_item_image);
            name = view.findViewById(R.id.cart_item_name);
            quantity = view.findViewById(R.id.cart_item_quantity);
            card = view.findViewById(R.id.cart_row_card);
        }
    }

    public CartAdapter(ArrayList<ProductDetail> productDetails, ArrayList<Integer> checkedItems, OnItemClickListener listener) {
        this.productDetails = productDetails;
        this.checkedItems = checkedItems;
        this.listener = listener;
        this.actionModeEnabled = false;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartAdapter.MyViewHolder holder, int position) {
        ProductDetail detail = productDetails.get(position);
        holder.name.setText(detail.getProduct().getProductName());
        holder.quantity.setText(detail.getProductQuantity() + detail.getProductUnit());

        int imageId = holder.image.getResources().getIdentifier(
                detail.getProduct().getProductImageName(),
                "drawable",
                holder.image.getContext().getPackageName());
        holder.image.setImageResource(imageId);


        holder.card.setChecked(checkedItems.contains(position));
        holder.name.setVisibility(checkedItems.contains(position) ? View.VISIBLE : View.GONE);
        // Add/remove from checked list
        holder.card.setOnClickListener(v -> toggleSelection(holder, position));

        holder.card.setOnLongClickListener( v -> {
            listener.onLongItemClick();
            actionModeEnabled = true;
            toggleSelection(holder, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }

    // What happens when we click an item
    public void toggleSelection(CartAdapter.MyViewHolder holder, int position){
        // Only check if we are in action mode
        if(actionModeEnabled){
            holder.card.toggle();
            if (checkedItems.contains(position)) {
                checkedItems.remove((Integer) position);
            }
            else {
                checkedItems.add(position);
            }

            // Item name visibility
            holder.name.clearAnimation();
            if(holder.name.getVisibility() == View.VISIBLE){
                // Animation to show cart item title
                holder.name.animate()
                        .translationY(holder.name.getHeight())
                        .alpha(0.0f)
                        .setDuration(500)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation){
                                super.onAnimationEnd(animation);
                                holder.name.setVisibility(View.GONE);
                            }
                        });
            }
            else{
                // Animation to hide cart item title
                holder.name.setVisibility(View.VISIBLE);
                holder.name.setAlpha(0.0f);
                holder.name.setTranslationY(holder.name.getHeight());
                holder.name.animate()
                        .translationY(0)
                        .alpha(1.0f)
                        .setDuration(500)
                        .setListener(null);
            }
        }
    }

    public boolean isActionModeEnabled() {
        return actionModeEnabled;
    }

    public void setActionModeEnabled(boolean actionModeEnabled) {
        this.actionModeEnabled = actionModeEnabled;
    }
}
