package com.example.librarymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.librarymanagementapp.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loaduserinfo();

        binding.backid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfileActivity.this, ProfileEditActivity.class));
            }
        });

    }

    private void loaduserinfo() {
        DatabaseReference ref  = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email =""+ snapshot.child("email").getValue();
                        String name =""+ snapshot.child("name").getValue();
                        String profilepic =""+ snapshot.child("profilepic").getValue();
                        String timestamp =""+ snapshot.child("timeStamp").getValue();
                        String uid =""+ snapshot.child("UId").getValue();
                        String usertype =""+ snapshot.child("userType").getValue();

                        String  formatteddate = MyApplication.formattimestamp(Long.parseLong(timestamp));

                        binding.nametv.setText(name);
                        binding.emailtv.setText(email);
                        binding.accounttypetv.setText(usertype);
                        binding.memberdatetv.setText(formatteddate);


                        Glide.with(ProfileActivity.this)
                                .load(profilepic)
                                .placeholder(R.drawable.ic_baseline_person_24)
                                .into(binding.profilepic);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}