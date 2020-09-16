package com.example.blackcoffer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blackcoffer.Model.Post_Item;
import com.example.blackcoffer.Model.Quiz_Item;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class playQuiz extends AppCompatActivity {
    ArrayList<Quiz_Item> questions;
    TextView ques;
    TextView description;
    int i;
    int correctAns;
    TextView score;
    int size;
    Boolean complete;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);
        Intent intent=getIntent();
        ques=findViewById(R.id.question_textview);
        description=findViewById(R.id.description);
        complete=false;
        String d=intent.getStringExtra("description");
        if (d!=null)
            description.setText(d);
        String user=intent.getStringExtra("username");
        score=findViewById(R.id.counter_text);
        questions=intent.getParcelableArrayListExtra("questions");
        size=questions.size();
        i=0;
        correctAns=0;
        ques.setText(questions.get(0).getQuestion());
        score.setText(correctAns+" out of "+size);

    }
    private void fadeView() {
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);


        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(playQuiz.this,
                R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    public void nextPressed(View view) {
        if (i < size-1) {
            i++;
            ques.setText(questions.get(i).getQuestion());
        }

    }
    public void prevPressed(View view){
        if(i>0){
            i--;
            ques.setText(questions.get((i)).getQuestion());}
    }
    public void truePressed(View view){
        if(questions.get(i).isAnswer())
    {
        fadeView();
        if(!complete){
        correctAns++;
        score.setText(correctAns+" out of "+size);}

    }
    else
        shakeAnimation();
        if(i==size-1){
            complete=true;
            if (correctAns==size)
                Toast.makeText(this, "Yayy!You scored full points.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "You scored "+correctAns+ " out of "+size, Toast.LENGTH_SHORT).show();}
        if (i<size-1)
            i++;
        ques.setText(questions.get(i).getQuestion());

    }
    public void falsePressed(View view){
        if(!questions.get(i).isAnswer())
        {
            if(!complete){
            correctAns++;
            score.setText(correctAns+" out of "+size);}
            fadeView();
        }
        else
        {
            shakeAnimation();
        }
        if(i==size-1) {
            complete = true;
            if (correctAns==size)
                Toast.makeText(this, "Yayy!You scored full points.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "You scored "+correctAns+ " out of "+size, Toast.LENGTH_SHORT).show();
        }
        if (i<size-1)
            i++;
        ques.setText(questions.get(i).getQuestion());

    }
}