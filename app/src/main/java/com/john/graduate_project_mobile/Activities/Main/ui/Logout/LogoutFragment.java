package com.john.graduate_project_mobile.Activities.Main.ui.Logout;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.john.graduate_project_mobile.Activities.Auth.AuthActivity;
import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.R;

public class LogoutFragment extends Fragment { ;

    public LogoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).logout();
        }
        Intent intent = new Intent(this.getContext(), AuthActivity.class);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }
}