package com.example.librarymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.librarymanagementapp.databinding.ActivityPdfAddBinding;
import com.example.librarymanagementapp.databinding.ActivityPdfEditBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PdfEditActivity extends AppCompatActivity {
    private ActivityPdfEditBinding binding;
    private ProgressDialog progressDialog;
    private ArrayList<String>catagorytitlearraylist,catagoryidarraylist;

    private String bookid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bookid = getIntent().getStringExtra("bookid");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadpdfcatagories();
        loadbookinfo();
        
        binding.catagoryid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catagorypicker();
            }
        });
        
        binding.uploadid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validdata();
            }
        });
    }

    String title, description;

    private void validdata() {

        title = binding.titleid.getText().toString().trim();
        description = binding.descid.getText().toString().trim();


        if(TextUtils.isEmpty(title))
        {
            Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show();
        }else  if(TextUtils.isEmpty(description))
        {
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(selectedcatagorytitle))
        {
            Toast.makeText(this, "Enter Category", Toast.LENGTH_SHORT).show();
        }else
        {
            updatepdf();
        }
    }

    private void updatepdf() {
        progressDialog.setMessage("Updating info");
        progressDialog.show();

        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("title",""+title);
        hashMap.put("description",""+description);
        hashMap.put("catagoryid",""+selectedcatagoryid);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookid)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfEditActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfEditActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadbookinfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        selectedcatagoryid = ""+snapshot.child("catagoryid").getValue();
                        String title = ""+snapshot.child("title").getValue();
                        String description = ""+snapshot.child("description").getValue();

                        binding.titleid.setText(title);
                        binding.descid.setText(description);

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Catagories");
                        ref.child(selectedcatagoryid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String catagory = ""+snapshot.child("catagory").getValue();
                                        binding.catagoryid.setText(catagory);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    String selectedcatagoryid, selectedcatagorytitle;

    private void catagorypicker() {

        String[] catagoriesarray = new String[catagorytitlearraylist.size()];
        for (int i = 0; i< catagorytitlearraylist.size(); i++)
        {
            catagoriesarray[i] = catagorytitlearraylist.get(i);
        }
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Category")
                .setItems(catagoriesarray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedcatagorytitle= catagorytitlearraylist.get(which);
                        selectedcatagoryid= catagoryidarraylist.get(which);

                        binding.catagoryid.setText(selectedcatagorytitle);

                    }
                }).show();

    }

    private void loadpdfcatagories() {

        catagorytitlearraylist = new ArrayList<>();
        catagoryidarraylist = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Catagories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                catagorytitlearraylist.clear();
                catagoryidarraylist.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String catagoryid = "" +dataSnapshot.child("id").getValue();
                    String catagorytitle = "" +dataSnapshot.child("catagory").getValue();
                    catagoryidarraylist.add(catagoryid);
                    catagorytitlearraylist.add(catagorytitle);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}