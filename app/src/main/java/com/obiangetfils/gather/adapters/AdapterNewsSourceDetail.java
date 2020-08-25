package com.obiangetfils.gather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.obiangetfils.gather.R;
import com.obiangetfils.gather.models.ModelNewsSourceDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterNewsSourceDetail extends RecyclerView.Adapter<AdapterNewsSourceDetail.NewsSourceDetailViewHolder> {

    private Context mContext;
    private ArrayList<ModelNewsSourceDetail> newsSourceDetailArrayList;

    public AdapterNewsSourceDetail(Context mContext, ArrayList<ModelNewsSourceDetail> newsSourceDetailArrayList) {
        this.mContext = mContext;
        this.newsSourceDetailArrayList = newsSourceDetailArrayList;
    }

    @NonNull
    @Override
    public NewsSourceDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_news_source_details, parent, false);
        return new NewsSourceDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsSourceDetailViewHolder holder, int position) {

        ModelNewsSourceDetail modelNewsSourceDetail = newsSourceDetailArrayList.get(position);
        String title, description, url, urlToImage, publishedAt, content;

        title = modelNewsSourceDetail.getTitle();
        description = modelNewsSourceDetail.getDescription();
        url = modelNewsSourceDetail.getUrl();
        urlToImage = modelNewsSourceDetail.getUrlToImage();
        publishedAt = modelNewsSourceDetail.getPublishedAt();
        content = modelNewsSourceDetail.getContent();

        // set Data
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.dateTv.setText(publishedAt);
        Picasso.get().load(urlToImage).into(holder.imageTv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return newsSourceDetailArrayList.size();
    }

    public class NewsSourceDetailViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv, descriptionTv, dateTv, readMore;
        ImageView imageTv;

        public NewsSourceDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTv = (ImageView) itemView.findViewById(R.id.imageTv);
            titleTv = (TextView) itemView.findViewById(R.id.titleTv);
            descriptionTv = (TextView) itemView.findViewById(R.id.descriptionTv);
            dateTv = (TextView) itemView.findViewById(R.id.dateTv);
            readMore = (TextView) itemView.findViewById(R.id.readMore);
        }
    }
}
