package com.example.blackcoffer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blackcoffer.Model.Post_Item;
import com.example.blackcoffer.Model.Quiz_Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateQuiz extends AppCompatActivity {
boolean answer;
EditText question;
EditText description;
Button trueButton;
Button falseButton;
FirebaseDatabase database;
ArrayList<Quiz_Item> quiz_items;
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        question=findViewById(R.id.question_textview);
        description=findViewById(R.id.description);
        answer=true;
        trueButton=findViewById(R.id.true_button);
        falseButton=findViewById(R.id.false_button);
        quiz_items=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        progressBar=findViewById(R.id.progress);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void truePressed(View view){
        answer=true;
        trueButton.setBackgroundColor(getColor(R.color.quizPressed));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            falseButton.setBackgroundColor(getColor(R.color.quizButton));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void falsePressed(View view){
        answer=false;
        falseButton.setBackgroundColor(getColor(R.color.quizPressed));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            trueButton.setBackgroundColor(getColor(R.color.quizButton));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void nextPressed(View view){
        if (question.getText().toString().isEmpty())
            question.setError("Cannot be Empty");
        else {
            quiz_items.add(new Quiz_Item(answer,question.getText().toString()));
            question.setText("");
            trueButton.setBackgroundColor(getColor(R.color.quizButton));
            falseButton.setBackgroundColor(getColor(R.color.quizButton));
        }

    }
    public void save(View view){
        if(description.getText().toString().isEmpty())
            description.setError("Cannot be Empty");
        else {
            String desc=description.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Post_Item post_item = new Post_Item(user.getUid(),desc,quiz_items,user.getDisplayName());
            database.getReference().child("users").child(user.getUid()).child("posts").push().setValue(post_item).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent=new Intent(CreateQuiz.this,ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            database.getReference().child("feed").push().setValue(post_item);
        }
    }
}