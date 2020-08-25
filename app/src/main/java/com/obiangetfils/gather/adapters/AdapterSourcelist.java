package com.obiangetfils.gather.adapters;

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

import com.obiangetfils.gather.FilterSourceList;
import com.obiangetfils.gather.R;
import com.obiangetfils.gather.controllers.NewsSourceDetailActivity;
import com.obiangetfils.gather.models.ModelSourceList;

import java.util.ArrayList;

public class AdapterSourcelist extends RecyclerView.Adapter<AdapterSourcelist.HolderSourceList> implements Filterable {

    private Context context;
    public ArrayList<ModelSourceList> modelSourceLists, filterList;
    private FilterSourceList filter;

    public AdapterSourcelist(Context context, ArrayList<ModelSourceList> modelSourceLists) {
        this.context = context;
        this.modelSourceLists = modelSourceLists;
        this.filterList = modelSourceLists;
    }

    @NonNull
    @Override
    public HolderSourceList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_source_list, parent, false);
        return new HolderSourceList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSourceList holder, int position) {

        // get Data
        ModelSourceList model = modelSourceLists.get(position);
        final String id = model.getId();
        final String name = model.getName();
        final String description = model.getDescription();
        final String category = model.getCategory();
        final String language = model.getLanguage();
        final String country = model.getCountry();

        // Set Data to Ui view
        holder.nameTv.setText(name);
        holder.descriptionTv.setText(description);
        holder.categoryTv.setText("Category: " + category);
        holder.languageTv.setText("Language: " + language);
        holder.countryTv.setText("Country: " + country);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, NewsSourceDetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("description", description);
                intent.putExtra("category", category);
                intent.putExtra("country", country);
                intent.putExtra("language", language);
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return modelSourceLists.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterSourceList(this, filterList);
        }

        return filter;
    }

    public class HolderSourceList extends RecyclerView.ViewHolder {

        TextView nameTv, descriptionTv, countryTv, categoryTv, languageTv;

        public HolderSourceList(@NonNull View itemView) {
            super(itemView);

            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            descriptionTv = (TextView) itemView.findViewById(R.id.descriptionTv);
            countryTv = (TextView) itemView.findViewById(R.id.countryTv);
            categoryTv = (TextView) itemView.findViewById(R.id.categoryTv);
            languageTv = (TextView) itemView.findViewById(R.id.languageTv);

        }
    }
}
