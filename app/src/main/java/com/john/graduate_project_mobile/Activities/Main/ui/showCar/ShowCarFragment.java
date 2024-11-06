package com.john.graduate_project_mobile.Activities.Main.ui.showCar;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.john.graduate_project_mobile.Activities.Main.ui.Rent.RentViewModel;
import com.john.graduate_project_mobile.Activities.Main.ui.home.HomeViewModel;
import com.john.graduate_project_mobile.R;
import com.john.graduate_project_mobile.Security.SecurityConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

public class ShowCarFragment extends Fragment implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener {

    private RequestQueue requestQueue;
    private HomeViewModel model;
    private RentViewModel rentViewModel;
    private ShowCarViewModel showCarViewModel;
    private String license;
    private String carModel;
    private String token;
    private LinearLayout layout, reviewsLayout;
    private MapView mapView;
    private GoogleMap map;
    private LatLng mark;
    private ImageView imageView;
    private TextView textName, textValue, textDescription, textDays, textStars, textAddress, textReview;
    private Button btn;
    private EditText editTextDays;
    private Boolean selectDate =false;
    private DatePickerDialog dpd;
    private final int MAX_SELECTABLE_DATE_IN_FUTURE = 365;

    public ShowCarFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
        model = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        rentViewModel = new ViewModelProvider(requireActivity()).get(RentViewModel.class);
        showCarViewModel = new ViewModelProvider(requireActivity()).get(ShowCarViewModel.class);
        if (getActivity() instanceof MainActivity){
            token = ((MainActivity) getActivity()).getToken();

        }
        if (model.getData().getValue() == null) {
            license = rentViewModel.getText().getValue();
            ((MainActivity) getActivity()).backArrow(R.id.rentCar, rentViewModel.getID().getValue());
            rentViewModel.setText(null);
        }
        else {
            license = model.getData().getValue();
            ((MainActivity) getActivity()).backArrow(R.id.rentCar, model.getID().getValue());
            model.setData(null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_show_car, container, false);
        layout = root.findViewById(R.id.CarLayout);
        imageView = layout.findViewById(R.id.imageView);
        textName = layout.findViewById(R.id.textViewM);
        textValue =  layout.findViewById(R.id.textViewValue);
        textDescription =  layout.findViewById(R.id.textViewDescription);
        textDays  =  layout.findViewById(R.id.textViewDays);
        textStars =  layout.findViewById(R.id.textViewStar);
        textAddress =  layout.findViewById(R.id.textViewAddress);
        textReview = layout.findViewById(R.id.textViewR);
        reviewsLayout =  layout.findViewById(R.id.reviews);
        btn = layout.findViewById(R.id.button18);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null)
            mapViewBundle = savedInstanceState.getBundle(SecurityConstants.MAPVIEW_BUNDLE_KEY);
        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle );
        mapView.getMapAsync(this);
        showCar();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.983810, 23.727539),14));
    }

    private void showCar(){
        if (token != null) {
            String url = "http://10.0.2.2:8080/android/showCar";
            JSONObject body = new JSONObject();
            try {
                body.put("license", license);
                body.put("token", token);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getInt("status")==0){
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.hide(ShowCarFragment.this);
                            fragmentTransaction.commit();
                            if (getActivity() instanceof MainActivity){
                                ((MainActivity) getActivity()).navigateTo(R.id.nav_info);
                                Toast.makeText(getActivity(), response.getString("msg"),Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            JSONObject car = response.getJSONObject("car");
                            String a = car.getString("address");
                            String[] latlng = a.split(",");
                            mark = new LatLng(Double.parseDouble(latlng[0]),Double.parseDouble(latlng[1]));
                            if (getActivity() instanceof MainActivity){
                                ((MainActivity) getActivity()).getImages(license, imageView);
                            }
                            new Thread(()->{
                                List<Address> addresses;
                                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                try {

                                    addresses = geocoder.getFromLocation(Double.parseDouble(latlng[0]),Double.parseDouble(latlng[1]),1);
                                    assert addresses != null;
                                    textAddress.setText(addresses.get(0).getAddressLine(0));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }).start();


                            textName.setText(car.getString("model"));
                            carModel =car.getString("model");

                            String v = car.getString("value")+" $/day";
                            textValue.setText(v);

                            String d = "Available days: " + car.getString("availableDays");
                            textDays.setText(d);

                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Description: ").append(car.getString("description")).append("\n").append(car.getString("seats"))
                                            .append(" Seats \n").append(car.getString("transmission")).append("\n").append(car.getString("fuel"));
                            textDescription.setText(stringBuilder);

                            stringBuilder.delete(0,stringBuilder.capacity()).append(car.getString("rating")).append(" (").append(response.getString("reviewsLen")).append(")");
                            textStars.setText(stringBuilder);

                            for (int i =0;i< response.getInt("reviewsLen");i++){
                                JSONArray reviews = response.getJSONArray("reviews");
                                JSONObject r = reviews.getJSONObject(i);
                                View view = LayoutInflater.from(getContext()).inflate(R.layout.review,null);

                                TextView name = view.findViewById(R.id.textViewName);
                                name.setText(r.getString("username"));

                                TextView rating = view.findViewById(R.id.textViewRating);
                                rating.setText(r.getString("stars"));

                                TextView desc = view.findViewById(R.id.textViewReview);
                                desc.setText(r.getString("review"));
                                reviewsLayout.addView(view);
                            }
                            if (response.getInt("reviewsLen") != 0)
                                textReview.setText("");
                            addMarker();
                            String[] temp = car.getString("availableDays").toUpperCase().split(", ");
                            List<Integer> days = new ArrayList<>();
                            for (String s: temp) {
                                if (s.equals("MONDAY"))
                                    days.add(2);
                                else if (s.equals("TUESDAY"))
                                    days.add(3);
                                else if (s.equals("WEDNESDAY"))
                                    days.add(4);
                                else if (s.equals("THURSDAY"))
                                    days.add(5);
                                else if (s.equals("FRIDAY"))
                                    days.add(6);
                                else if (s.equals("SATURDAY"))
                                    days.add(7);
                                else if (s.equals("SUNDAY"))
                                    days.add(1);
                                else if (s.equals("ALL"))
                                    days = Arrays.asList(1,2,3,4,5,6,7);
                            }

                            List<Integer> finalDays = days;
                            btn.setOnClickListener(l -> {
                                if (!selectDate) {
                                    LinearLayout layout1 = layout.findViewById(R.id.Dates);
                                    View view = LayoutInflater.from(getContext()).inflate(R.layout.select_date, null);
                                    editTextDays = view.findViewById(R.id.editTextText13);
                                    Button button = view.findViewById(R.id.button19);
                                    button.setOnClickListener(v1 -> {
                                        if (!selectDate) {
                                            button.setText(R.string.selectAnother);
                                            selectDate = true;
                                        }
                                        Calendar now = Calendar.getInstance();
                                        if (dpd == null) {
                                            dpd = DatePickerDialog.newInstance(
                                                    ShowCarFragment.this,
                                                    now.get(Calendar.YEAR),
                                                    now.get(Calendar.MONTH),
                                                    now.get(Calendar.DAY_OF_MONTH)
                                            );
                                        } else {
                                            dpd.initialize(
                                                    ShowCarFragment.this,
                                                    now.get(Calendar.YEAR),
                                                    now.get(Calendar.MONTH),
                                                    now.get(Calendar.DAY_OF_MONTH)
                                            );
                                        }
                                        ArrayList<Calendar> weekends = new ArrayList<Calendar>();
                                        Calendar day = Calendar.getInstance();
                                        for (int i = 0; i < MAX_SELECTABLE_DATE_IN_FUTURE; i++) {
                                            if (finalDays.contains(day.get(Calendar.DAY_OF_WEEK))) {
                                                Calendar da = (Calendar) day.clone();
                                                weekends.add(da);
                                            }
                                            day.add(Calendar.DATE, 1);
                                        }
                                        Calendar[] weekendDays = weekends.toArray(new Calendar[weekends.size()]);
                                        dpd.setSelectableDays(weekendDays);

                                        dpd.setOnCancelListener(dialog -> {
                                            Log.d("DatePickerDialog", "Dialog was cancelled");
                                            dpd = null;
                                        });
                                        dpd.show(requireFragmentManager(), "Datepickerdialog");

                                    });
                                    layout1.addView(view);
                                }
                                else {
                                    sendRequest();
                                }

                            });


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
        else {
            System.out.println("TOKEN expired");
            Toast.makeText(getContext(), "You have to login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);
        }
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


    private void addMarker(){
        MarkerOptions markerOptions = new MarkerOptions().position(mark).title(carModel);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mark,14));
        map.addMarker(markerOptions);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        StringBuilder stringBuilder = new StringBuilder();
        String month;
        String day;
        monthOfYear+=1;
        if (monthOfYear<10){
            month = "0" +monthOfYear;
        }
        else
            month = String.valueOf(monthOfYear);
        if (dayOfMonth<10){
            day = "0" +monthOfYear;
        }
        else
            day = String.valueOf(dayOfMonth);

        if (editTextDays.getText().toString().equals("")){
            stringBuilder.append(year).append("-").append(month).append("-").append(day);
        }
        else {
            stringBuilder.append(editTextDays.getText()).append(",").append(year).append("-").append(month).append("-").append(day);
        }
        editTextDays.setText(stringBuilder);

    }

    private void sendRequest(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://10.0.2.2:8080/android/showCar/Rent?days=").append(editTextDays.getText().toString()).append("&license=").append(license)
                .append("&token=").append(token);
        JSONObject body = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringBuilder.toString(), body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getContext(), response.getString("msg"), Toast.LENGTH_LONG).show();
                    if (response.getString("msg").contains("successfully")) {
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).navigateTo(R.id.nav_home);
                        }
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

}