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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librarymanagementapp.databinding.SamplePdfAdminBinding;

import java.util.ArrayList;

public class AdapterPdfAdmin extends RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin> {

    private Context context;
    private ArrayList<Modelpdf>pdfArrayList;
    private SamplePdfAdminBinding binding;


    public AdapterPdfAdmin(Context context, ArrayList<Modelpdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
    }

    @NonNull
    @Override
    public HolderPdfAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SamplePdfAdminBinding.inflate(LayoutInflater.from(context),parent,false);

        return new HolderPdfAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfAdmin holder, int position) {

        Modelpdf model = pdfArrayList.get(position);
        String pdfid = model.getId();
        String catagoryid = model.getCatagoryid();
        String pdfurl = model.getUrl();
        String title = model.getTitle();
        String description = model.getDescription();
        long timestamp = model.getTimestamp();
        String formattedDate = MyApplication.formattimestamp(timestamp);

        holder.titleid.setText(title);
        holder.descid.setText(description);
        MyApplication.loadcatagory(""+catagoryid,holder.catagoryid);


        holder.morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreoptiondialog(model,holder);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PdfDetailsActivity.class);
                intent.putExtra("bookid",pdfid);
                context.startActivity(intent);
            }
        });

    }

    private void moreoptiondialog(Modelpdf model, HolderPdfAdmin holder) {
        String bookid = model.getId();
        String booktitle = model.getTitle();
        String bookurl = model.getUrl();
        String[] options = {"Edit" , "Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Choose options")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0)
                        {

                            Intent intent= new Intent(context, PdfEditActivity.class);
                            intent.putExtra("bookid",bookid);
                            context.startActivity(intent);
                        }else if(which == 1)
                        {

                            MyApplication.deletebook(context, ""+bookid, ""+booktitle,""+bookurl);

                        }
                    }
                }).show();
    }







    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    class HolderPdfAdmin extends RecyclerView.ViewHolder{

        TextView titleid, descid,catagoryid;

        ImageButton morebtn;
        public HolderPdfAdmin(@NonNull View itemView) {
            super(itemView);

            titleid = binding.titleid;
            descid = binding.descid;
            catagoryid =binding.catagoryid;
            morebtn = binding.moreid;
        }
    }
}
