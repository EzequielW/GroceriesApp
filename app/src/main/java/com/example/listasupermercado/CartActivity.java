package com.example.listasupermercado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.example.listasupermercado.adapter.CartAdapter;
import com.example.listasupermercado.model.Cart;
import com.example.listasupermercado.model.Product;
import com.example.listasupermercado.model.ProductDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private int LAUNCH_SEARCH_ACTIVITY = 1;
    private ArrayList<ProductDetail> productDetails = new ArrayList<ProductDetail>();
    private CartAdapter cartAdapter;
    private String cartName;
    private String cartID;
    private DatabaseReference cartRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        cartName = getIntent().getExtras().getString("cartName");
        cartID = getIntent().getExtras().getString("cartID");
        // Load database reference to cart
        cartRef = FirebaseDatabase.getInstance().getReference().child("cart").child(cartID).child("products");

        cartAdapter = new CartAdapter(productDetails, cartID, cartRef);
        RecyclerView recyclerView = findViewById(R.id.shop_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);

        // Listener to update the cart if any change occurs
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productDetails.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    ProductDetail pd = snapshot1.getValue(ProductDetail.class);
                    productDetails.add(pd);
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("The read failed: " , error.toException());
            }
        });

        // Add divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Set button to search for a new shop item
        Button addItem = findViewById(R.id.add_item_button);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
                startActivityForResult(searchActivity, LAUNCH_SEARCH_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LAUNCH_SEARCH_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                ProductDetail productDetail = data.getParcelableExtra("selectedProduct");
                productDetails.add(productDetail);
                cartRef.setValue(productDetails);
            }
        }
    }
}
