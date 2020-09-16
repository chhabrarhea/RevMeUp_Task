package com.example.blackcoffer.Model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import com.bumptech.glide.Glide;
import com.example.blackcoffer.ProfileActivity;
import com.example.blackcoffer.R;
import com.example.blackcoffer.playQuiz;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post_Item> postList;

    public PostAdapter(Context context, ArrayList<Post_Item> postList)  {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View post = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(post);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Post_Item post = postList.get(position);
        final String[] dp = {""};
         FirebaseDatabase database=FirebaseDatabase.getInstance();
         database.getReference().child("users").child(post.getUid()).child("dp").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.exists()){
                 dp[0] =snapshot.getValue().toString();
                 Glide.with(context).load(dp[0]).into(holder.dpQuiz);
                     Glide.with(context).load(dp[0]).into(holder.dp);
                 holder.dp.setBorderWidth(0);
                 holder.dpQuiz.setBorderWidth(0);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

            if (post.isQuiz()) {
            holder.quiz.setVisibility(View.VISIBLE);
            holder.captionQuiz.setText(post.getDescription());

                holder.nameQuiz.setText(post.getUsername());
        } else {
            holder.ad.setVisibility(View.VISIBLE);
            holder.caption.setText(post.getDescription());

            Glide.with(context).load(post.getImage().getUrl()).into(holder.image);
            holder.name.setText(post.getUsername());
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

     

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView dp;
        private CircleImageView dpQuiz;
        private CardView quiz;
        private CardView ad;
        private TextView name;
        private TextView nameQuiz;
        private ImageView image;
        private TextView caption;
        private TextView captionQuiz;
        private Button go;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dpQuiz = itemView.findViewById(R.id.dpQuiz);
            quiz = itemView.findViewById(R.id.cardQuiz);
            ad = itemView.findViewById(R.id.cardPost);
            nameQuiz = itemView.findViewById(R.id.usernameQuiz);
            captionQuiz = itemView.findViewById(R.id.descriptionQuiz);
            go = itemView.findViewById(R.id.go);
            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, playQuiz.class);
                    intent.putExtra("username",postList.get(getAdapterPosition()).getUsername());
                    intent.putExtra("description",postList.get(getAdapterPosition()).getDescription());
                    intent.putExtra("questions",postList.get(getAdapterPosition()).getQuestions());
                    context.startActivity(intent);
                }
            });
            
            dp = itemView.findViewById(R.id.dp);
            name = itemView.findViewById(R.id.username);
            image = itemView.findViewById(R.id.image);
            caption = itemView.findViewById(R.id.description);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ProfileActivity.class);
                    intent.putExtra("uid",postList.get(getAdapterPosition()).getUid());
                    intent.putExtra("username",postList.get(getAdapterPosition()).getUsername());
                    context.startActivity(intent);
                }
            });
            nameQuiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ProfileActivity.class);
                    intent.putExtra("uid",postList.get(getAdapterPosition()).getUid());
                    intent.putExtra("username",postList.get(getAdapterPosition()).getUsername());
                    context.startActivity(intent);
                }
            });
        }
    }

}
