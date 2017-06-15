package com.example.srikiransistla.homework9;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by kodali's on 4/18/2016.
 */
public class MyPlaceFragment extends Fragment {
    Movie placeData;
    ViewPager placesViewPager;
    MyFragmentPagerAdapter placesPagerAdapter;
    ViewPager hotelsViewPager;
    MyHotelsPagerAdapter hotelsPagerAdapter;
    ViewPager restaurantViewPager;
    MyRestaurantPagerAdapter restaurantPagerAdapter;
    private static final String ARG_PLACE = "myPlaces";


    public static MyPlaceFragment newInstance(Movie placesVisited) {
        MyPlaceFragment fragment = new MyPlaceFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLACE, placesVisited);
        fragment.setArguments(args);
        fragment.setSharedElementEnterTransition(new DetailsTransition());

        return fragment;
    }

    public MyPlaceFragment() {
        setRetainInstance(true);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("In OnCreate", "Places Activity");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments()!=null) {
            placeData = (Movie) getArguments().getSerializable(ARG_PLACE);
        }
        placesPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(),placeData.getPlacesVisited().size());
        restaurantPagerAdapter = new MyRestaurantPagerAdapter(getChildFragmentManager(),placeData.getRestaurants().size());
        hotelsPagerAdapter = new MyHotelsPagerAdapter(getChildFragmentManager(), placeData.getHotel().size());

        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("In OnCreateView", "Places Activity");

        View rootView = inflater.inflate(R.layout.myplace_detailview,container, false);
        /*if(placesPagerAdapter != null){
            placesPagerAdapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(),movieData1.getSize());
            Log.d("In OnCreateView", "In if condition");

        }*/

        ImageView imageView = (ImageView) rootView.findViewById(R.id.palce_image);
        //TextView placeName = (TextView) rootView.findViewById(R.id.main_place_name);
        TextView placeDesc = (TextView) rootView.findViewById(R.id.main_place_desc);
        CollapsingToolbarLayout toolbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

        String image=(String) placeData.getImage();
        String name = (String) placeData.getName();
        String desc = (String) placeData.getDescription();

        //placeName.setText(name);
        placeDesc.setText(desc);

        toolbar.setTitle(name);

        byte[] decodedBytes = Base64.decode(image, 0);
        Bitmap image2 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        imageView.setImageBitmap(image2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("Lollipop", "ha ha I am in");
            imageView.setTransitionName(name);
        }
        placesViewPager = (ViewPager) rootView.findViewById(R.id.places_pager);
        placesViewPager.setAdapter(placesPagerAdapter);
        //      placesViewPager.setCurrentItem(3);
        placesViewPager.setOffscreenPageLimit(2);


        TabLayout placeTabLayout = (TabLayout) rootView.findViewById(R.id.place_tabs);
        placeTabLayout.setupWithViewPager(placesViewPager);


        restaurantViewPager = (ViewPager) rootView.findViewById(R.id.restaurant_pager);
        restaurantViewPager.setAdapter(restaurantPagerAdapter);
        restaurantViewPager.setOffscreenPageLimit(2);

        TabLayout restauranttabLayout = (TabLayout) rootView.findViewById(R.id.restaurant_tabs);
        restauranttabLayout.setupWithViewPager(restaurantViewPager);

        hotelsViewPager = (ViewPager) rootView.findViewById(R.id.hotels_pager);
        hotelsViewPager.setAdapter(hotelsPagerAdapter);
        hotelsViewPager.setOffscreenPageLimit(2);


        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.hotel_tabs);
        tabLayout.setupWithViewPager(hotelsViewPager);
        customizeViewPager();


        return rootView;

    }

    private void customizeViewPager(){
        placesViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalized_position = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalized_position / 2 + 0.5f);
                page.setScaleY(normalized_position / 2 + 0.5f);
            }
        });

        hotelsViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalized_position = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalized_position / 2 + 0.5f);
                page.setScaleY(normalized_position / 2 + 0.5f);
            }
        });

        restaurantViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalized_position = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalized_position / 2 + 0.5f);
                page.setScaleY(normalized_position / 2 + 0.5f);
            }
        });
    }





    public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
        int count;
        public MyFragmentPagerAdapter(FragmentManager fm, int size){
            super(fm);
            count = size;
        }

        @Override
        public Fragment getItem(int position){
            Log.d("In get item", "Myplaces");
            return Fragment_MyPlaces.newInstance((HashMap<String, Object>) placeData.getPlacesVisited().get(position));
        }
        @Override
        public int getCount(){return count;}

        @Override
        public CharSequence getPageTitle(int position){
            Locale l = Locale.getDefault();
            HashMap<String, Object> places = (HashMap<String, Object>) placeData.getPlacesVisited().get(position);
            String name = (String) places.get("name");
            return name.toUpperCase(l);
        }

        @Override
        public float getPageWidth(int position){

            return 0.5f;//(You can choose it .For full screen per page you should give 1f)
        }

    }

    public class MyRestaurantPagerAdapter extends FragmentStatePagerAdapter {
        int count;
        public MyRestaurantPagerAdapter(FragmentManager fm, int size){
            super(fm);
            count = size;
        }

        @Override
        public Fragment getItem(int position){
            ArrayList<HashMap<String, Object>> restaurants = (ArrayList<HashMap<String, Object>>) placeData.getRestaurants();
            HashMap<String, Object> restaurant = (HashMap<String, Object>) restaurants.get(position);

            return Fragment_MyRestaurants.newInstance(restaurant);
        }
        @Override
        public int getCount(){return count;}

        @Override
        public CharSequence getPageTitle(int position){
            Locale l = Locale.getDefault();
            ArrayList<HashMap<String, Object>> restaurants = (ArrayList<HashMap<String, Object>>) placeData.getRestaurants();
            HashMap<String, Object> restaurant = (HashMap<String, Object>) restaurants.get(position);
            String name = (String) restaurant.get("name");
            return name.toUpperCase(l);
        }

        @Override
        public float getPageWidth(int position){

            return 0.5f;//(You can choose it .For full screen per page you should give 1f)
        }

    }


    public class MyHotelsPagerAdapter extends FragmentStatePagerAdapter {
        int count;
        public MyHotelsPagerAdapter(FragmentManager fm, int size){
            super(fm);
            count = size;
        }

        @Override
        public Fragment getItem(int position){
            ArrayList<HashMap<String, Object>> hotels = (ArrayList<HashMap<String, Object>>) placeData.getHotel();
            HashMap<String, Object> hotel = (HashMap<String, Object>) hotels.get(position);

            return Fragment_MyHotels.newInstance(hotel);
        }
        @Override
        public int getCount(){return count;}

        @Override
        public CharSequence getPageTitle(int position){
            Locale l = Locale.getDefault();
            ArrayList<HashMap<String, Object>> hotels = (ArrayList<HashMap<String, Object>>) placeData.getHotel();
            HashMap<String, Object> hotel = (HashMap<String, Object>) hotels.get(position);
            String name = (String) hotel.get("name");
            return name.toUpperCase(l);
        }

        @Override
        public float getPageWidth(int position){

            return 0.5f;//(You can choose it .For full screen per page you should give 1f)
        }



    }

    public static class DetailsTransition extends TransitionSet {
        public DetailsTransition() {
            setOrdering(ORDERING_TOGETHER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addTransition(new ChangeBounds())
                        .addTransition(new ChangeTransform())
                        .addTransition(new ChangeImageTransform());
            }
        }
    }


}
