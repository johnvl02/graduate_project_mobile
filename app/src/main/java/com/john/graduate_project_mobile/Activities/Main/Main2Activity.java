package com.john.graduate_project_mobile.Activities.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.john.graduate_project_mobile.R;

public class Main2Activity extends AppCompatActivity {
    private ImageView Imview;
    private RequestQueue requestQueue;
    private ObjectMapper mapper;

    public Main2Activity(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Main2Activity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        requestQueue = Volley.newRequestQueue(this);
        //Imview = findViewById(R.id.);

    }


}