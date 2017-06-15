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
public class Fragment_HotelDetailView extends Fragment {
    private static final String HOTEL = "hotel";
    private HashMap<String, Object> hotel;
    ShareActionProvider mShareActionProvider;

    public static Fragment_HotelDetailView newInstance(HashMap<String, Object> hotel) {

        Fragment_HotelDetailView fragment = new Fragment_HotelDetailView();
        Bundle args = new Bundle();
        args.putSerializable(HOTEL, hotel);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_HotelDetailView() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments()!=null) {
            hotel = (HashMap<String, Object>) getArguments().getSerializable(HOTEL);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().setTitle("Movie list");

        View rootView = inflater.inflate(R.layout.hotel_detailview, container, false);
        final TextView hotel_name=(TextView) rootView.findViewById(R.id.hotel_name);
        final TextView hotel_cost=(TextView) rootView.findViewById(R.id.hotel_cost);
        final TextView hotel_exp=(TextView) rootView.findViewById(R.id.hotel_exp_det);
        final ImageView hotel_poster=(ImageView) rootView.findViewById(R.id.hotel_pic);
        final RatingBar hotel_rating=(RatingBar) rootView.findViewById(R.id.hotel_rating);
        final TextView hotel_ratingNum=(TextView) rootView.findViewById(R.id.hotel_rating_num);

        String hotName=(String) hotel.get("name");
        Double hotCost=(Double) hotel.get("cost");
        String hotExp=(String) hotel.get("experience");
        Double hotRating= (Double) hotel.get("rating");
        String image=(String) hotel.get("image");

        byte[] decodedBytes = Base64.decode(image, 0);
        Bitmap image2 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        hotel_name.setText(hotName);
        hotel_cost.setText(hotCost.floatValue()+"");
        hotel_exp.setText(hotExp);
        hotel_poster.setImageBitmap(image2);
        float f = hotRating.floatValue();
        hotel_rating.setRating(f);
        hotel_ratingNum.setText(2*f + "");
        return rootView;
    }


}
