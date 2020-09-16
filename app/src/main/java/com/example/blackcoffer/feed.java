package com.example.blackcoffer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.blackcoffer.Model.Post_Item;
import com.example.blackcoffer.Model.PostAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class feed extends AppCompatActivity {
ArrayList<Post_Item> allPosts;
PostAdapter adapter;
RecyclerView recyclerView;
FirebaseDatabase database;
FirebaseAuth mAuth;
Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        allPosts=new ArrayList<>();
        recyclerView=findViewById(R.id.recycle);
        adapter=new PostAdapter(feed.this,allPosts);
        recyclerView.setAdapter(adapter);
        mAuth=FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database=FirebaseDatabase.getInstance();
        database.getReference().child("feed").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post_Item post=snapshot.getValue(Post_Item.class);
                if(post!=null){
                    allPosts.add(post);
                adapter.notifyDataSetChanged();}
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.feed_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.allUsers:
                Intent intent=new Intent(feed.this,ListOfUsers.class);
                startActivity(intent);
                break;
            case R.id.profile:
                Intent intent1=new Intent(feed.this,ProfileActivity.class);
                intent1.putExtra("uid",mAuth.getCurrentUser().getUid());
                intent1.putExtra("username",mAuth.getCurrentUser().getDisplayName());
                String photo="";
                if(mAuth.getCurrentUser().getPhotoUrl()!=null)
                    photo=mAuth.getCurrentUser().getPhotoUrl().toString();
                intent1.putExtra("dp",photo);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}