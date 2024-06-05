package com.john.graduate_project_mobile.Activities.Auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.john.graduate_project_mobile.R;


public class NewPasswordFragment extends Fragment {

    private EditText code, pass1, pass2;
    private Button button;
    private String username;

    public NewPasswordFragment() {
        // Required empty public constructor
    }

    public NewPasswordFragment(String username) {
        this.username = username;
    }

    public static NewPasswordFragment newInstance(String param1, String param2) {
        NewPasswordFragment fragment = new NewPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_password, container, false);
        code = view.findViewById(R.id.editTextText6);
        pass1 = view.findViewById(R.id.editTextText7);
        pass2 = view.findViewById(R.id.editTextText8);
        button = view.findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof AuthActivity){
                    ((AuthActivity) getActivity()).newPassword(code.getText().toString(), pass1.getText().toString(), pass2.getText().toString(), username);
                }
            }
        });
        return view;
    }
}