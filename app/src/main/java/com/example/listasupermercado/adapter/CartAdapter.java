package com.example.listasupermercado.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.listasupermercado.R;
import com.example.listasupermercado.model.ProductDetail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder>{
    private ArrayList<ProductDetail> productDetails;
    private String cartID;
    private DatabaseReference cartRef;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;
        public TextView quantity;
        public ImageView delete;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.shop_item_image);
            name = (TextView) view.findViewById(R.id.shop_item_name);
            quantity = (TextView) view.findViewById(R.id.shop_item_quantity);
            delete = (ImageView) view.findViewById(R.id.shop_item_delete);
        }
    }

    public CartAdapter(ArrayList<ProductDetail> productDetails, String cartID, DatabaseReference cartRef) {
        this.productDetails = productDetails;
        this.cartID = cartID;
        this.cartRef = cartRef;
    }

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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDetails.remove(position);
                cartRef.setValue(productDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productDetails.size();
    }
}
