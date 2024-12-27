package com.example.librarymanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.librarymanagementapp.databinding.ActivityPdfAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class PdfAddActivity extends AppCompatActivity {
    private ActivityPdfAddBinding binding;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private static final int PDF_PICK_CODE = 1000;
    private Uri pdfuri=null;
    private ArrayList<String> categorytitleArrayList,catagoryidarraylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        loadpdfcatagories();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please wait");

        binding.backid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        binding.addfileid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfpick();
            }
        });
        binding.catagoryid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catagorypicker();
            }
        });
        binding.uploadid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
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
        }else  if(pdfuri == null)
        {
            Toast.makeText(this, "Select pdf", Toast.LENGTH_SHORT).show();
        }else
        {
            uptostorage();
        }
    }

    private void uptostorage() {

        long timestamp = System.currentTimeMillis();
        String filepath = "Books/" + timestamp;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filepath);
        storageReference.putFile(pdfuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri>uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while ((!uriTask.isSuccessful()));
                        String uploadedpdfurl = ""+uriTask.getResult();
                        uploadtodb(uploadedpdfurl,timestamp);
                        //Toast.makeText(PdfAddActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PdfAddActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadtodb(String uploadedpdfurl, long timestamp) {
        String uid = firebaseAuth.getUid();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",""+uid);
        hashMap.put("id",""+timestamp);
        hashMap.put("title",""+title);
        hashMap.put("description",""+description);
        hashMap.put("catagoryid",""+selectedcatagoryid);
        hashMap.put("url",""+uploadedpdfurl);
        hashMap.put("timestamp",timestamp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfAddActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(PdfAddActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void loadpdfcatagories() {

        categorytitleArrayList = new ArrayList<>();
        catagoryidarraylist = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Catagories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categorytitleArrayList.clear();
                catagoryidarraylist.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    String catagoryid = "" +dataSnapshot.child("id").getValue();
                    String catagorytitle = "" +dataSnapshot.child("catagory").getValue();
                    catagoryidarraylist.add(catagoryid);
                    categorytitleArrayList.add(catagorytitle);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    String selectedcatagoryid, selectedcatagorytitle;
    private void catagorypicker() {

        String[] catagoriesarray = new String[categorytitleArrayList.size()];
        for (int i = 0; i< categorytitleArrayList.size(); i++)
        {
            catagoriesarray[i] = categorytitleArrayList.get(i);
        }
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Category")
                .setItems(catagoriesarray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedcatagorytitle= categorytitleArrayList.get(which);
                        selectedcatagoryid= catagoryidarraylist.get(which);
                        binding.catagoryid.setText(selectedcatagorytitle);

                    }
                }).show();

    }

    private void pdfpick() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF"),PDF_PICK_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if(requestCode == PDF_PICK_CODE)
            {

                pdfuri = data.getData();

            }

        }else
        {

            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }

    }
}