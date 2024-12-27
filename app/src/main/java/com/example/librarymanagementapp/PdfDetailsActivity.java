package com.example.librarymanagementapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.librarymanagementapp.databinding.ActivityPdfDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PdfDetailsActivity extends AppCompatActivity {
    private ActivityPdfDetailsBinding binding;
    String bookid, booktitle, bookurl;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        bookid = intent.getStringExtra("bookid");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please wait");

        binding.downloadid.setVisibility(View.GONE);
        loadbookdetails();
        binding.backid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.downloadid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(PdfDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){

                    MyApplication.downloadbook(PdfDetailsActivity.this, ""+ bookid, ""+booktitle, ""+bookurl);
                }else {
                    requestpermissionlauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });


    }

    private ActivityResultLauncher<String> requestpermissionlauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted){
                    MyApplication.downloadbook(this, "" + bookid, ""+ booktitle, ""+bookurl );
                }else
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            });



    private void loadbookdetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                booktitle = ""+ snapshot.child("title").getValue();
                String catagoryid = ""+ snapshot.child("catagoryid").getValue();
                String description = ""+ snapshot.child("description").getValue();
                String timestamp = ""+ snapshot.child("timestamp").getValue();

                bookurl = "" + snapshot.child("url").getValue();

                binding.downloadid.setVisibility(View.VISIBLE);

                String date = MyApplication.formattimestamp(Long.parseLong(timestamp));

                MyApplication.loadcatagory(catagoryid,binding.catagoryid);

                binding.titleid.setText(booktitle);
                binding.descriptionid.setText(description);
                binding.dateid.setText(date);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}