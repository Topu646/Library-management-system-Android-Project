package com.example.librarymanagementapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementapp.databinding.SampleCategoryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterCatagory extends  RecyclerView.Adapter<AdapterCatagory.HolderCatagory>{

    private Context context;
    private ArrayList<ModelCategory>catagorylist;
    private SampleCategoryBinding binding;

    public AdapterCatagory(Context context, ArrayList<ModelCategory> catagorylist) {
        this.context = context;
        this.catagorylist = catagorylist;

    }

    @NonNull
    @Override
    public HolderCatagory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SampleCategoryBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderCatagory(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCatagory holder, int position) {

        ModelCategory modelCategory = catagorylist.get(position);
        String uid =modelCategory.getUid();
        String id =modelCategory.getId();
        String catagory =modelCategory.getCatagory();
        long timestamp =modelCategory.getTimestamp();
        holder.category.setText(catagory);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Delete")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deletecatagory(modelCategory,holder);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PdflistadminAcitivity.class);
                ModelCategory modelCategory1 = catagorylist.get(holder.getAdapterPosition());
                intent.putExtra("catagoryid",modelCategory1.getId());
                intent.putExtra("catagorytitle",modelCategory1.getCatagory());
                context.startActivity(intent);
            }
        });


    }

    private void deletecatagory(ModelCategory modelCategory, HolderCatagory holder) {
        String id = modelCategory.getId();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Catagories");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return catagorylist.size();
    }

    class HolderCatagory extends RecyclerView.ViewHolder{
        TextView category;
        ImageButton delete;

        public HolderCatagory(@NonNull View itemView) {
            super(itemView);
            category = binding.catagoryid;
            delete = binding.deleteid;

        }
    }


}
