package com.john.graduate_project_mobile.Activities.Main;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.john.graduate_project_mobile.Activities.Auth.AuthActivity;
import com.john.graduate_project_mobile.Activities.Main.ui.Rent.RentFragment;
import com.john.graduate_project_mobile.R;
import com.john.graduate_project_mobile.Security.JWTGenerator;
import com.john.graduate_project_mobile.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private NavigationView headNav;
    private TextView textName, textUsername;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private String username;
    private String token;
    private static final JWTGenerator jwt = new JWTGenerator();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_rent, R.id.nav_addcar, R.id.nav_requestsFor, R.id.nav_requestsMy, R.id.nav_profile, R.id.nav_maps, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        username = getIntent().getStringExtra("username");
        token = getIntent().getStringExtra("token");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        textName = findViewById(R.id.nav_view).findViewById(R.id.textView7);
        textUsername = findViewById(R.id.nav_view).findViewById(R.id.textView);
        textName.setText(getIntent().getStringExtra("name"));
        textUsername.setText(getIntent().getStringExtra("username"));
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void changeFragment(Fragment myFragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, myFragment,"home");
        fragmentTransaction.commit();

    }


    public void submitInfo(String date, String info){
        if (jwt.validateToken(token)) {
            String url = "http://10.0.2.2:8080/android/moreInfo";
            JSONObject body = new JSONObject();
            try {
                body.put("token", token);
                body.put("date", date);
                body.put("info", info);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Toast.makeText(MainActivity.this, response.get("msg").toString(),Toast.LENGTH_SHORT).show();
                        navigateTo(R.id.nav_rent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i : error.networkResponse.data)
                        stringBuilder.append((char) i);
                    String[] temp = stringBuilder.toString().split(":");
                    String s = temp[1].replace("\"}","").replace("\"","");
                    Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
                    System.out.println(error.getLocalizedMessage());
                }
            });
            requestQueue.add(request);
        }
        else
            System.out.println("TOKEN expired");
    }

    public String getToken(){
        if (jwt.validateToken(token))
            return token;
        else return null;
    }

    public String getUsername(){
        if (jwt.validateToken(token))
            return jwt.getUsernameJWT(token);
        else return null;
    }

    public void getImages(String name, ImageView imageView){
        String url = "http://10.0.2.2:8080/android/image?name="+name;
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(imageRequest);
    }

    public void navigateTo(int id){
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.navigate(id);
    }

    public void backArrow(int id, int id2){
        DrawerLayout layout = binding.drawerLayout;
        Toolbar toolbar = binding.appBarMain.toolbar;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,layout, toolbar,R.string.open, R.string.close);
        layout.addDrawerListener(toggle);
        toggle.syncState();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) ->{
            if(navDestination.getId() == id){
                toggle.setDrawerIndicatorEnabled(false);
                System.out.println("arrow");
            }
        } );
        toggle.setToolbarNavigationClickListener(v -> {
            toggle.setDrawerIndicatorEnabled(true);
            navController.navigate(id2);
            System.out.println("arrow2");
        });
    }

    public void logout(){
        SharedPreferences preferences = getSharedPreferences("Profile",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("token");
        editor.remove("name");
        editor.remove("username");
        editor.apply();
    }

    private void showMessageWithOk(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    public void showMessage2(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }

}