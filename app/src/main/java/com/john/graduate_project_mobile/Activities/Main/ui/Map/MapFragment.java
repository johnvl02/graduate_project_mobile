package com.john.graduate_project_mobile.Activities.Main.ui.Map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.Activities.Main.ui.Rent.RentViewModel;
import com.john.graduate_project_mobile.R;
import com.john.graduate_project_mobile.Security.SecurityConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private RequestQueue requestQueue;
    private GoogleMap mMap;
    private MapView mapView;
    private JSONObject object;
    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null)
            mapViewBundle = savedInstanceState.getBundle(SecurityConstants.MAPVIEW_BUNDLE_KEY);
        mapView = view.findViewById(R.id.mapView3);
        mapView.onCreate(mapViewBundle );
        mapView.getMapAsync(this);

        String url = "http://10.0.2.2:8080/android/maps";
        JSONObject body = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, body, response -> {
            object = response;
            for (int i = 0 ; i<object.length();i++){
                try {
                    JSONObject temp = object.getJSONObject(String.valueOf(i));
                    String[] address = temp.getString("address").split(",");
                    LatLng mark = new LatLng(Double.parseDouble(address[0]), Double.parseDouble(address[1]));
                    MarkerOptions markerOptions = new MarkerOptions().position(mark).title(temp.getString("model")).snippet("click again to show more info");
                    mMap.addMarker(markerOptions).setTag(temp.getString("license"));
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(@NonNull Marker marker) {
                            RentViewModel rentViewModel = new ViewModelProvider(requireActivity()).get(RentViewModel.class);
                            rentViewModel.setText(marker.getTag().toString());
                            rentViewModel.setID(R.id.nav_maps);
                            if (getActivity() instanceof MainActivity){
                                ((MainActivity) getActivity()).navigateTo(R.id.rentCar);
                            }
                        }
                    });
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
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.983810, 23.727539),9));

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
}