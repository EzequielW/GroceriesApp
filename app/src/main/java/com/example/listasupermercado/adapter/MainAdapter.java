package com.example.listasupermercado.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.listasupermercado.CartActivity;
import com.example.listasupermercado.R;
import com.example.listasupermercado.model.Cart;
import com.example.listasupermercado.model.ProductDetail;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private ArrayList<String> cartNames;
    private ArrayList<String> cartIDs;
    private int visibleList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView editIcon;
        public ImageView deleteIcon;
        public ImageView shareIcon;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.main_row_title);
            editIcon = (ImageView) view.findViewById(R.id.main_row_edit);
            shareIcon = (ImageView) view.findViewById(R.id.main_row_share);
            deleteIcon = (ImageView) view.findViewById(R.id.main_row_delete);
        }
    }

    public MainAdapter(ArrayList<String> cartNames, ArrayList<String> cartIDs) {
        this.cartNames = cartNames;
        this.cartIDs = cartIDs;
        this.visibleList = 0;
    }

    @Override
    public MainAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_row, parent, false);

        return new MainAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MainAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(cartNames.get(position));

        if(position == visibleList){
            holder.editIcon.setVisibility(View.VISIBLE);
            holder.shareIcon.setVisibility(View.VISIBLE);
            holder.deleteIcon.setVisibility(View.VISIBLE);

            holder.editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cartActivity = new Intent(holder.name.getContext(), CartActivity.class);
                    cartActivity.putExtra("cartName", cartNames.get(position));
                    cartActivity.putExtra("cartID", cartIDs.get(position));
                    ((Activity)(holder.name.getContext())).startActivity(cartActivity);
                }
            });
            holder.shareIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                            .setLink(Uri.parse("https://www.groceriesapp.com/").buildUpon()
                                    .appendQueryParameter("cartId", cartIDs.get(position))
                                    .appendQueryParameter("cartName", cartNames.get(position)).build())
                            .setDomainUriPrefix("https://groceriesapp.page.link")
                            .setAndroidParameters(
                                    new DynamicLink.AndroidParameters.Builder("com.example.listasupermercado")
                                            .build())
                            .buildDynamicLink();

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, dynamicLink.getUri().toString());
                    sendIntent.setType("text/plain");
                    ((Activity)(holder.name.getContext())).startActivity(Intent.createChooser(sendIntent, "Select app"));
                }
            });
            holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.deleteIcon.getContext());
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference().child("cart").child(cartIDs.get(position)).removeValue();
                            cartNames.remove(position);
                            cartIDs.remove(position);
                            notifyItemRemoved(position);
                            notifyItemChanged(position, cartNames.size());
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        }
        else{
            holder.editIcon.setVisibility(View.GONE);
            holder.shareIcon.setVisibility(View.GONE);
            holder.deleteIcon.setVisibility(View.GONE);

            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int prev = visibleList;
                    visibleList = position;
                    notifyItemChanged(prev);
                    notifyItemChanged(visibleList);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cartNames.size();
    }
}