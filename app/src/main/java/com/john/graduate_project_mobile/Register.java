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

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    private EditText username, password, email, firstName, lastName, phone, age;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.editTextText);
        password = findViewById(R.id.editTextTextPassword);
        email = findViewById(R.id.editTextTextEmailAddress);
        firstName = findViewById(R.id.editTextText3);
        lastName = findViewById(R.id.editTextText4);
        phone = findViewById(R.id.editTextPhone);
        age = findViewById(R.id.editTextNumber);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void register(View view){
        String url = "http://10.0.2.2:8080/android/register";
        JSONObject body = new JSONObject();
        try {
            body.put("username", username.getText());
            body.put("password", password.getText());
            body.put("firstName", firstName.getText());
            body.put("lastName", lastName.getText());
            body.put("mail", email.getText());
            body.put("phone", phone.getText());
            body.put("age", age.getText());
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                   if (response.getInt("status") == 0){
                        //TODO go to the activate account
                   }
                   else {
                       Toast.makeText(Register.this, response.getString("msg").toString(), Toast.LENGTH_LONG).show();
                   }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
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
}