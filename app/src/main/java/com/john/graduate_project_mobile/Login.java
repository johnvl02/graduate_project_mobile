package com.john.graduate_project_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.john.graduate_project_mobile.Activities.Main.Main2Activity;
import com.john.graduate_project_mobile.Security.JWTGenerator;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private EditText username, password;
    private SharedPreferences preferences;
    private RequestQueue requestQueue;
    private String token;
    private final JWTGenerator jwtGenerator = new JWTGenerator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        requestQueue = Volley.newRequestQueue(this);
        preferences = getPreferences(MODE_PRIVATE);
        try {
            token = preferences.getString("token", " ");
            if (jwtGenerator.validateToken(token)){
                Intent intent = new Intent(Login.this, Main2Activity.class);
                intent.putExtra("token",token);
                intent.putExtra("name",  preferences.getString("name","  "));
                startActivity(intent);
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void login(View view){
        String url = "http://10.0.2.2:8080/android/login";
        JSONObject body = new JSONObject();
        try {
            body.put("username",username.getText());
            body.put("password", password.getText());
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() == 3){
                    try {
                        Toast.makeText(Login.this, response.getString("msg").toString(), Toast.LENGTH_LONG).show();
                        token = response.getString("token");
                        String name = response.getString("name");
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("token", token);
                        editor.putString("name", name);
                        editor.apply();
                        Intent intent = new Intent(Login.this, Main2Activity.class);
                        intent.putExtra("token",token);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    try {
                        Toast.makeText(Login.this, response.getString("msg").toString(), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getLocalizedMessage());
            }
        });
        requestQueue.add(request);
    }

    public void register(View view){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

   /* private void login2(JSONObject newEmergencies, JSONObject Alerts){
        String url = "http://10.0.2.2:8080/emergence";
        JSONObject body = new JSONObject();
        try {
            System.out.println("jfka");
            body.put("da","d");
           *//* body.put("alerts",Alerts);
            body.put("newEmergencies",newEmergencies);*//*
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,
                url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                *//*try {
                    result = new ArrayList<>();
                    tempAddress = new HashMap<>();
                    int size = response.length();
                    if (size>2) {
                        for (int i = 0; i < size-2; i++) {
                            result.add(response.getString(String.valueOf(i)));
                            String[] temp = response.getString(String.valueOf(i)).split("\\|");
                            String[] center =temp[1].split("=");
                            String[] loc = center[1].split(",");
                            final int j=i;
                            new Thread(()->{
                                List<Address> addresses;
                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                try {
                                    addresses = geocoder.getFromLocation(Double.parseDouble(loc[0]),Double.parseDouble(loc[1]),1);
                                    tempAddress.put(String.valueOf(j),addresses.get(0).getAddressLine(0));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }).start();
                        }
                        if (!response.getString("updateAlerts").isEmpty()) {
                            updateAlerts(response.getString("updateAlerts"));
                        }
                        if (!response.getString("oldEmergencies").isEmpty()){
                            updateEmergencies(response.getString("oldEmergencies"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*//*
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getLocalizedMessage();
            }
        });
        requestQueue.add(postRequest);
    }*/
}