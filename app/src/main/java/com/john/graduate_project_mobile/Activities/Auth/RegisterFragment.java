package com.john.graduate_project_mobile.Activities.Auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.john.graduate_project_mobile.R;

public class RegisterFragment extends Fragment {

    private EditText username, password, email, firstName, lastName, phone, age;
    private Button submit;


    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        username = view.findViewById(R.id.editTextText);
        password = view.findViewById(R.id.editTextTextPassword);
        email = view.findViewById(R.id.editTextTextEmailAddress);
        firstName = view.findViewById(R.id.editTextText3);
        lastName = view.findViewById(R.id.editTextText4);
        phone = view.findViewById(R.id.editTextPhone);
        age = view.findViewById(R.id.editTextNumber);
        submit = view.findViewById(R.id.button4);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof AuthActivity){
                    ((AuthActivity) getActivity()).register(username.getText().toString(), password.getText().toString(),
                            firstName.getText().toString(),lastName.getText().toString(), email.getText().toString(), phone.getText().toString(), age.getText().toString());
                }
            }
        });

        return view;
    }
}