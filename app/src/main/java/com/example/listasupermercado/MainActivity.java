package com.example.listasupermercado;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import com.example.listasupermercado.adapter.MainAdapter;
import com.example.listasupermercado.model.Cart;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final ArrayList<String> cartNames = new ArrayList<>();
    private final ArrayList<String> cartIDs = new ArrayList<>();
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> onSignInResult(result)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ingredients)
                .setTheme(R.style.AppTheme)
                .build();
        signInLauncher.launch(signInIntent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.main_menu_logout) {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(task -> signInLauncher.launch(signInIntent));
            }
            return true;
        });

        // Load user carts
        try{
            FileInputStream fis = this.openFileInput("user_carts.bin");
            Input input = new Input(fis);
            while(input.available() > 0){
                cartNames.add(input.readString());
                cartIDs.add(input.readString());
            }
            input.close();
            fis.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        // User lists recycler
        final MainAdapter mainAdapter = new MainAdapter(cartNames, cartIDs);
        RecyclerView recyclerView = findViewById(R.id.cart_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mainAdapter);

        // Link to share a list
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        String newCartID = deepLink.getQueryParameter("cartId");
                        String newCartName = deepLink.getQueryParameter("cartName");
                        Log.i("CART_ID", newCartID);
                        Log.i("CART_NAME", newCartName);
                        if(!cartIDs.contains(newCartID)){
                            cartIDs.add(newCartID);
                            cartNames.add(newCartName);
                            mainAdapter.notifyItemChanged(cartIDs.size() - 1);
                            mainAdapter.notifyItemChanged(cartNames.size() - 1);
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
                cartNames.add(title);
                mainAdapter.notifyItemChanged(cartNames.size() - 1);

                // Save cart to database
                DatabaseReference newPush = FirebaseDatabase.getInstance().getReference().child("cart").push();
                newPush.setValue(new Cart(title, new ArrayList<>()));
                cartIDs.add(newPush.getKey());
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });


    }

    @Override
    protected void onStop(){
        super.onStop();

        try{
            FileOutputStream fos = this.openFileOutput("user_carts.bin", Context.MODE_PRIVATE);
            Output output = new Output(fos);
            for(int i = 0; i < cartIDs.size(); i++){
                output.writeString(cartNames.get(i));
                output.writeString(cartIDs.get(i));
            }
            output.close();
            fos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        /*
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // ...
        }

         */
    }
}