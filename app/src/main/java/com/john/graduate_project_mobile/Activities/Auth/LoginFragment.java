
package com.john.graduate_project_mobile.Activities.Auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.john.graduate_project_mobile.R;


public class LoginFragment extends Fragment {
    private Button login, register, passwordB;
    private EditText username, password;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        login = view.findViewById(R.id.button);
        register = view.findViewById(R.id.button2);
        passwordB = view.findViewById(R.id.button3);
        username = view.findViewById(R.id.editTextUsername);
        password = view.findViewById(R.id.editTextPassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof AuthActivity){
                    ((AuthActivity) getActivity()).login(username.getText().toString(), password.getText().toString());
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof AuthActivity){
                    ((AuthActivity) getActivity()).changeFragment(new RegisterFragment());
                }
            }
        });

        passwordB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof AuthActivity){
                    ((AuthActivity) getActivity()).changeFragment(new ForgotPasswordFragment());
                }
            }
        });

        return view;
    }





}