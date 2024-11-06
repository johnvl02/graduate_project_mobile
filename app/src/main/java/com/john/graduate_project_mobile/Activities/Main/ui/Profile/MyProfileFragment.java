package com.john.graduate_project_mobile.Activities.Main.ui.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.john.graduate_project_mobile.Activities.Auth.AuthActivity;
import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.Activities.Main.ui.EditCar.EditCarViewModel;
import com.john.graduate_project_mobile.Activities.Main.ui.Rent.RentFragment;
import com.john.graduate_project_mobile.Activities.Main.ui.Rent.RentViewModel;
import com.john.graduate_project_mobile.Activities.Main.ui.showCar.ShowCarFragment;
import com.john.graduate_project_mobile.R;

import org.json.JSONException;
import org.json.JSONObject;


public class MyProfileFragment extends Fragment {
    private TextView name, email, phone, age;
    private Button btn;
    private MyProfileViewModel model;
    private LinearLayout reviews, carsFrame;
    private RequestQueue requestQueue;
    private String token;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this.getContext());
        if (getActivity() instanceof MainActivity){
            token = ((MainActivity) getActivity()).getToken();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        model = new ViewModelProvider(requireActivity()).get(MyProfileViewModel.class);
        name = view.findViewById(R.id.textViewname);
        email = view.findViewById(R.id.textViewEmail);
        phone = view.findViewById(R.id.textViewphone);
        age = view.findViewById(R.id.textViewage);
        reviews = view.findViewById(R.id.reviewUserFrame);
        carsFrame = view.findViewById(R.id.carFrame);
        btn = view.findViewById(R.id.editButton);

        viewProfile();
        return view;
    }

    private void viewProfile(){
        if (token != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://10.0.2.2:8080/android/MyProfile?token=").append(token);
            JSONObject body = new JSONObject();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringBuilder.toString(), body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject user = response.getJSONObject("user");
                        stringBuilder.delete(0, stringBuilder.length());
                        stringBuilder.append(user.getString("firstName")).append(" ").append(user.getString("lastName"));
                        name.setText(stringBuilder);
                        email.setText(user.getString("mail"));
                        phone.setText(user.getString("phone"));
                        age.setText(user.getString("age"));

                        btn.setOnClickListener(v -> {
                            try {
                                model.setName(name.getText().toString());
                                model.setEmail(user.getString("mail"));
                                model.setPhone(user.getString("phone"));
                                model.setAge(user.getString("age"));
                                if (getActivity() instanceof MainActivity) {
                                    FragmentManager fragmentManager = getParentFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.hide(MyProfileFragment.this);
                                    fragmentTransaction.commit();
                                    ((MainActivity) getActivity()).navigateTo(R.id.nav_editProfile);
                                    //((MainActivity) getActivity()).changeFragment(new EditProfileFragment());
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        });

                        JSONObject reviewsUser = response.getJSONObject("reviews");
                        int i = reviewsUser.length();
                        for (int k = 0; k < i; k++) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.review, null);
                            JSONObject temp = reviewsUser.getJSONObject(String.valueOf(k));
                            TextView textView = view.findViewById(R.id.textViewName);
                            textView.setText(temp.getJSONObject("reviewForUserID").getString("owner_username"));
                            TextView textView2 = view.findViewById(R.id.textViewRating);
                            textView2.setText(temp.getString("stars"));
                            TextView textView3 = view.findViewById(R.id.textViewReview);
                            textView3.setText(temp.getString("review"));
                            reviews.addView(view);
                        }

                        JSONObject cars = response.getJSONObject("cars");
                        i = cars.length();
                        for (int k = 0; k < i; k++) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.car_card, null);

                            JSONObject temp = cars.getJSONObject(String.valueOf(k));

                            JSONObject car = temp.getJSONObject("car");
                            ImageView view1 = view.findViewById(R.id.imageView2);
                            if (getActivity() instanceof MainActivity) {
                                ((MainActivity) getActivity()).getImages(car.getString("license"), view1);
                            }

                            TextView textView = view.findViewById(R.id.textViewModel);
                            textView.setText(car.getString("model"));
                            TextView textView2 = view.findViewById(R.id.textViewRating);
                            textView2.setText(car.getString("rating"));
                            TextView textView3 = view.findViewById(R.id.textViewPrice);
                            stringBuilder.delete(0, stringBuilder.length());
                            stringBuilder.append("$").append(car.getString("value")).append("/day");
                            textView3.setText(stringBuilder);
                            Button button = view.findViewById(R.id.RentButton);
                            button.setVisibility(View.INVISIBLE);
                            carsFrame.addView(view);

                            View view2 = LayoutInflater.from(getContext()).inflate(R.layout.edit_delete_buttons, null);
                            Button editBtn = view2.findViewById(R.id.editCarBtn);
                            editBtn.setOnClickListener(v -> {
                                try {
                                    editCar(car.getString("license"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            Button delete = view2.findViewById(R.id.deleteCarBtn);
                            delete.setOnClickListener(v -> {
                                try {
                                    showMessageWithOk(car.getString("license"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            carsFrame.addView(view2);

                            TextView textView1 = new TextView(getContext());
                            textView1.setText(R.string.review);
                            textView1.setTextSize(18);
                            carsFrame.addView(textView1);

                            JSONObject review = temp.getJSONObject("review");
                            int j = review.length();
                            for (int x = 0; x < j; x++) {
                                View view3 = LayoutInflater.from(getContext()).inflate(R.layout.review, null);
                                JSONObject temp2 = review.getJSONObject(String.valueOf(x));
                                TextView textView4 = view3.findViewById(R.id.textViewName);
                                textView4.setText(temp2.getJSONObject("reviewForCarID").getString("renter_username"));
                                TextView textView5 = view3.findViewById(R.id.textViewRating);
                                textView5.setText(temp2.getString("stars"));
                                TextView textView6 = view3.findViewById(R.id.textViewReview);
                                textView6.setText(temp2.getString("review"));
                                carsFrame.addView(view3);
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
        else {
            System.out.println("TOKEN expired");
            Toast.makeText(getContext(), "You have to login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);
        }
    }

    private void deleteCar(String license){
        if (token != null){
            String url = "http://10.0.2.2:8080/android/deleteCar?license="+license;
            JSONObject body = new JSONObject();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Toast.makeText(getContext(),response.getString("msg"),Toast.LENGTH_LONG).show();
                        if (getActivity() instanceof MainActivity) {
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.hide(MyProfileFragment.this);
                            fragmentTransaction.commit();
                            ((MainActivity) getActivity()).navigateTo(R.id.nav_profile);
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
        else {
            System.out.println("TOKEN expired");
            Toast.makeText(getContext(), "You have to login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            startActivity(intent);
        }
    }

    private void editCar(String license){
        EditCarViewModel editCarViewModel = new ViewModelProvider(requireActivity()).get(EditCarViewModel.class);
        editCarViewModel.setData(license);
        if (getActivity() instanceof MainActivity) {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(MyProfileFragment.this);
            fragmentTransaction.commit();
            ((MainActivity) getActivity()).navigateTo(R.id.nav_editCar);
        }
    }

    private void showMessageWithOk(String license){
        new AlertDialog.Builder(getContext())
                .setTitle("Verification")
                .setMessage("Are you sure you want to delete this car ??\n"+license)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCar(license);
                    }
                })
                .show();
    }
}