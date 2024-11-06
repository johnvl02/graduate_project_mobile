package com.john.graduate_project_mobile.Activities.Main.ui.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.john.graduate_project_mobile.Activities.Main.ui.showCar.ShowCarFragment;
import com.john.graduate_project_mobile.R;
import com.john.graduate_project_mobile.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {

    private RequestQueue requestQueue;
    private FragmentHomeBinding binding;
    private LinearLayout frameLayout;
    private View viewload;
    private HomeViewModel model;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        frameLayout = root.findViewById(R.id.layout2);
        viewload = LayoutInflater.from(getContext()).inflate(R.layout.loading, null);
        frameLayout.addView(viewload);
        getCars();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
        model = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        frameLayout.removeAllViews();
    }

    private void getCars(){
        String url = "http://10.0.2.2:8080/android/homepage";
        JSONObject body = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, body, response -> {
            try {
                frameLayout.removeView(viewload);
                for (int i=0; i<response.length();i++) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.car_card, null);

                    ImageView view1 = view.findViewById(R.id.imageView2);
                    if (getActivity() instanceof MainActivity){
                        ((MainActivity) getActivity()).getImages(response.getJSONObject(String.valueOf(i)).getString("license"), view1);
                    }

                    TextView textView = view.findViewById(R.id.textViewModel);
                    textView.setText(response.getJSONObject(String.valueOf(i)).getString("model"));

                    TextView textView3 = view.findViewById(R.id.textViewRating);
                    textView3.setText(response.getJSONObject(String.valueOf(i)).getString("rating"));

                    TextView textView2 = view.findViewById(R.id.textViewPrice);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("$").append(response.getJSONObject(String.valueOf(i)).getString("value")).append("/day");
                    textView2.setText(stringBuilder);

                    Button btn = view.findViewById(R.id.RentButton);
                    btn.setTag(response.getJSONObject(String.valueOf(i)).getString("license"));
                    String l = response.getJSONObject(String.valueOf(i)).getString("license");
                    btn.setOnClickListener(v -> {
                        model.setData(l);
                        model.setID(R.id.nav_home);
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.remove(this);
                        fragmentTransaction.commit();
                        if (getActivity() instanceof MainActivity){
                            ((MainActivity) getActivity()).navigateTo(R.id.rentCar);
                        }

                    });
                    frameLayout.addView(view);
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getLocalizedMessage());
                Toast.makeText(getContext(),"Something went wrong, check your connection and try again", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);
    }

}