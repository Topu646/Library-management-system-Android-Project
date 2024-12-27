package com.example.librarymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        firebaseAuth = FirebaseAuth.getInstance();

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               checkuser();
           }
       },1500);

    }

    private void checkuser() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null)
        {
            Intent intent = new Intent(SplashActivity.this,Loginactivity.class);
            startActivity(intent);
            finish();
        }else
        {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userType =""+ snapshot.child("userType").getValue();
                    if(userType.equals("user"))
                    {
                        Intent intent = new Intent(SplashActivity.this,UserDashboard.class);
                        startActivity(intent);
                        finish();
                    }else if (userType.equals("admin"))
                    {
                        Intent intent = new Intent(SplashActivity.this,AdminDashboard.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}