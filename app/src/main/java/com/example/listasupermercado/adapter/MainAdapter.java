package com.example.listasupermercado.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.listasupermercado.CartActivity;
import com.example.listasupermercado.R;

import com.example.listasupermercado.model.Cart;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private final ArrayList<Cart> cartList;
    private final ArrayList<String> cartIDs;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView itemQuantity;
        public MaterialCardView cartCard;
        public RecyclerView recyclerView;
        public ImageView shareIcon;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.main_row_title);
            itemQuantity = view.findViewById(R.id.main_row_quantity);
            cartCard = view.findViewById(R.id.main_row_card);
            recyclerView = view.findViewById(R.id.main_row_user_list);
            shareIcon = view.findViewById(R.id.main_row_share);
        }
    }

    public MainAdapter(ArrayList<Cart> cartList, ArrayList<String> cartIDs) {
        this.cartList = cartList;
        this.cartIDs = cartIDs;
    }

    @NonNull
    @Override
    public MainAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MainAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(cartList.get(position).getName());
        // Asign item quantity, if the products are empty firebase sets it to null
        int quantity;
        if(cartList.get(position).getProducts() != null){
            quantity = cartList.get(position).getProducts().size();
        }
        else{
            quantity = 0;
        }
        String items = holder.itemQuantity.getContext().getString(R.string.main_row_quantity_text, quantity);
        holder.itemQuantity.setText(items);

        // Open list to show items and add new ones
        holder.cartCard.setOnClickListener( v -> {
            Intent cartActivity = new Intent(holder.name.getContext(), CartActivity.class);
            cartActivity.putExtra("cartName", cartList.get(position).getName());
            cartActivity.putExtra("cartID", cartIDs.get(position));
            holder.name.getContext().startActivity(cartActivity);
        });
        // Popup menu with options for that list
        holder.cartCard.setOnLongClickListener( v -> {
            PopupMenu popup = new PopupMenu(holder.cartCard.getContext(), holder.cartCard);
            popup.getMenuInflater().inflate(R.menu.main_row_menu, popup.getMenu());
            popup.setOnMenuItemClickListener( item -> {
                if(item.getItemId() == R.id.main_row_delete){
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(holder.cartCard.getContext(), R.style.AppTheme_PopupOverlay);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure you want to delete this list?");
                    builder.setPositiveButton("Yes", (dialog, which) -> FirebaseDatabase.getInstance().getReference().child("carts").child(cartIDs.get(position)).removeValue());
                    builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                    builder.show();
                }
                else if(item.getItemId() == R.id.main_row_change_name){
                    Toast.makeText(
                            holder.cartCard.getContext(),
                            "Change title name not implemented",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                return true;
            });
            popup.show();

            return true;
        });

        // Setup user avatar list
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        ArrayList<String> userAvatars = new ArrayList<>(cartList.get(position).getSharedUsers().values());
        UserAdapter userAdapter = new UserAdapter(userAvatars);
        ((SimpleItemAnimator) holder.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        holder.recyclerView.setAdapter(userAdapter);
        holder.recyclerView.setHasFixedSize(true);

        // Share dynamic link
        holder.shareIcon.setOnClickListener( v -> {
            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://www.groceriesapp.com/").buildUpon()
                            .appendQueryParameter("cartId", cartIDs.get(position)).build())
                    .setDomainUriPrefix("https://groceriesapp.page.link")
                    .setAndroidParameters(
                            new DynamicLink.AndroidParameters.Builder("com.example.listasupermercado")
                                    .build())
                    .buildDynamicLink();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, dynamicLink.getUri().toString());
            sendIntent.setType("text/plain");
            holder.name.getContext().startActivity(Intent.createChooser(sendIntent, "Select app"));
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}