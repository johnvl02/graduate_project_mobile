package com.john.graduate_project_mobile.Activities.Main.ui.AddCar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import java.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.john.graduate_project_mobile.databinding.FragmentAddcarBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddCarFragment extends Fragment implements OnMapReadyCallback {

    private RequestQueue requestQueue;
    private LatLng mark;
    private EditText license, model, seats, value, description;
    private RadioGroup fuel, transmission;
    private CheckBox cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday, cbSaturday, cbSunday, cbAll;
    private List<String> days = new ArrayList<>();
    private ImageView imageView;
    private Boolean haveImage = false;
    private SearchView searchView;
    private GoogleMap mMap;
    private MapView mapView;
    private Button submit;
    private String token;
    private View rootView;
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
    private FragmentAddcarBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
        if (getActivity() instanceof MainActivity){
            token = ((MainActivity) getActivity()).getToken();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddcarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        rootView = root;

        license = root.findViewById(R.id.editTextTextLicense);
        model = root.findViewById(R.id.editTextTexModel);
        seats = root.findViewById(R.id.editTextTextSeats);
        value = root.findViewById(R.id.editTextTextValue);
        description = root.findViewById(R.id.editTextTextMultiLineDescription);

        fuel = root.findViewById(R.id.radioGroupFuel);
        transmission = root.findViewById(R.id.radioGroupTransmission);

        cbMonday = root.findViewById(R.id.checkBoxMonday);
        cbMonday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbTuesday = root.findViewById(R.id.checkBoxTuesday);
        cbTuesday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbWednesday = root.findViewById(R.id.checkBoxWednesday);
        cbWednesday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbThursday = root.findViewById(R.id.checkBoxThursday);
        cbThursday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbFriday = root.findViewById(R.id.checkBoxFriday);
        cbFriday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbSaturday = root.findViewById(R.id.checkBoxSaturday);
        cbSaturday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbSunday = root.findViewById(R.id.checkBoxSunday);
        cbSunday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                days.add(buttonView.getText()+", ");
            }
            else {
                days.remove(buttonView.getText()+", ");
            }
        });
        cbAll = root.findViewById(R.id.checkBoxAll);
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
                days.remove(buttonView.getText()+", ");
            }
        });

        searchView = root.findViewById(R.id.searchView);
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
                            System.out.println(addressList.size());
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
        mapView = root.findViewById(R.id.mapView2);
        mapView.onCreate(mapViewBundle );
        mapView.getMapAsync(this);

        imageView = root.findViewById(R.id.imageViewPhoto);
        imageView.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
            haveImage = true;
        });

        submit = root.findViewById(R.id.button9);
        submit.setOnClickListener(v -> {
            showMessageWithOk();
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void Submit(){
        if (token != null) {
            String url = "http://10.0.2.2:8080/android/addCar";
            JSONObject body = new JSONObject();

            String l = getLicense();
            String m = getModel();
            String s = getSeats();
            String v = getValue();
            String de = getDescription();
            String f = getFuel();
            String t = getTransmission();
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
            if (l != null && m != null && s != null && v != null && de != null && f != null && t != null && d != null) {
                try {

                    body.put("token", token);
                    body.put("license", l);
                    body.put("model", m);
                    body.put("seats", s);
                    body.put("value", v);
                    body.put("description", de);
                    body.put("fuel", f);
                    body.put("transmission", t);
                    body.put("days", d);
                    body.put("address", address);
                    body.put("image", img);

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
                                fragmentTransaction.remove(AddCarFragment.this);
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


    private String getFuel(){
        int id = fuel.getCheckedRadioButtonId();
        if (id == -1){
            Toast.makeText(getContext(),"Please select a fuel type for the car", Toast.LENGTH_LONG).show();
            return null;
        }
        else {
            RadioButton radioButton = rootView.findViewById(id);
            System.out.println(radioButton.getText());
            return radioButton.getText().toString();
        }
    }

    private String getTransmission(){
        int id = transmission.getCheckedRadioButtonId();
        if (id == -1){
            Toast.makeText(getContext(),"Please select a transmission type for the car", Toast.LENGTH_LONG).show();
            return null;
        }
        else {
            RadioButton radioButton = rootView.findViewById(id);
            System.out.println(radioButton.getText());
            return radioButton.getText().toString();
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

    private String getLicense(){
        String l = license.getText().toString();
        if (Objects.equals(l, "")){
            Toast.makeText(getContext(), "Please fill in the license plate of the car",Toast.LENGTH_LONG).show();
            return null;
        }
        else if (l.length() >10){
            Toast.makeText(getContext(), "Please fill in the license plate correctly (max 10 characters)",Toast.LENGTH_LONG).show();
            return null;
        }
        else
            return l;
    }

    private String getModel(){
        String m = model.getText().toString();
        if (Objects.equals(m, "")){
            Toast.makeText(getContext(), "Please fill in the model of the car",Toast.LENGTH_LONG).show();
            return null;
        }
        else
            return m;
    }

    private String getSeats(){
        String s = seats.getText().toString();
        if (Objects.equals(s, "")){
            Toast.makeText(getContext(), "Please fill in the seats that the car have",Toast.LENGTH_LONG).show();
            return null;
        }
        else
            return s;
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
                       Submit();
                    }
                })
                .show();
    }
}