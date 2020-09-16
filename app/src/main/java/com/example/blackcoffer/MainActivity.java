package com.example.blackcoffer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.example.blackcoffer.Model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private FirebaseAuth mAuth;
private GoogleSignInClient client;
SignInButton google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_ui);
        mAuth=FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null)
        {
            FirebaseUser currentUser=mAuth.getCurrentUser();
            Intent intent=new Intent(MainActivity.this,feed.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client= GoogleSignIn.getClient(this, gso);
        google=findViewById(R.id.gmail);
        google.setOnClickListener((View.OnClickListener) this);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                Log.d("MainActivity", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                 Log.i("onActivityResult","Exception: "+e.getMessage());
            }
        }

    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            FirebaseUser currentUser=mAuth.getCurrentUser();
                            Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                            intent.putExtra("uid",currentUser.getUid());
                            intent.putExtra("username",currentUser.getDisplayName());
                            User user=new User(currentUser.getDisplayName(),currentUser.getUid());
                            if(currentUser.getPhotoUrl()!=null)
                                database.getReference().child("users").child(currentUser.getUid()).child("dp").setValue(currentUser.getPhotoUrl().toString());
                            database.getReference().child("allUsers").push().setValue(user);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.gmail)
        {
            Intent signInIntent = client.getSignInIntent();
            startActivityForResult(signInIntent, 1);
        }

    }
    public void signIn(View view){
        Intent intent=new Intent(MainActivity.this,Login.class);
        startActivity(intent);
    }
    public void signUp(View view){
        Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
        startActivity(intent);
    }

}