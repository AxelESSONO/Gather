package com.obiangetfils.gather.controllers;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.obiangetfils.gather.R;
import com.obiangetfils.gather.adapters.AdapterSourcelist;
import com.obiangetfils.gather.models.Constants;
import com.obiangetfils.gather.models.ModelSourceList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText searchEt;
    private ImageButton filterBtn;
    private RecyclerView sourceRV;

    private ArrayList<ModelSourceList> sourceLists;
    private AdapterSourcelist adapterSourcelist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        searchEt = (EditText) findViewById(R.id.searchEt);
        filterBtn = (ImageButton) findViewById(R.id.filterBtn);
        sourceRV = (RecyclerView) findViewById(R.id.sourceRV);

        loadSoures();

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                try {
                    adapterSourcelist.getFilter().filter(charSequence);
                } catch (Exception e) {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void loadSoures() {
        sourceLists = new ArrayList<>();
        sourceLists.clear();

        progressBar.setVisibility(View.VISIBLE);
        //Request Data
        String url = "https://newsapi.org/v2/sources?apiKey=" + Constants.API_KEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // response is got as String
                try {

                    //Convert String to JSon Object
                    JSONObject jsonObject = new JSONObject(response);
                    // Get Source Array from that object
                    JSONArray jsonArray = jsonObject.getJSONArray("sources");

                    //Get All data from that Array using loop
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        String id = jsonObject1.getString("id");
                        String name = jsonObject1.getString("name");
                        String description = jsonObject1.getString("description");
                        String url = jsonObject1.getString("url");
                        String category = jsonObject1.getString("category");
                        String language = jsonObject1.getString("language");
                        String country = jsonObject1.getString("country");

                        ModelSourceList modelSourceList = new ModelSourceList("" + id,
                                "" + name,
                                "" + description,
                                "" + url,
                                "" + category,
                                "" + language,
                                "" + country);

                        sourceLists.add(modelSourceList);
                    }
                    progressBar.setVisibility(View.GONE);
                    adapterSourcelist = new AdapterSourcelist(MainActivity.this, sourceLists);
                    sourceRV.setAdapter(adapterSourcelist);

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}