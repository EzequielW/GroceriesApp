package com.example.listasupermercado;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.example.listasupermercado.adapter.MainAdapter;
import com.example.listasupermercado.model.Cart;
import com.example.listasupermercado.model.ProductDetail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> cartNames = new ArrayList<String>();
    private ArrayList<String> cartIDs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setTitle("Mis listas");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
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
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DYNAMICLK_FAILURE", "getDynamicLink:onFailure", e);
                    }
                });

        // Add divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Dialog to add new item to the list
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Title");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = input.getText().toString();
                        cartNames.add(title);
                        mainAdapter.notifyItemChanged(cartNames.size() - 1);

                        // Save cart to database
                        DatabaseReference newPush = FirebaseDatabase.getInstance().getReference().child("cart").push();
                        newPush.setValue(new Cart(title, new ArrayList<ProductDetail>()));
                        cartIDs.add(newPush.getKey());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
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
}