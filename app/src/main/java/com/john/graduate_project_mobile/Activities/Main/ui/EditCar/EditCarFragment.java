package com.john.graduate_project_mobile.Activities.Main.ui.EditCar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.john.graduate_project_mobile.Activities.Auth.AuthActivity;
import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.Activities.Main.ui.Profile.MyProfileFragment;
import com.john.graduate_project_mobile.R;
import com.john.graduate_project_mobile.Security.SecurityConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class EditCarFragment extends Fragment implements OnMapReadyCallback {
    private TextView title ;
    private CheckBox cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday, cbSaturday, cbSunday, cbAll;
    private List<String> days = new ArrayList<>();
    private LatLng mark;
    private EditText value, description;
    private ImageView imageView;
    private Boolean haveImage = false;
    private Boolean newImage = false;
    private SearchView searchView;
    private GoogleMap mMap;
    private MapView mapView;
    private Button submit;
    private JSONObject car;
    private EditCarViewModel mViewModel;
    private RequestQueue requestQueue;
    private String token;

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                    imageView.setImageURI(uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(EditCarViewModel.class);
        requestQueue = Volley.newRequestQueue(this.getContext());
        if (getActivity() instanceof MainActivity){
            token = ((MainActivity) getActivity()).getToken();
            ((MainActivity) getActivity()).backArrow(R.id.nav_editCar, R.id.nav_profile);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_car, container, false);
        title = view.findViewById(R.id.textViewTitle);


        value = view.findViewById(R.id.editTextTextValue2);
        description = view.findViewById(R.id.editTextTextMultiLineDescription2);

        cbMonday = view.findViewById(R.id.checkBoxMonday2);
        cbMonday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbTuesday = view.findViewById(R.id.checkBoxTuesday2);
        cbTuesday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbWednesday = view.findViewById(R.id.checkBoxWednesday2);
        cbWednesday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbThursday = view.findViewById(R.id.checkBoxThursday2);
        cbThursday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbFriday = view.findViewById(R.id.checkBoxFriday2);
        cbFriday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbSaturday = view.findViewById(R.id.checkBoxSaturday2);
        cbSaturday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbSunday = view.findViewById(R.id.checkBoxSunday2);
        cbSunday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbAll = view.findViewById(R.id.checkBoxAll2);
        cbAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.clear();
                days.add(buttonView.getText().toString());
                cbMonday.setChecked(false);
                cbTuesday.setChecked(false);
                cbWednesday.setChecked(false);
                cbThursday.setChecked(false);
                cbFriday.setChecked(false);
                cbSaturday.setChecked(false);
                cbSunday.setChecked(false);
            }
            else {
                days.remove(buttonView.getText());
            }
        });

        searchView = view.findViewById(R.id.searchView2);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();

                if (location != null){
                    new Thread(()->{
                        List<Address> addressList = null;
                        Geocoder geocoder = new Geocoder(requireContext());
                        try {
                            addressList = geocoder.getFromLocationName(location,1);
                            if (addressList.size()==0){
                                if (getActivity() instanceof MainActivity){
                                    ((MainActivity) getActivity()).runOnUiThread(() ->showMessage("Error Message", "The address you have entered is not correct"));
                                }
                            }
                            else {
                                Address address = addressList.get(0);
                                mark = new LatLng(address.getLatitude(),address.getLongitude());
                                if (getActivity() instanceof MainActivity){
                                    ((MainActivity) getActivity()).runOnUiThread(() ->addMarker());
                                }
                            }

                        } catch (IOException e) {
                            Toast.makeText(getContext(), "Something went wrong",Toast.LENGTH_LONG).show();
                        }
                    }).start();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Bundle mapViewBundle = null;
        if (savedInstanceState != null)
            mapViewBundle = savedInstanceState.getBundle(SecurityConstants.MAPVIEW_BUNDLE_KEY);
        mapView = view.findViewById(R.id.mapView3);
        mapView.onCreate(mapViewBundle );
        mapView.getMapAsync(this);

        imageView = view.findViewById(R.id.imageViewPhoto2);
        imageView.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
            haveImage = true;
            newImage = true;
        });

        submit = view.findViewById(R.id.button20);
        submit.setOnClickListener(v -> {
            showMessageWithOk();
        });
        getCar();

        return view;
    }

    private void getCar(){
        if (token != null) {
            String url = "http://10.0.2.2:8080/android/editCar?license="+mViewModel.getData().getValue();
            JSONObject body = new JSONObject();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response);
                            if (response.getString("msg").equals("good")){
                                car = response;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(getString(R.string.editCarTitle)).append(response.getString("license"));
                                title.setText(stringBuilder.toString());
                                String d = response.getString("availableDays");
                                String[] temp = d.split(", ");
                                setDays(Arrays.asList(temp));
                                value.setText(response.getString("value"));
                                description.setText(response.getString("description"));
                                if (getActivity() instanceof MainActivity){
                                    ((MainActivity) getActivity()).getImages(response.getString("license"), imageView);
                                    haveImage= true;
                                }
                                String[] temp2 = response.getString("address").split(",");
                                mark = new LatLng(Double.parseDouble(temp2[0]), Double.parseDouble(temp2[1]));
                                addMarker();
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
            } else{
                System.out.println("TOKEN expired");
                Toast.makeText(getContext(), "You have to login first", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                startActivity(intent);
            }
    }
    private void submit(){
        if (token != null) {
            String url = "http://10.0.2.2:8080/android/editCar";
            JSONObject body = new JSONObject();
            String v = getValue();
            String de = getDescription();
            String d = getDays();
            String img = getImage(((BitmapDrawable) imageView.getDrawable()).getBitmap());
            if (!haveImage) {
                Toast.makeText(getContext(), "Please select a image for the car", Toast.LENGTH_LONG).show();
                return;
            }
            if (mark == null) {
                Toast.makeText(getContext(), "Please fill in the address of the car", Toast.LENGTH_LONG).show();
                return;
            }
            String address = mark.latitude + "," + mark.longitude;
            if (v != null && de != null && d != null) {
                try {

                    body.put("token", token);
                    body.put("license", car.getString("license"));
                    body.put("value", v);
                    body.put("description", de);
                    body.put("days", d);
                    body.put("address", address);
                    body.put("image", img);
                    body.put("new image", newImage);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("msg").contains("successfully")) ;
                            Toast.makeText(getContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                            if (getActivity() instanceof MainActivity) {
                                FragmentManager fragmentManager = getParentFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.remove(EditCarFragment.this);
                                fragmentTransaction.commit();
                                ((MainActivity) getActivity()).navigateTo(R.id.nav_home);
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
        else{
            System.out.println("TOKEN expired");
            Toast.makeText(getContext(), "You have to login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);
        }
    }

    private String getDays(){
        if (days.size() == 0){
            Toast.makeText(getContext(), "Please select the days that the car is available", Toast.LENGTH_LONG).show();
            return null;
        }
        else {
            StringBuilder res = new StringBuilder();
            for (String s: days) {
                res.append(s);
            }
            return res.toString();
        }
    }

    private void setDays(List<String> list){
        for (String s:list) {
            switch (s) {
                case "Monday":
                    cbMonday.setChecked(true);
                    break;
                case "Tuesday":
                    cbTuesday.setChecked(true);
                    break;
                case "Wednesday":
                    cbWednesday.setChecked(true);
                    break;
                case "Thursday":
                    cbThursday.setChecked(true);
                    break;
                case "Friday":
                    cbFriday.setChecked(true);
                    break;
                case "Saturday":
                    cbSaturday.setChecked(true);
                    break;
                case "Sunday":
                    cbSunday.setChecked(true);
                    break;
                case "All":
                    cbAll.setChecked(true);
                    break;
            }
        }
    }

    private String getValue(){
        String v = value.getText().toString();
        if (Objects.equals(v, "")){
            Toast.makeText(getContext(), "Please fill in the value of the car",Toast.LENGTH_LONG).show();
            return null;
        }
        else
            return v;
    }

    private String getDescription(){
        String d = description.getText().toString();
        if (Objects.equals(d, "")){
            Toast.makeText(getContext(), "Please fill in the description of the car",Toast.LENGTH_LONG).show();
            return null;
        }
        else
            return d;
    }

    private String getImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String img = Base64.getEncoder().encodeToString(bytes);
        return img;
    }

    public void showMessage(String title, String message){
        new AlertDialog.Builder(getContext()).setTitle(title).setMessage(message).setCancelable(true).show();
    }
    private void showMessageWithOk(){
        new AlertDialog.Builder(getContext())
                .setTitle("Verification")
                .setMessage("Are you sure about the car details that you provided?")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        submit();
                    }
                })
                .show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

    }

    private void addMarker(){
        MarkerOptions markerOptions = new MarkerOptions().position(mark).title(" Car ").snippet("I don't know");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark,15));
        mMap.addMarker(markerOptions);
    }
}