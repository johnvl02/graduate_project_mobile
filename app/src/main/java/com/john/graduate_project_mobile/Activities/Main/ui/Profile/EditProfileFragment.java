package com.john.graduate_project_mobile.Activities.Main.ui.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.john.graduate_project_mobile.Activities.Auth.AuthActivity;
import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.R;

import org.json.JSONException;
import org.json.JSONObject;


public class EditProfileFragment extends Fragment {
    private EditText name, email, phone, age;
    private Button btn;
    private MyProfileViewModel model;
    private RequestQueue requestQueue;
    private String token;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this.getContext());
        if (getActivity() instanceof MainActivity){
            token = ((MainActivity) getActivity()).getToken();
            ((MainActivity) getActivity()).backArrow(R.id.nav_editProfile, R.id.nav_profile);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        model = new ViewModelProvider(requireActivity()).get(MyProfileViewModel.class);
        name = view.findViewById(R.id.editTextText9);
        name.setText(model.getName().getValue());
        email = view.findViewById(R.id.editTextText10);
        email.setText(model.getEmail().getValue());
        phone = view.findViewById(R.id.editTextText11);
        phone.setText(model.getPhone().getValue());
        age = view.findViewById(R.id.editTextText12);
        age.setText(model.getAge().getValue());
        btn = view.findViewById(R.id.button16);
        btn.setOnClickListener(v -> submitProfileChanges());
        return view;
    }

    public void submitProfileChanges(){
        if (checkValues()) {
            if (token != null) {
                String url = "http://10.0.2.2:8080/android/editProfile";
                JSONObject body = new JSONObject();
                String s = name.getText().toString();
                String[] temp = s.split(" ");
                try {
                    body.put("token", token);
                    body.put("first name", temp[0]);
                    body.put("last name", temp[1]);
                    body.put("email", email.getText().toString());
                    body.put("phone", phone.getText().toString());
                    body.put("age", age.getText().toString());
                    System.out.println(body);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("msg").contains("successfully")) ;
                            Toast.makeText(getContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                            //return to the home page and clear all fields
                            if (getActivity() instanceof MainActivity) {
                                FragmentManager fragmentManager = getParentFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.remove(EditProfileFragment.this);
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
            else {
                System.out.println("TOKEN expired");
                Toast.makeText(getContext(), "You have to login first", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                startActivity(intent);
            }
        }
    }

    private boolean checkValues(){
        String[] n = name.getText().toString().split(" ");
        String e = email.getText().toString();
        long ph;
        try {
            ph = Long.parseLong(phone.getText().toString());
        }
        catch (NumberFormatException exception){
            Toast.makeText(getContext(), "Please enter only numbers for the phone",Toast.LENGTH_SHORT).show();
            return false;
        }
        int a;
        try {
            a = Integer.parseInt(age.getText().toString());
        }
        catch (NumberFormatException exception){
            Toast.makeText(getContext(), "Please enter only numbers for the age",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (n[0].length()<2 || n[1].length()<2){
            Toast.makeText(getContext(), "Please fill correctly your full name",Toast.LENGTH_SHORT).show();
            return false;
        } else if (!e.contains("@") || !e.contains(".")) {
            Toast.makeText(getContext(), "Please enter a valid email",Toast.LENGTH_SHORT).show();
            return false;
        } else if ((ph < 1000000000L) || (ph > 9999999999L)) {
            Toast.makeText(getContext(), "Please enter a valid phone, something like 6912345678 or 2101234567",Toast.LENGTH_SHORT).show();
            return false;
        } else if (a <= 18 || a > 99) {
            Toast.makeText(getContext(), "Your age must be between 18 and 99",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}