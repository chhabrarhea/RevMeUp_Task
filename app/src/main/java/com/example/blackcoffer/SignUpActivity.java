package com.example.blackcoffer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blackcoffer.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    EditText confirmPassword;
    EditText brandName;
    TextInputLayout emailLayout;
    TextInputLayout brandNameLayout;
    TextInputLayout passwordLayout;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email = findViewById(R.id.signEmail);
        password = findViewById(R.id.signPassword);
        confirmPassword = findViewById(R.id.signConfirmPassword);
        emailLayout = findViewById(R.id.signEmailLayout);
        passwordLayout = findViewById(R.id.signPasswordLayout);
        mAuth = FirebaseAuth.getInstance();
        brandName = findViewById(R.id.signUsername);
        brandNameLayout = findViewById(R.id.signUsernameLayout);
    }

    public void checkSignup(View view) {
        if (brandName.getText().toString().isEmpty()) {
            brandNameLayout.setErrorEnabled(true);
            brandNameLayout.setError("Cannot be Empty");
            return;
        } else
            brandNameLayout.setErrorEnabled(false);
        if (!isValidEmail(email.getText().toString())) {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Cannot be Empty");
            return;
        } else
            emailLayout.setErrorEnabled(false);
        if (!isValidPassword(password.getText().toString())) {
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError("Invalid Password, must contain a Capital Letter");
            return;
        } else if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError("Passwords must Match!");
            return;
        } else
            passwordLayout.setErrorEnabled(false);
        signUp();

    }

    public void signUp() {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(brandName.getText().toString()).build();
                            Objects.requireNonNull(mAuth.getCurrentUser()).updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    move();
                                }

                            });
                            Toast.makeText(SignUpActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

    public void move()
    {
        FirebaseUser currentUser=mAuth.getCurrentUser();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        User user=new User(currentUser.getDisplayName(),currentUser.getUid());
        if(currentUser.getPhotoUrl()!=null)
            database.getReference().child("users").child(currentUser.getUid()).child("dp").setValue(currentUser.getPhotoUrl().toString());
        database.getReference().child("allUsers").push().setValue(user);
        Intent intent=new Intent(SignUpActivity.this,ProfileActivity.class);
        intent.putExtra("uid",currentUser.getUid());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("username",currentUser.getDisplayName());
        startActivity(intent);
        finish();
    }


    public boolean isValidPassword(CharSequence pass) {
        Pattern pattern;
        Matcher matcher;
        if (pass == null || pass.length() == 0)
            return false;
        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(pass);
        return matcher.matches();
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null || target.equals(""))
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


}