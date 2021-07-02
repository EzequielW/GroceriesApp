package com.example.listasupermercado.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listasupermercado.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    private final ArrayList<String> avatarList;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView avatar;

        public MyViewHolder(View view){
            super(view);
            avatar = view.findViewById(R.id.main_row_user_avatar);
        }
    }

    public UserAdapter(ArrayList<String> avatarList){
        this.avatarList = avatarList;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_row_user, parent, false);

        return new UserAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position){
        if(!avatarList.get(position).equals("noPhoto")){
                Picasso.get().load(avatarList.get(position)).into(holder.avatar);
        }
    }

    @Override
    public int getItemCount() {
        return avatarList.size();
    }
}
