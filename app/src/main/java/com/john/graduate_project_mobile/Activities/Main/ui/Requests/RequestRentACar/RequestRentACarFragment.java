package com.john.graduate_project_mobile.Activities.Main.ui.Requests.RequestRentACar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.Activities.Main.ui.Review.ReviewViewModel;
import com.john.graduate_project_mobile.R;

import org.json.JSONException;
import org.json.JSONObject;


public class RequestRentACarFragment extends Fragment {


    private RequestForViewModel model;
    private JSONObject object;
    private String username;
    private ReviewViewModel carViewModel;
    public RequestRentACarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model =  new ViewModelProvider(requireActivity()).get(RequestForViewModel.class);
        object = model.getData().getValue();
        if (getActivity() instanceof MainActivity){
            username = ((MainActivity) getActivity()).getUsername();
            ((MainActivity) getActivity()).backArrow(R.id.nav_requestsForCar, R.id.nav_requestsFor);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_request_rent_a_car, container, false);
        carViewModel = new ViewModelProvider(requireActivity()).get(ReviewViewModel.class);
        try {
            TextView textView = root.findViewById(R.id.textView14);
            textView.setText(object.getString("license"));

            TextView textView2 = root.findViewById(R.id.textView23);
            textView2.setText(object.getString("owner"));

            TextView textView3 = root.findViewById(R.id.textView25);
            textView3.setText(object.getString("request_date"));

            TextView textView4 = root.findViewById(R.id.textView27);
            textView4.setText(object.getString("update_date"));

            TextView textView5 = root.findViewById(R.id.textView29);
            textView5.setText(object.getString("dates"));

            System.out.println(object);
            TextView textView6 = root.findViewById(R.id.textView31);
            switch (object.getInt("reviewCar")){
                case (1):
                    textView6.setVisibility(View.INVISIBLE);
                    Button btn = root.findViewById(R.id.button10);
                    btn.setVisibility(View.VISIBLE);
                    btn.setOnClickListener(v -> {
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.hide(RequestRentACarFragment.this);
                        fragmentTransaction.commit();

                        try {
                            carViewModel.setLicense(object.getString("license"));
                            carViewModel.setRenter(username);
                            carViewModel.setDate(object.getString("request_date"));
                            carViewModel.setReviewCar(true);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        ((MainActivity) getActivity()).navigateTo(R.id.nav_review);
                    });
                    break;
                case (0):
                    textView6.setText("You can't review this car yet");
                    break;
                case (2):
                    textView6.setText("You have reviewed this car");
                    break;
                case (3):
                    textView6.setText("You can't review this car");
                    break;
                case (4):
                    textView6.setText("You must wait");
                    break;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        return root;
    }
}