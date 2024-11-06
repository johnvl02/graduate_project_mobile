package com.john.graduate_project_mobile.Activities.Main.ui.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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


public class ProfileFragment extends Fragment {
    private TextView name, email, age, phone, date, info, rating;
    private LinearLayout reviews;
    private ProfileViewModel model;
    private RequestQueue requestQueue;
    private String token;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this.getContext());
        if (getActivity() instanceof MainActivity){
            token = ((MainActivity) getActivity()).getToken();
            ((MainActivity) getActivity()).backArrow(R.id.nav_renterProfile, R.id.nav_requestsMy);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        model = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        name = root.findViewById(R.id.textView47);
        email = root.findViewById(R.id.textView48);
        age = root.findViewById(R.id.textView51);
        phone = root.findViewById(R.id.textView52);
        date = root.findViewById(R.id.textView53);
        info = root.findViewById(R.id.textView54);
        rating = root.findViewById(R.id.textView55);
        reviews = root.findViewById(R.id.ReviewLayout);
        getProfile();
        return root;
    }

    private void getProfile(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://10.0.2.2:8080/android/profile?username=").append(model.getUsername().getValue());
        JSONObject body = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringBuilder.toString(), body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject renter = response.getJSONObject("renter");
                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append(renter.getString("firstName")).append(" ").append(renter.getString("lastName"));
                    name.setText(stringBuilder);
                    email.setText(renter.getString("mail"));
                    age.setText(renter.getString("age"));
                    phone.setText(renter.getString("phone"));
                    JSONObject renterInfo = response.getJSONObject("info");
                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append("Date of diploma : ").append(renterInfo.getString("date"));
                    date.setText(stringBuilder);
                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append("More info : ").append(renterInfo.getString("info"));
                    info.setText(stringBuilder);
                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append(response.getString("rating")).append("(").append(response.getString("people")).append(") ");
                    rating.setText(stringBuilder);
                    JSONObject review = response.getJSONObject("reviews");
                    for (int i =0; i<review.length();i++) {
                        JSONObject temp = review.getJSONObject(String.valueOf(i));
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.review, null);
                        TextView textView = view.findViewById(R.id.textViewName);
                        textView.setText(temp.getString("owner"));
                        TextView textView2 = view.findViewById(R.id.textViewRating);
                        textView2.setText(temp.getString("stars"));
                        TextView textView3 = view.findViewById(R.id.textViewReview);
                        textView3.setText(temp.getString("review"));
                        reviews.addView(view);
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