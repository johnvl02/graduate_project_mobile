package com.john.graduate_project_mobile.Activities.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.R;
import com.john.graduate_project_mobile.Security.JWTGenerator;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private SharedPreferences preferences;
    private String token, name;
    private final JWTGenerator jwtGenerator = new JWTGenerator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        requestQueue = Volley.newRequestQueue(this);
        preferences = getSharedPreferences("Profile",MODE_PRIVATE);
        try {
            token = preferences.getString("token", " ");
            if (jwtGenerator.validateToken(token)){
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("name",  preferences.getString("name","  "));
                intent.putExtra("username",  preferences.getString("username","  "));
                startActivity(intent);
            }
            else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.layoutForFragment, new LoginFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void login(String username, String password){
        String url = "http://10.0.2.2:8080/android/login";
        JSONObject body = new JSONObject();
        try {
            body.put("username",username);
            body.put("password", password);
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() == 3){
                    try {
                        Toast.makeText(AuthActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                        token = response.getString("token");
                        name = response.getString("name");
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("token", token);
                        editor.putString("name", name);
                        editor.putString("username", username);
                        editor.apply();
                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        intent.putExtra("token",token);
                        intent.putExtra("name", name);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    try {
                        Toast.makeText(AuthActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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

    public void register(String username, String password, String firstName, String lastName, String email, String phone, String age){
        String url = "http://10.0.2.2:8080/android/register";
        JSONObject body = new JSONObject();
        try {
            body.put("username", username);
            body.put("password", password);
            body.put("firstName", firstName);
            body.put("lastName", lastName);
            body.put("mail", email);
            body.put("phone", phone);
            body.put("age", age);
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("status") == 0){
                        token = response.getString("token");
                        name = firstName + " "+ lastName;
                        changeFragment(new VerificationCodeFragment(username));
                    }
                    else {
                        Toast.makeText(AuthActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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

    public void verificationCode(String code, String username){
        String url = "http://10.0.2.2:8080/android/verification";
        JSONObject body = new JSONObject();
        try {
            body.put("username", username);
            body.put("code", code);
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("page") == 1){
                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        intent.putExtra("token",token);
                        intent.putExtra("name", name);
                        intent.putExtra("name", username);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(AuthActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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


    public void sendEmail(String username, String email){
        String url = "http://10.0.2.2:8080/android/resetPassword";
        JSONObject body = new JSONObject();
        try {
            body.put("username", username);
            body.put("mail", email);
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("find")){
                        Toast.makeText(AuthActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        changeFragment(new NewPasswordFragment(username));
                    }
                    else {
                        Toast.makeText(AuthActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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

    public void newPassword(String code, String pass, String cPass, String username){
        String url = "http://10.0.2.2:8080/android/createNewPassword";
        JSONObject body = new JSONObject();
        try {
            body.put("username", username);
            body.put("password", pass);
            body.put("confirmPassword", cPass);
            body.put("code", code);
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("user")){
                        Toast.makeText(AuthActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
                        changeFragment(new LoginFragment());
                    }
                    else {
                        Toast.makeText(AuthActivity.this, response.getString("msg"), Toast.LENGTH_LONG).show();
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

    public void changeFragment(Fragment myFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutForFragment, myFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}