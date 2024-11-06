package com.john.graduate_project_mobile.Activities.Main.ui.Requests.RequestRentYourCar;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.Activities.Main.ui.Profile.ProfileViewModel;
import com.john.graduate_project_mobile.Activities.Main.ui.Review.ReviewViewModel;
import com.john.graduate_project_mobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestRentMyCarFragment extends Fragment {

    private RequestRentMyCarViewModel mViewModel;
    private ProfileViewModel model;
    private RequestQueue requestQueue;
    private JSONObject object;
    private String token;
    private ReviewViewModel carViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel =  new ViewModelProvider(requireActivity()).get(RequestRentMyCarViewModel.class);
        object = mViewModel.getData().getValue();
        requestQueue = Volley.newRequestQueue(this.getContext());
        if (getActivity() instanceof MainActivity){
            token = ((MainActivity) getActivity()).getToken();
            ((MainActivity) getActivity()).backArrow(R.id.nav_requestsMyCar, R.id.nav_requestsMy);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_request_rent_my_car, container, false);
        carViewModel = new ViewModelProvider(requireActivity()).get(ReviewViewModel.class);
        model = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        try {
            TextView textView = root.findViewById(R.id.textView32);
            textView.setText(object.getString("license"));

            TextView textView2 = root.findViewById(R.id.textView34);
            String username = object.getString("renter");
            textView2.setText(username);
            textView2.setOnClickListener(v -> {
                model.setUsername(username);
                if (getActivity() instanceof MainActivity){
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.hide(RequestRentMyCarFragment.this);
                    fragmentTransaction.commit();
                    ((MainActivity) getActivity()).navigateTo(R.id.nav_renterProfile);
                }

            });

            TextView textView3 = root.findViewById(R.id.textView36);
            textView3.setText(object.getString("request_date"));

            TextView textView4 = root.findViewById(R.id.textView38);
            textView4.setText(object.getString("update_date"));

            TextView textView5 = root.findViewById(R.id.textView40);
            textView5.setText(object.getString("dates"));

            Button a = root.findViewById(R.id.button13);
            a.setOnClickListener(v -> updateRequest("accept"));
            Button d = root.findViewById(R.id.button14);
            d.setOnClickListener(v -> updateRequest("decline"));

            TextView textView6 = root.findViewById(R.id.textView42);
            switch (object.getInt("reviewCar")){
                case (1):
                    textView6.setVisibility(View.INVISIBLE);
                    Button btn = root.findViewById(R.id.button12);
                    btn.setVisibility(View.VISIBLE);
                    btn.setOnClickListener(v -> {
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.addToBackStack("forcar");
                        fragmentTransaction.hide(RequestRentMyCarFragment.this);
                        fragmentTransaction.commit();

                        try {
                            carViewModel.setLicense(object.getString("license"));
                            carViewModel.setRenter(object.getString("renter"));
                            carViewModel.setDate(object.getString("request_date"));
                            carViewModel.setReviewCar(false);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        ((MainActivity) getActivity()).navigateTo(R.id.nav_review);
                    });
                    break;
                case (0):
                    a.setVisibility(View.VISIBLE);
                    d.setVisibility(View.VISIBLE);
                    textView6.setText("You can't review this car yet");
                    break;
                case (2):
                    textView6.setText("You have reviewed this car");
                    break;
                case (3):
                    textView6.setText("You can't review this car");
                    break;
                case (4):
                    a.setVisibility(View.VISIBLE);
                    d.setVisibility(View.VISIBLE);
                    textView6.setText("You must wait");
                    break;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        return root;
    }
    public void updateRequest(String value) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append("http://10.0.2.2:8080/android/updateStatus?token=").append(token).append("&license=").append(object.getString("license"))
                    .append("&date=").append(object.getString("request_date")).append("&status=").append(value).append("&renter=").append(object.getString("renter"));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringBuilder.toString(), new JSONObject(), response -> {
                try {
                    if (response.getString("msg").equals("The request updated successfully")){
                        Toast.makeText(getContext(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                        if (getActivity() instanceof MainActivity){
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.remove(RequestRentMyCarFragment.this);
                            fragmentTransaction.commit();
                            ((MainActivity) getActivity()).navigateTo(R.id.nav_home);
                        }
                    }
                    else
                        Toast.makeText(getContext(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(request);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

}