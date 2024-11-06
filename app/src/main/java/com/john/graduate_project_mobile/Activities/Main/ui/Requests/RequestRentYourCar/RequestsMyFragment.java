package com.john.graduate_project_mobile.Activities.Main.ui.Requests.RequestRentYourCar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.Activities.Main.ui.Requests.RequestRentACar.RequestForViewModel;
import com.john.graduate_project_mobile.Activities.Main.ui.Requests.RequestRentACar.RequestRentACarFragment;
import com.john.graduate_project_mobile.R;

import org.json.JSONException;
import org.json.JSONObject;


public class RequestsMyFragment extends Fragment {

    private RequestQueue requestQueue;
    private LinearLayout layout;
    private String token;
    private RequestRentMyCarViewModel model;

    public RequestsMyFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this.getContext());
        if (getActivity() instanceof MainActivity){
            token = ((MainActivity) getActivity()).getToken();
        }
        model =  new ViewModelProvider(requireActivity()).get(RequestRentMyCarViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_requests_my, container, false);
        layout = root.findViewById(R.id.layout3);
        getRequest();
        return root;
    }

    private void getRequest(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://10.0.2.2:8080/android/requestMyCar?token=").append(token);
        JSONObject body = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringBuilder.toString(), body, response -> {
            try {
                int len = response.length();
                for (int i=0;i<len;i++){
                    JSONObject temp = response.getJSONObject(String.valueOf(i));

                    View view = LayoutInflater.from(getContext()).inflate(R.layout.request_card, null);

                    TextView view1 = view.findViewById(R.id.textView16);
                    view1.setText(temp.getString("license"));

                    TextView textView = view.findViewById(R.id.textView19);
                    textView.setText(temp.getString("dates"));

                    TextView textView2 = view.findViewById(R.id.textView21);
                    String s1 = temp.getString("status");
                    if (temp.getInt("reviewCar") == 1){
                        s1+=",\nYou can review this car";
                    }
                    textView2.setText(s1);

                    Button btn = view.findViewById(R.id.button11);
                    btn.setOnClickListener(v -> {
                        model.setData(temp);
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.hide(this);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity()).navigateTo(R.id.nav_requestsMyCar);

                    });
                    layout.addView(view);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }
}