package com.john.graduate_project_mobile.Activities.Auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.john.graduate_project_mobile.R;

public class VerificationCodeFragment extends Fragment {
    private EditText vcode;
    private Button submit;
    private String username;

    public VerificationCodeFragment() {
        // Required empty public constructor
    }

    public VerificationCodeFragment(String username) {
        this.username = username;
    }

    public static VerificationCodeFragment newInstance(String param1, String param2) {
        VerificationCodeFragment fragment = new VerificationCodeFragment();
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
        View view = inflater.inflate(R.layout.fragment_verification_code, container, false);
        vcode = view.findViewById(R.id.editTextVCode);
        submit = view.findViewById(R.id.button5);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof AuthActivity){
                    ((AuthActivity) getActivity()).verificationCode(vcode.getText().toString(), username);
                }
            }
        });
        return view;
    }
}