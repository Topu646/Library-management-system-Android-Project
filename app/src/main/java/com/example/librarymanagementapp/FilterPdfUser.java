package com.example.librarymanagementapp;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterPdfUser extends Filter {

    ArrayList<Modelpdf> filterlist;
    AdapterPdfUser adapterPdfUser;

    public FilterPdfUser(ArrayList<Modelpdf> filterlist, AdapterPdfUser adapterPdfUser) {
        this.filterlist = filterlist;
        this.adapterPdfUser = adapterPdfUser;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if(constraint.length() > 0 || constraint != null)
        {
            constraint = constraint.toString().toLowerCase();
            ArrayList<Modelpdf> filteredmodels= new ArrayList<>();

            for (int i = 0; i< filterlist.size();i++)
            {
                if(filterlist.get(i).getTitle().toLowerCase().contains(constraint)){
                    filteredmodels.add(filterlist.get(i));
                }
            }

            results.count = filteredmodels.size();
            results.values = filteredmodels;
        }else
        {
            results.count = filterlist.size();
            results.values = filterlist;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapterPdfUser.pdfarraylist = (ArrayList<Modelpdf>) results.values;
        adapterPdfUser.notifyDataSetChanged();
    }
}
