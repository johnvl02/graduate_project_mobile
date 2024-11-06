package com.john.graduate_project_mobile.Activities.Main.UserInfo;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class InfoFragment extends Fragment {

    private EditText date, info;
    private Button btn;
    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        date = view.findViewById(R.id.editTextDate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener(){
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String m;
                                if (monthOfYear+1<10){
                                    m = "0"+ (monthOfYear+1);
                                }
                                else
                                    m = String.valueOf(monthOfYear+1);
                                String d;
                                if (dayOfMonth <10)
                                    d="0"+ (dayOfMonth);
                                else
                                    d=String.valueOf(dayOfMonth);
                                date.setText(year + "-" +m + "-" +  d );

                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });
        info = view.findViewById(R.id.editTextTextMultiLine);
        btn = view.findViewById(R.id.button8);
        btn.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity){
                ((MainActivity) getActivity()).submitInfo(date.getText().toString(), info.getText().toString());
            }
        });
        return view;
    }


}