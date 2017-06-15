package com.example.srikiransistla.homework9;

/**
 * Created by kodali's on 4/17/2016.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class Fragment_MyPlaces extends Fragment {
    private static final String ARG_PLACE = "place";
    private HashMap<String, Object> place;
    private int total = 0;
    public static Fragment_MyPlaces newInstance(HashMap<String, Object> place) {

        Fragment_MyPlaces fragment = new Fragment_MyPlaces();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLACE, place);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_MyPlaces() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (getArguments()!=null) {
            place = (HashMap<String, Object>) getArguments().getSerializable(ARG_PLACE);
        }
//        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Detail View", "On Create View");

        View rootView = inflater.inflate(R.layout.places_home, container, false);
        ImageView image = (ImageView) rootView.findViewById(R.id.place_home);

        ArrayList<String> images = (ArrayList<String>) place.get("images");

        byte[] decodedBytes = Base64.decode(images.get(0), 0);
        Bitmap image2 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        image.setImageBitmap(image2);

        final OnMyPlaceSelectedListener mListener;
        try{
            mListener=(OnMyPlaceSelectedListener) getContext();
        } catch (ClassCastException e){
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMyPlaceItemSelected(place);
            }
        });
        return rootView;
    }
    public interface OnMyPlaceSelectedListener{
        public void onMyPlaceItemSelected(HashMap<String, Object> place);
    }

}
