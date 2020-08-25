package com.obiangetfils.gather.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.obiangetfils.gather.R;
import com.obiangetfils.gather.adapters.AdapterNewsSourceDetail;
import com.obiangetfils.gather.models.Constants;
import com.obiangetfils.gather.models.ModelNewsSourceDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsSourceDetailActivity extends AppCompatActivity {

    private TextView nameTv, descriptionTv, countryTv, categoryTv, languageTv;
    private RecyclerView newsRv;
    private ArrayList<ModelNewsSourceDetail> sourceDetails;
    private AdapterNewsSourceDetail adapterNewsSourceDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_source_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Latest News");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // Bind view
        nameTv = findViewById(R.id.nameTv);
        descriptionTv = findViewById(R.id.descriptionTv);
        countryTv = findViewById(R.id.countryTv);
        categoryTv = findViewById(R.id.categoryTv);
        languageTv = findViewById(R.id.languageTv);
        newsRv = findViewById(R.id.newsRv);

        // Get Data
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String category = intent.getStringExtra("category");
        String country = intent.getStringExtra("country");
        String language = intent.getStringExtra("language");
        actionBar.setTitle(name);

        nameTv.setText(name);
        descriptionTv.setText("description: " + description);
        countryTv.setText("country: " + country);
        categoryTv.setText("category: " + category);
        languageTv.setText("language: " + language);

        loadNewData(id);

    }

    private void loadNewData(String id) {

        sourceDetails = new ArrayList<>();
        String url = "https://newsapi.org/v2/top-headLines?sources=" + id + "&apiKey=" + Constants.API_KEY;

        //ProgressBar
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading Message");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("articles");

                    // get Data
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectNews = jsonArray.getJSONObject(i);
                        String title = jsonObjectNews.getString("title");
                        String description = jsonObjectNews.getString("description");
                        String url = jsonObjectNews.getString("url");
                        String urlToImage = jsonObjectNews.getString("urlToImage");
                        String publishedAt = jsonObjectNews.getString("publishedAt");
                        String content = jsonObjectNews.getString("content");

                        // convert date format
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String formattedDate = "";

                        try {
                            Date date = dateFormat1.parse(publishedAt);
                            formattedDate = dateFormat2.format(date);
                        } catch (Exception e){
                            formattedDate = publishedAt;
                        }

                        ModelNewsSourceDetail modelNewsSourceDetail = new ModelNewsSourceDetail("" + title,
                                "" + description,
                                "" + url,
                                "" + urlToImage,
                                "" + formattedDate,
                                "" + content);
                        sourceDetails.add(modelNewsSourceDetail);
                    }

                    progressDialog.dismiss();
                    adapterNewsSourceDetail = new AdapterNewsSourceDetail(NewsSourceDetailActivity.this, sourceDetails);
                    newsRv.setAdapter(adapterNewsSourceDetail);

                } catch (Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(NewsSourceDetailActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(NewsSourceDetailActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}