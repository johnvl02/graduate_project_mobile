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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.john.graduate_project_mobile.Activities.Main.MainActivity;
import com.john.graduate_project_mobile.Activities.Main.ui.rentCar.RentFragment;
import com.john.graduate_project_mobile.R;
import com.john.graduate_project_mobile.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {

    private RequestQueue requestQueue;
    private FragmentHomeBinding binding;
    //private HashMap<String, ImageView> cars = new HashMap<>();
    private LinearLayout frameLayout;
    private View viewload;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        frameLayout = root.findViewById(R.id.layout);
        viewload = LayoutInflater.from(getContext()).inflate(R.layout.loading, null);
        frameLayout.addView(viewload);
       /* System.out.println(cars);
        cars.forEach((s, imageView) -> {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.car_card, null);
            ImageView view1 = view.findViewById(R.id.imageView2);
            cars.put(s,view1);
            getImages(s);
            System.out.println(view1);
            frameLayout.addView(view);
        });*/
        //frameLayout.removeView();
        //final TextView textView = binding.textHome;
        //System.out.println(cars);
        //System.out.println(LocalDateTime.now());
        getCars();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
        //System.out.println("start "+ LocalDateTime.now());

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
                    getImages(response.getJSONObject(String.valueOf(i)).getString("licence"), view1);
                    TextView textView = view.findViewById(R.id.textViewModel);
                    textView.setText(response.getJSONObject(String.valueOf(i)).getString("model"));
                    TextView textView2 = view.findViewById(R.id.textViewPrice);
                    textView2.setText(response.getJSONObject(String.valueOf(i)).getString("value"));
                    Button btn = view.findViewById(R.id.RentButton);
                    btn.setTag(response.getJSONObject(String.valueOf(i)).getString("licence"));
                    btn.setOnClickListener(v -> {
                        ((MainActivity) getActivity()).changeFragment(new RentFragment());
                    });
                    frameLayout.addView(view);
                    //cars.put(response.getJSONObject(String.valueOf(i)).getString("licence"), null);
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getLocalizedMessage());
            }
        });
        requestQueue.add(request);
    }

    private void getImages(String name, ImageView imageView){
        String url = "http://10.0.2.2:8080/android/test?name="+name;

        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                //cars.get(name).setImageBitmap(response);
                imageView.setImageBitmap(response);
                //Imview.setImageBitmap(response);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(imageRequest);
    }
}