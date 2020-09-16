package com.example.blackcoffer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.blackcoffer.Model.PostAdapter;
import com.example.blackcoffer.Model.Post_Item;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {
FirebaseDatabase database;
CircleImageView dp;
TextView brand;
RecyclerView recyclerView;
ArrayList<Post_Item> allPosts;
PostAdapter adapter;
String photo;
String uid;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");
        String username=intent.getStringExtra("username");
        database.getReference().child("users").child(uid).child("dp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getValue()!=null){
                    Glide.with(ProfileActivity.this).asBitmap().load(snapshot.getValue().toString()).centerCrop().into(dp);
                dp.setBorderWidth(0);}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Database Error.Cannot load display picture", Toast.LENGTH_SHORT).show();
            }
        });


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(checkCurrent())
        {
            FloatingActionMenu fab=findViewById(R.id.fab_menu);
            fab.setVisibility(View.VISIBLE);
        }



        recyclerView=findViewById(R.id.recycle);
        dp=findViewById(R.id.dp);
        brand=findViewById(R.id.username);
        allPosts=new ArrayList<>();
        adapter=new PostAdapter(ProfileActivity.this,allPosts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        brand.setText(username);

        database.getReference().child("users").child(uid).child("posts").addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Post_Item post=snapshot.getValue(Post_Item.class);
                if(post!=null) {
                    allPosts.add(post);
                    adapter.notifyDataSetChanged();

                }
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

             }
         });

    }
    public void postImage(View view){
        Intent intent=new Intent(ProfileActivity.this,Create_Post.class);
        startActivity(intent);
        finish();
    }
    public void postQuiz(View view)
    {
        Intent intent=new Intent(ProfileActivity.this,CreateQuiz.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(checkCurrent()){
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.my_profile,menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                signOut();
                break;
            case R.id.feed:
                Intent intent=new Intent(ProfileActivity.this,feed.class);
                startActivity(intent);
                break;
            case R.id.change:
                Intent intent2=new Intent(ProfileActivity.this,changeDisplay.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkCurrent()
    {
        return mAuth.getCurrentUser().getUid().equals(uid);
    }

    public void signOut()
    {
        AlertDialog.Builder alb=new AlertDialog.Builder(this);
        alb.setTitle("Logout");
        alb.setMessage("Are you sure you want to logout?")
                .setIcon(R.drawable.ic_power)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient client= GoogleSignIn.getClient(ProfileActivity.this, gso);
                client.revokeAccess();
                mAuth.signOut();
                Intent intent1=new Intent(ProfileActivity.this,MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();
            }
        }).show();

    }

}