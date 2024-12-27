package com.example.librarymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.librarymanagementapp.databinding.ActivityAddCatagoryBinding;
import com.example.librarymanagementapp.databinding.ActivityAdminDashboardBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddCatagory extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ActivityAddCatagoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCatagoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        binding.imagebuttonid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminDashboard.class));
            }
        });
        binding.submitid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validdata();
            }
        });
    }
    private String catagory;

    private void validdata() {
        catagory = binding.catagoryid.getText().toString().trim();
        if(TextUtils.isEmpty(catagory))
        {
            Toast.makeText(this, "Enter a category", Toast.LENGTH_SHORT).show();
        }else
        {
            long timestamp = System.currentTimeMillis();
            HashMap<String,Object>hashMap = new HashMap<>();
            hashMap.put("id",""+ timestamp);
            hashMap.put("catagory", catagory);
            hashMap.put("timestamp", timestamp);
            hashMap.put("uid", firebaseAuth.getUid());

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Catagories");
            ref.child(""+ timestamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(AddCatagory.this, "Successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddCatagory.this, "Try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}