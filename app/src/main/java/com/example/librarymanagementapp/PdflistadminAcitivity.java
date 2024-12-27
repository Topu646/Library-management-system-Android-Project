package com.example.librarymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.librarymanagementapp.databinding.ActivityPdflistadminAcitivityBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PdflistadminAcitivity extends AppCompatActivity {

    private ArrayList<Modelpdf>pdfarrayList;
    private AdapterPdfAdmin adapterPdfAdmin;
    private ActivityPdflistadminAcitivityBinding binding;
    private String catagoryid, catagorytitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdflistadminAcitivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent= getIntent();
        catagoryid = intent.getStringExtra("catagoryid");
        catagorytitle = intent.getStringExtra("catagorytitle");
        loadpdflist();
        binding.backid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadpdflist() {
        pdfarrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pdfarrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren())
                        {
                            Modelpdf model = ds.getValue(Modelpdf.class);
                            if(model != null) {
                                if(model.getCatagoryid().equals(catagoryid)) {
                                    pdfarrayList.add(model);
                                }
                            }

                        }
                        adapterPdfAdmin = new AdapterPdfAdmin(PdflistadminAcitivity.this,pdfarrayList);
                        binding.pdfid.setAdapter(adapterPdfAdmin);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

    }
}