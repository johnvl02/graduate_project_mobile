package com.john.graduate_project_mobile.Activities.Main.ui.Review;

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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewFragment extends Fragment {

    private ReviewViewModel mViewModel;
    private RadioGroup rating;
    private TextView title;
    private EditText review;
    private Button submit;
    private String token;
    private RequestQueue requestQueue;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
        if (getActivity() instanceof MainActivity){
            token = ((MainActivity) getActivity()).getToken();
            ((MainActivity) getActivity()).backArrow(R.id.nav_review, R.id.nav_requestsFor);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_review, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(ReviewViewModel.class);
        title = view.findViewById(R.id.textView57);
        review = view.findViewById(R.id.editTextTextMultiLine2);
        rating = view.findViewById(R.id.radioGroupRating);
        submit = view.findViewById(R.id.button17);
        if (Boolean.TRUE.equals(mViewModel.getReviewCar().getValue())){
            title.setText(R.string.reviewCar);
            submit.setOnClickListener(v -> {
                reviewCar();
            });

        }
        else {
            title.setText(R.string.reviewUser);
            submit.setOnClickListener(v -> {
                System.out.println("ok");
                reviewUser();
            });

        }
        return view;
    }

    private void reviewCar(){
        if (token != null) {
            String url = "http://10.0.2.2:8080/android/Review/Car";
            JSONObject body = new JSONObject();

            String r = checkReview(review.getText().toString());
            String stars = checkRating();

            if (r != null && stars != null) {
                try {
                    body.put("license", mViewModel.getLicense().getValue());
                    body.put("date", mViewModel.getDate().getValue());
                    body.put("renter",token);
                    body.put("review", r);
                    body.put("rating", stars);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            if (response.getString("msg").contains("wrong") || response.getString("msg").contains("didn't"))
                                Toast.makeText(getContext(),response.getString("msg"), Toast.LENGTH_SHORT).show();
                            else {
                                FragmentManager fragmentManager = getParentFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.remove(ReviewFragment.this);
                                fragmentTransaction.commit();
                                Toast.makeText(getContext(),response.getString("msg"), Toast.LENGTH_SHORT).show();
                                if (getActivity() instanceof MainActivity){
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
                        System.out.println(error.getLocalizedMessage());
                    }
                });
                requestQueue.add(request);
            }
        } else
            System.out.println("TOKEN expired");

    }

    private void reviewUser(){
        if (token != null) {
            String url = "http://10.0.2.2:8080/android/Review/User";
            JSONObject body = new JSONObject();

            String r = checkReview(review.getText().toString());
            String stars = checkRating();

            if (r != null && stars != null) {
                try {
                    body.put("license", mViewModel.getLicense().getValue());
                    body.put("date", mViewModel.getDate().getValue());
                    body.put("renter", mViewModel.getRenter().getValue());
                    body.put("owner", token);
                    body.put("review", r);
                    body.put("rating", stars);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            if (response.getString("msg").contains("wrong") || response.getString("msg").contains("didn't"))
                                Toast.makeText(getContext(),response.getString("msg"), Toast.LENGTH_SHORT).show();
                            else {
                                FragmentManager fragmentManager = getParentFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.remove(ReviewFragment.this);
                                fragmentTransaction.commit();
                                Toast.makeText(getContext(),response.getString("msg"), Toast.LENGTH_SHORT).show();
                                if (getActivity() instanceof MainActivity){
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
                        System.out.println(error.getLocalizedMessage());
                    }
                });
                requestQueue.add(request);
            }
        } else
            System.out.println("TOKEN expired");

    }

    private String checkReview(String r){
        if (r.length()>500){
            Toast.makeText(getContext(),"The review is over 500 characters!!",Toast.LENGTH_LONG).show();
            return null;
        }
        else if (r.length()==0){
            Toast.makeText(getContext(),"Please fill in the review!",Toast.LENGTH_LONG).show();
            return null;
        }
        else {
            return r;
        }
    }
    private String checkRating(){
        int id = rating.getCheckedRadioButtonId();
        if (id == -1){
            if (mViewModel.getReviewCar().getValue()) {
                Toast.makeText(getContext(), "Please select a rating for the experience of the rent this car", Toast.LENGTH_LONG).show();
                return null;
            }
            else {
                Toast.makeText(getContext(), "Please select a rating for the experience renting this car to this user", Toast.LENGTH_LONG).show();
                return null;
            }
        }
        else {
            RadioButton radioButton = view.findViewById(id);
            return radioButton.getText().toString();
        }
    }


}