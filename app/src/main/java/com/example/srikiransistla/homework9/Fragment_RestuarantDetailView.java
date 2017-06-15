package com.example.srikiransistla.homework9;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ShareActionProvider;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by kodali's on 4/29/2016.
 */
public class Fragment_RestuarantDetailView extends Fragment {
    private static final String RESTAURANT = "restaurant";
    private HashMap<String, Object> restaurant;
    ShareActionProvider mShareActionProvider;

    public static Fragment_RestuarantDetailView newInstance(HashMap<String, Object> restaurant) {

        Fragment_RestuarantDetailView fragment = new Fragment_RestuarantDetailView();
        Bundle args = new Bundle();
        args.putSerializable(RESTAURANT, restaurant);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_RestuarantDetailView() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments()!=null) {
            restaurant = (HashMap<String, Object>) getArguments().getSerializable(RESTAURANT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().setTitle("Movie list");

        View rootView = inflater.inflate(R.layout.restaurant_detailview, container, false);
        final TextView rest_name=(TextView) rootView.findViewById(R.id.restaurant_name);
        final TextView rest_cost=(TextView) rootView.findViewById(R.id.restaurant_cost);
        final TextView rest_exp=(TextView) rootView.findViewById(R.id.restaurant_exp_det);
        final ImageView rest_poster=(ImageView) rootView.findViewById(R.id.restaurant_pic);
        final RatingBar rest_rating=(RatingBar) rootView.findViewById(R.id.restaurant_rating);
        final TextView rest_ratingNum=(TextView) rootView.findViewById(R.id.restaurant_rating_num);

        String restName=(String) restaurant.get("name");
        Double restCost=(Double) restaurant.get("cost");
        String restExp=(String) restaurant.get("experience");
        Double restRating= (Double) restaurant.get("rating");
        String image=(String) restaurant.get("image");

        byte[] decodedBytes = Base64.decode(image, 0);
        Bitmap image2 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        rest_name.setText(restName);
        rest_cost.setText(restCost.floatValue()+"");
        rest_exp.setText(restExp);
        rest_poster.setImageBitmap(image2);
        float f = restRating.floatValue();
        rest_rating.setRating(f);
        rest_ratingNum.setText(f + "");
        return rootView;
    }

}
