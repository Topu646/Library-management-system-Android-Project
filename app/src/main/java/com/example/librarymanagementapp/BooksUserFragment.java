package com.example.librarymanagementapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.librarymanagementapp.databinding.FragmentBooksUserBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BooksUserFragment extends Fragment {

    private String catagoryid;
    private String catagory;
    private String uid;

    private ArrayList<Modelpdf> pdfarraylist;
    private AdapterPdfUser adapterPdfUser;

    private FragmentBooksUserBinding binding;


    public BooksUserFragment() {

    }


    public static BooksUserFragment newInstance(String catagoryid, String catagory, String uid) {
        BooksUserFragment fragment = new BooksUserFragment();
        Bundle args = new Bundle();
        args.putString("catagoryid", catagoryid);
        args.putString("catagory", catagory);
        args.putString("uid", uid);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catagoryid = getArguments().getString("catagoryid");
            catagory = getArguments().getString("catagory");
            uid = getArguments().getString("uid");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBooksUserBinding.inflate(LayoutInflater.from(getContext()),container,false);

        if(catagory.equals("All"))
        {
            pdfarraylist = new ArrayList<>();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pdfarraylist.clear();
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        Modelpdf model = ds.getValue(Modelpdf.class);
                        pdfarraylist.add(model);


                    }
                    adapterPdfUser= new AdapterPdfUser(getContext(),pdfarraylist);
                    binding.booksrv.setAdapter(adapterPdfUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else
        {
            pdfarraylist = new ArrayList<>();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
            ref.orderByChild("catagoryid").equalTo(catagoryid)
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pdfarraylist.clear();
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        Modelpdf model = ds.getValue(Modelpdf.class);
                        pdfarraylist.add(model);


                    }
                    adapterPdfUser= new AdapterPdfUser(getContext(),pdfarraylist);
                    binding.booksrv.setAdapter(adapterPdfUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        binding.searchid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    adapterPdfUser.getFilter().filter(s);
                }catch (Exception e)
                {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }
}