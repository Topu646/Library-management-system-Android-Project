package com.example.librarymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.librarymanagementapp.databinding.ActivityAdminDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminDashboard extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ActivityAdminDashboardBinding binding;

    private ArrayList<ModelCategory>categoryArrayList;
    private AdapterCatagory adapterCatagory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadcategory();

        binding.logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        binding.catagorybuttonid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddCatagory.class));
            }
        });
        binding.addpdfid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PdfAddActivity.class));
            }
        });

        binding.profileid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, ProfileActivity.class));
            }
        });

    }

    private void loadcategory() {
        categoryArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Catagories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                categoryArrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    ModelCategory modelCategory = dataSnapshot.getValue(ModelCategory.class);
                    categoryArrayList.add(modelCategory);
                }
                adapterCatagory = new AdapterCatagory(AdminDashboard.this,categoryArrayList);
                binding.recyclerid.setAdapter(adapterCatagory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void checkUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null)
        {
            startActivity(new Intent(getApplicationContext(),Loginactivity.class));
            finish();
        }else
        {

            String email = firebaseUser.getEmail();
        }
    }
}