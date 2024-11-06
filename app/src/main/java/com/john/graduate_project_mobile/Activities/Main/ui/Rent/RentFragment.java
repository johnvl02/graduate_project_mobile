package com.john.graduate_project_mobile.Activities.Main.ui.Rent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.R;
import com.john.graduate_project_mobile.databinding.FragmentRentBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class RentFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentRentBinding binding;
    private LinearLayout frameLayout;
    private View viewload;
    private RequestQueue requestQueue;
    private RentViewModel rentViewModel;
    private final int size = 20;
    private int currentPage;
    private String sort;
    private  int allPages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this.getContext());
        currentPage =0;
        sort = "none";
        allPages =1;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rentViewModel= new ViewModelProvider(requireActivity()).get(RentViewModel.class);
        binding = FragmentRentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        frameLayout = root.findViewById(R.id.layout2);
        viewload = LayoutInflater.from(getContext()).inflate(R.layout.loading, null);
        frameLayout.addView(viewload);
        Spinner spinner = root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sorting, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        frameLayout.removeAllViews();
    }

    public void rentCars(int page, int size, String sort){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://10.0.2.2:8080/android/rent?page=").append(page).append("&size=").append(size)
                .append("&sort=").append(sort);
        JSONObject body = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringBuilder.toString(), body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response);
                    int len = response.getJSONObject("cars").length();
                    for (int i=0;i<len;i++){
                        JSONObject temp = response.getJSONObject("cars").getJSONObject(String.valueOf(i));

                        View view = LayoutInflater.from(getContext()).inflate(R.layout.car_card, null);

                        ImageView view1 = view.findViewById(R.id.imageView2);
                        getImages(temp.getString("license"), view1);

                        TextView textView = view.findViewById(R.id.textViewModel);
                        textView.setText(temp.getString("model"));

                        TextView textView3 = view.findViewById(R.id.textViewRating);
                        textView3.setText(temp.getString("rating"));

                        TextView textView2 = view.findViewById(R.id.textViewPrice);
                        stringBuilder.delete(0,stringBuilder.length());
                        stringBuilder.append("$").append(temp.getString("value")).append("/day");
                        textView2.setText(stringBuilder);

                        Button btn = view.findViewById(R.id.RentButton);
                        btn.setTag(temp.getString("license"));
                        String l = temp.getString("license");
                        btn.setOnClickListener(v -> {
                            rentViewModel.setText(l);
                            rentViewModel.setID(R.id.nav_rent);
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.hide(RentFragment.this);
                            fragmentTransaction.commit();
                            ((MainActivity) getActivity()).navigateTo(R.id.rentCar);

                        });
                        frameLayout.addView(view);
                    }
                    currentPage = response.getInt("currentPage");
                    allPages = response.getInt("allPages");
                    if (currentPage+1<allPages){
                        View view2 = LayoutInflater.from(getContext()).inflate(R.layout.button, null);
                        Button button = view2.findViewById(R.id.button15);
                        button.setOnClickListener(v -> {
                            rentCars(currentPage+1,size,sort);
                            frameLayout.removeView(view2);
                        });
                        frameLayout.addView(view2);
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

    private void getImages(String name, ImageView imageView){
        String url = "http://10.0.2.2:8080/android/image?name="+name;

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(imageRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sort = (String) parent.getItemAtPosition(position);
        currentPage = 0;
        sort = sort.toLowerCase();
        rentCars(currentPage,size, sort);
        frameLayout.removeAllViews();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}