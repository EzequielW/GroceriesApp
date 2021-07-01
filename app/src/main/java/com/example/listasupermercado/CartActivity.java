package com.example.listasupermercado;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.listasupermercado.adapter.CartAdapter;
import com.example.listasupermercado.model.ProductDetail;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CartActivity extends AppCompatActivity {
    private final ArrayList<ProductDetail> productDetails = new ArrayList<>();
    private final ArrayList<Integer> checkedItems = new ArrayList<>();
    private CartAdapter cartAdapter;
    private DatabaseReference cartRef;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        String cartName = getIntent().getExtras().getString("cartName");
        String cartID = getIntent().getExtras().getString("cartID");

        // Set cart title in the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(cartName);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.main_menu_logout) {
                Toast.makeText(
                        getApplicationContext(),
                        "Log out not implemented",
                        Toast.LENGTH_SHORT
                ).show();
            }
            return true;
        });

        // Load database reference to cart
        cartRef = FirebaseDatabase.getInstance().getReference().child("cart").child(cartID).child("products");

        actionModeCallback = new ActionModeCallback();

        cartAdapter = new CartAdapter(productDetails, checkedItems, () -> {
            if(actionMode == null){
                actionMode = startSupportActionMode(actionModeCallback);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.shop_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
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

        // Handle result for search activity
        ActivityResultLauncher<Intent> searchActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        ProductDetail productDetail = result.getData().getParcelableExtra("selectedProduct");
                        productDetails.add(productDetail);
                        cartRef.setValue(productDetails);
                    }
                }
        );

        // Set button to search for a new shop item
        Button addItem = findViewById(R.id.add_item_button);
        addItem.setOnClickListener(v -> {
            Intent searchActivity = new Intent(getApplicationContext(), SearchActivity.class);
            searchActivityResultLauncher.launch(searchActivity);
        });
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.cart_context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.cart_menu_delete) {
                deleteWarningAlert();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            checkedItems.clear();
            cartAdapter.setActionModeEnabled(false);
            cartAdapter.notifyDataSetChanged();
            actionMode = null;
        }
    }

    @Override
    public void onBackPressed(){
        if(actionMode != null){
            actionMode.finish();
        }
        else{
            super.onBackPressed();
        }
    }

    public void deleteWarningAlert(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this items?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            deleteCheckedItems();
            actionMode.finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Delete all checked items, reverse list to it safely
    public void deleteCheckedItems(){
        Collections.sort(checkedItems, Collections.reverseOrder());
        for(int position: checkedItems){
            productDetails.remove(position);
        }

        checkedItems.clear();
        cartAdapter.notifyDataSetChanged();
        cartRef.setValue(productDetails);
    }
}
