package com.obiangetfils.gather.controllers;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

    private String selectedCountry = "All", selectedCategories = "All", selectedLanguage = "All";
    private int selectedCountryPosition = 0, selectedCategoryPosition = 0, selectedLanguagePosition = 0;

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
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBottomSheet();
            }
        });

    }

    private void filterBottomSheet() {
        View view = LayoutInflater.from(this).inflate(R.layout.filter_layout, null);
        Spinner countrySpinner, categorySpinner, languageSpinner;
        TextView countryLabelTv, categoryLabelTv, languageLabelTv;
        Button applyBtn;

        countrySpinner = (Spinner) view.findViewById(R.id.countrySpinner);
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        languageSpinner = (Spinner) view.findViewById(R.id.languageSpinner);

        countryLabelTv = (TextView) view.findViewById(R.id.countryLabelTv);
        categoryLabelTv = (TextView) view.findViewById(R.id.categoryLabelTv);
        languageLabelTv = (TextView) view.findViewById(R.id.languageLabelTv);

        applyBtn = (Button) view.findViewById(R.id.applyBtn);

        ArrayAdapter<String> adapterCountries = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Constants.COUNTRIES);
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Constants.CATEGORIES);
        ArrayAdapter<String> adapterLanguages = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Constants.LANGUAGES);

        adapterCountries.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapterLanguages.setDropDownViewResource(android.R.layout.simple_spinner_item);

        countrySpinner.setAdapter(adapterCountries);
        categorySpinner.setAdapter(adapterCategories);
        languageSpinner.setAdapter(adapterLanguages);

        countrySpinner.setSelection(selectedCountryPosition);
        categorySpinner.setSelection(selectedCategoryPosition);
        languageSpinner.setSelection(selectedLanguagePosition);

        //spinner item selected listener
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = Constants.COUNTRIES[position];
                selectedCountryPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategories = Constants.CATEGORIES[position];
                selectedCategoryPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguage = Constants.LANGUAGES[position];
                selectedLanguagePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                loadSoures();
            }
        });

    }

    private void loadSoures() {

        //show selected filter option in actionBar
        getSupportActionBar().setSubtitle("Country: "+selectedCountry+ " Category: "+ selectedCategories+ " Language: "+selectedLanguage);

        if (selectedCountry.equals("All")){
            selectedCountry = "";
        }

        if (selectedCategories.equals("All")){
            selectedCategories = "";
        }

        if (selectedLanguage.equals("All")){
            selectedLanguage = "";
        }


        sourceLists = new ArrayList<>();
        sourceLists.clear();

        progressBar.setVisibility(View.VISIBLE);
        //Request Data
        String url = "https://newsapi.org/v2/sources?apiKey=" + Constants.API_KEY+"&country=" +selectedCountry + "&category"+selectedCategories+ "&language"+selectedLanguage;
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

                        ModelSourceList modelSourceList = new ModelSourceList(
                                "" + id,
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