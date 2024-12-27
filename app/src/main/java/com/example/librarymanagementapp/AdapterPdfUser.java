package com.example.librarymanagementapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementapp.databinding.SamplePdfUserBinding;

import java.util.ArrayList;

public class AdapterPdfUser extends  RecyclerView.Adapter<AdapterPdfUser.HolderPdfUser> implements Filterable {
    private Context context;
    public ArrayList<Modelpdf>pdfarraylist,filterlist;
    private FilterPdfUser filter;

    private SamplePdfUserBinding binding;

    public AdapterPdfUser(Context context, ArrayList<Modelpdf> pdfarraylist) {
        this.context = context;
        this.pdfarraylist = pdfarraylist;
        this.filterlist = pdfarraylist;
    }

    @NonNull
    @Override
    public HolderPdfUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SamplePdfUserBinding.inflate(LayoutInflater.from(context),parent, false);

        return new HolderPdfUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfUser holder, int position) {

        Modelpdf model = pdfarraylist.get(position);
        String bookid = model.getId();
        String title = model.getTitle();
        String description = model.getDescription();
        String catagoryid = model.getCatagoryid();
        String pdfurl = model.getUrl();
        long timestamp = model.getTimestamp();
        String date = MyApplication.formattimestamp(timestamp);


        holder.titletv.setText(title);
        holder.descriptiontv.setText(description);
        MyApplication.loadcatagory(""+catagoryid, holder.catagorytv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, PdfDetailsActivity.class);

                intent.putExtra("bookid", bookid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfarraylist.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null)
        {
            filter = new FilterPdfUser(filterlist, this);
        }
        return filter;
    }

    class HolderPdfUser extends RecyclerView.ViewHolder{

        TextView titletv, descriptiontv, catagorytv;

        public HolderPdfUser(@NonNull View itemView) {
            super(itemView);

            titletv = binding.titleid;
            descriptiontv = binding.descid;
            catagorytv = binding.catagoryid;

        }
    }
}
