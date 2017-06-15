package com.example.srikiransistla.homework9;

/**
 * Created by kodali's on 4/17/2016.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.HashMap;

public class Fragment_MyRestaurants extends Fragment {
    private static final String RESTAURANT = "restaurant";
    private HashMap<String, Object> restaurant;
    private int total = 0;
    public static Fragment_MyRestaurants newInstance(HashMap<String, Object> restaurant) {

        Fragment_MyRestaurants fragment = new Fragment_MyRestaurants();
        Bundle args = new Bundle();
        args.putSerializable(RESTAURANT, restaurant);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_MyRestaurants() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (getArguments()!=null) {
            restaurant = (HashMap<String, Object>) getArguments().getSerializable(RESTAURANT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.restaurant_pager, container, false);
        ImageView image = (ImageView) rootView.findViewById(R.id.restaurant_home);

        String image_rest = (String) restaurant.get("image");

        byte[] decodedBytes = Base64.decode(image_rest, 0);
        Bitmap image2 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        image.setImageBitmap(image2);

        final OnViewPagerSelectedListener mListener;
        try{
            mListener=(OnViewPagerSelectedListener) getContext();
        } catch (ClassCastException e){
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mListener.onViewPagerItemSelected(restaurant);
            }
        });

        return rootView;
    }
    public interface OnViewPagerSelectedListener{
        public void onViewPagerItemSelected(HashMap<String, Object> restaurant);
    }
}
