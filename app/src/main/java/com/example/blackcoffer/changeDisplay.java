package com.example.blackcoffer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

public class changeDisplay extends AppCompatActivity {
    Bitmap bitmap;
    ImageView image;
    RelativeLayout root;
    FirebaseDatabase db;
    FirebaseStorage storage;
    String imageName;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_display);
        image=findViewById(R.id.image);
       progressBar=findViewById(R.id.progress);
        root=findViewById(R.id.root);
        db=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        imageName= UUID.randomUUID().toString()+".jpg";
        image=findViewById(R.id.photo);
        mAuth=FirebaseAuth.getInstance();
    }
    public void save(View view){
        if(bitmap==null)
        {
            Toast.makeText(changeDisplay.this,"No Photo Chosen!",Toast.LENGTH_SHORT).show();

        }
        else
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] data = baos.toByteArray();
            progressBar.setVisibility(View.VISIBLE);
            final UploadTask uploadTask = storage.getReference().child("images").child(imageName).putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(changeDisplay.this,"Image upload failed.Try again!",Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = uploadTask  .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw Objects.requireNonNull(task.getException());
                            }
                            return storage.getReference().child("images").child(imageName).getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                final FirebaseUser current=mAuth.getCurrentUser();
                                final Uri downloadUri = task.getResult();
                                assert downloadUri != null;
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(downloadUri).build();

                                Objects.requireNonNull(current).updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                                        database.getReference().child("users").child(current.getUid()).child("dp").setValue(downloadUri.toString());
                                        Intent intent=new Intent(changeDisplay.this,ProfileActivity.class);
                                        intent.putExtra("uid",current.getUid());
                                        intent.putExtra("username",current.getDisplayName());
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            } else {
                                Toast.makeText(changeDisplay.this,"Image upload failed.Try again!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==1 || requestCode==1888) && resultCode == RESULT_OK && data != null) {

            try {

                if(requestCode==1) {
                    Uri uri = data.getData();
                    Glide.with(this)
                            .asBitmap()
                            .load(uri)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    image.setImageBitmap(resource);
                                    image.setVisibility(View.VISIBLE);
                                    bitmap=resource;
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                }
                else {
                    bitmap = (Bitmap) (Objects.requireNonNull(data.getExtras())).get("data");
                    image.setImageBitmap(bitmap);
                    image.setVisibility(View.VISIBLE);

                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            bitmap=null;}
    public void capture(View view){
        if (ContextCompat.checkSelfPermission(changeDisplay.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1888);
        }
        else{
            Dexter.withContext(changeDisplay.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1888);
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    if(permissionDeniedResponse.isPermanentlyDenied()){
                        Snackbar.make(root,"Change app setings to continue",Snackbar.LENGTH_LONG)
                                .setAction("Settings", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.parse("package:" + getPackageName()));
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }).show();
                    }
                    else
                        Toast.makeText(changeDisplay.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).check();
        }}
    public void upload(View view){
        if (ContextCompat.checkSelfPermission(changeDisplay.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }
        else{
            Dexter.withContext(changeDisplay.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    if(permissionDeniedResponse.isPermanentlyDenied())
                    {

                        Snackbar.make(root,"Change app settings to continue",Snackbar.LENGTH_LONG)
                                .setAction("Settings", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.parse("package:" + getPackageName()));
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }).show();
                    }
                    else
                        Toast.makeText(changeDisplay.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                }
            }).check();
        }
    }
}