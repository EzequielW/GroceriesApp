package com.example.listasupermercado;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.listasupermercado.adapter.MainAdapter;
import com.example.listasupermercado.model.Cart;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final ArrayList<Cart> cartList = new ArrayList<>();
    private final ArrayList<String> cartIDs = new ArrayList<>();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.main_menu_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent signInActivity = new Intent(this, SignInActivity.class);
                startActivity(signInActivity);
            }
            return true;
        });

        // User lists recycler
        final MainAdapter mainAdapter = new MainAdapter(cartList, cartIDs);
        RecyclerView recyclerView = findViewById(R.id.cart_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mainAdapter);

        // Load user carts
        Query cartQuery = dbRef.child("carts").orderByChild("sharedUsers/"+user.getUid()).startAt(false);
        cartQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    cartIDs.clear();
                    cartList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        Cart cart = snapshot1.getValue(Cart.class);
                        cartList.add(cart);
                        cartIDs.add(snapshot1.getKey());
                    }
                    mainAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("User read failed: " , error.toException());
                }
            }
        );

        // Link to share a list
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)

                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        String newCartID = deepLink.getQueryParameter("cartId");
                        Log.i("CART_ID", newCartID);
                        if(!cartIDs.contains(newCartID)){
                            // Add user to shared so he can see the cart
                            String photo;
                            if (user.getPhotoUrl() != null){
                                photo = user.getPhotoUrl().toString();
                            }
                            else{
                                photo = "noPhoto";
                            }
                            dbRef.child("carts").child(newCartID)
                                    .child("sharedUsers")
                                    .child(user.getUid())
                                    .setValue(photo);
                        }
                    }
                })
                .addOnFailureListener(this, e -> Log.w("DYNAMICLK_FAILURE", "getDynamicLink:onFailure", e));

        // Dialog to add new item to the list
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this, R.style.AppTheme_PopupOverlay);
            builder.setTitle("List title");
            // Set up the input
            final EditText input = new EditText(MainActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) -> {
                String title = input.getText().toString();

                // Save cart to database, user must be added to shared users because firebase can only query by one field
                Map<String, String> sharedUsers = new HashMap<>();
                if(user.getPhotoUrl() != null){
                    sharedUsers.put(user.getUid(), user.getPhotoUrl().toString());
                }
                else{
                    sharedUsers.put(user.getUid(), "noPhoto");
                }
                DatabaseReference newPush = FirebaseDatabase.getInstance().getReference().child("carts").push();
                newPush.setValue(new Cart(title, new ArrayList<>(), user.getUid(), sharedUsers));
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });
    }
}