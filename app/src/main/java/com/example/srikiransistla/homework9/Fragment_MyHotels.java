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

public class Fragment_MyHotels extends Fragment {
    private static final String HOTEL = "hotels";
    private HashMap<String, Object> hotel;
    private int total = 0;
    public static Fragment_MyHotels newInstance(HashMap<String, Object> hotel) {

        Fragment_MyHotels fragment = new Fragment_MyHotels();
        Bundle args = new Bundle();
        args.putSerializable(HOTEL, hotel);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_MyHotels() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (getArguments()!=null) {
            hotel = (HashMap<String, Object>) getArguments().getSerializable(HOTEL);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.hotel_pager, container, false);

        ImageView image = (ImageView) rootView.findViewById(R.id.hotel_home);

        String image_rest = (String) hotel.get("image");

        byte[] decodedBytes = Base64.decode(image_rest, 0);
        Bitmap image2 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        image.setImageBitmap(image2);




        final OnMyHotelSelectedListener mListener;
        try{
            mListener=(OnMyHotelSelectedListener) getContext();
        } catch (ClassCastException e){
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMyHotelItemSelected(hotel);
            }
        });
        return rootView;
    }

    public interface OnMyHotelSelectedListener{
        public void onMyHotelItemSelected(HashMap<String, Object> hotel);
    }

}
