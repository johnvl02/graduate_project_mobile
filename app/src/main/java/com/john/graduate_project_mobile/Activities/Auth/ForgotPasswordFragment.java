package com.john.graduate_project_mobile.Activities.Auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.john.graduate_project_mobile.R;


public class ForgotPasswordFragment extends Fragment {
    private EditText username, email;
    private Button submit;


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    public static ForgotPasswordFragment newInstance(String param1, String param2) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        username = view.findViewById(R.id.editTextText2);
        email = view.findViewById(R.id.editTextText5);
        submit = view.findViewById(R.id.button6);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof AuthActivity){
                    ((AuthActivity) getActivity()).sendEmail(username.getText().toString(), email.getText().toString());
                }
            }
        });
        return view;
    }
}