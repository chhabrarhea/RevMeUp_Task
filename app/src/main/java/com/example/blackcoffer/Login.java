package com.example.blackcoffer;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
EditText email;
EditText password;
TextInputLayout emailLayout;
TextInputLayout passwordLayout;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.signEmail);
        password=findViewById(R.id.signPassword);
        emailLayout=findViewById(R.id.signEmailLayout);
        passwordLayout=findViewById(R.id.signPasswordLayout);
        mAuth=FirebaseAuth.getInstance();

    }

    public void checkLogin(View view)
    {
        if(email.getText().toString().isEmpty())
        {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Cannot be Empty");
            return;
        }
        else
            emailLayout.setErrorEnabled(false);
        if(password.getText().toString().isEmpty())
        {
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError("Cannot be Empty");
            return;
        }
        else
           passwordLayout.setErrorEnabled(false);
        login();
    }
    public void login(){
        if(email.getText()!=null && email.getText().toString().isEmpty())
            email.setError("Cannot Be Empty");
        else if (password.getText()!=null && password.getText().toString().isEmpty())
            password.setError("Cannot Be Empty");
        else
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this,"Authentication Successful!",Toast.LENGTH_SHORT).show();
                                FirebaseUser currentUser=mAuth.getCurrentUser();
                                Intent intent=new Intent(Login.this,ProfileActivity.class);
                                intent.putExtra("uid",currentUser.getUid());
                                intent.putExtra("username",currentUser.getDisplayName());
                                if(currentUser.getPhotoUrl()!=null)
                                intent.putExtra("dp",currentUser.getPhotoUrl().toString());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Login.this,"Authentication Failed!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }
}