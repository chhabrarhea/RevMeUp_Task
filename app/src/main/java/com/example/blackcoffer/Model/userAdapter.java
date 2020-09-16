package com.example.blackcoffer.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;

import com.bumptech.glide.Glide;
import com.example.blackcoffer.ProfileActivity;
import com.example.blackcoffer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class userAdapter extends RecyclerView.Adapter<userAdapter.viewHolder> {
private Context context;
private ArrayList<User> allUsers;

    public userAdapter(Context context, ArrayList<User> allUsers) {
        this.context = context;
        this.allUsers = allUsers;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,null,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {
        User user=allUsers.get(position);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        database.getReference().child("users").child(user.getUid()).child("dp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getValue()!=null)
                    Glide.with(context).asBitmap().load(snapshot.getValue().toString()).centerCrop().into(holder.dp);
                holder.dp.setBorderWidth(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.username.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        private TextView username;
        private CircleImageView dp;
        private RelativeLayout card;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            dp=itemView.findViewById(R.id.dp);
            card=itemView.findViewById(R.id.parent);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ProfileActivity.class);
                    intent.putExtra("uid",allUsers.get(getAdapterPosition()).getUid());
                    intent.putExtra("username",allUsers.get(getAdapterPosition()).getName());
                    context.startActivity(intent);
                }
            });
        }
    }

}
