package com.example.srikiransistla.homework9;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPlaceDetailFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    String name;
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Location mLastLocation;
    private int RECOVERY_DIALOG_REQUEST = 1;
    HashMap<String, Object> place;
    private static final String ARG_PLACE = "myPlaces";
    ViewPager placesViewPager;
    MyFragmentPagerAdapter placesPagerAdapter;



    public static MyPlaceDetailFragment newInstance(HashMap<String, Object> place){
        MyPlaceDetailFragment fragment = new MyPlaceDetailFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_PLACE, place);
        fragment.setArguments(args);

        return fragment;
    }

    public MyPlaceDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if(savedInstanceState==null){
            buildGoogleApiClient();
        }
        if (getArguments()!=null) {
            place = (HashMap<String, Object>) getArguments().getSerializable(ARG_PLACE);
        }
        ArrayList<String> images = (ArrayList<String>)place.get("images");
        placesPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(),images.size());

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googlemap);
        if(mMapFragment!=null){
            mMapFragment.getMapAsync(this);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_myplace, container, false);
        TextView placeName = (TextView) rootview.findViewById(R.id.subplace_name);
        TextView placeExp = (TextView) rootview.findViewById(R.id.subplace_exp);

        name=(String) place.get("name");

        placeName.setText((String) place.get("name"));
        placeExp.setText((String) place.get("experience"));

        placesViewPager = (ViewPager) rootview.findViewById(R.id.subbplace_pager);
        placesViewPager.setAdapter(placesPagerAdapter);

        placesViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalized_position = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalized_position / 2 + 0.5f);
                page.setScaleY(normalized_position / 2 + 0.5f);
            }
        });


        return rootview;
    }
    private void buildGoogleApiClient() {
        if(mGoogleApiClient==null){
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        LocationRequest mLocationRequest = createLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("onConnectionFailed ", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
    /*
    * set location request: frequency, priority
    * */
    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }
    LatLng latLng_Prev = null;
    @Override
    public void onLocationChanged(Location location) {
        //move camera when location changed
        LatLng latLng_Now = new LatLng(location.getLatitude(), location.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng_Now)      // Sets the center of the map to LatLng (refer to previous snippet)
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng_Now, 2.0f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom + 1.5f));
        //newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f)
        if(latLng_Prev == null){
            latLng_Prev = latLng_Now;
        }
        //draw line between two locations:
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(latLng_Prev, latLng_Now)
                .width(5)
                .color(Color.RED));
        latLng_Prev=latLng_Now;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng lat) {
                Toast.makeText(getContext(), "Latitude: " + lat.latitude + "\nLongitude: " + lat.longitude, Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng lat) {
                final Marker marker = mMap.addMarker(new MarkerOptions()
                        .title("self defined marker")
                        .snippet("Hello!")
                        .position(lat).visible(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//.icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
                );
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getContext(), marker.getTitle().toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));*/

        List<Address> addressList = null;
        if (name != null && !name.equals("")) {
            Geocoder geocoder = new Geocoder(getContext());
            try {
                addressList = geocoder.getFromLocationName(name, 1);
                //Toast.makeText(getActivity(), "Place coordinates found", Toast.LENGTH_SHORT).show();
                Log.d("Map", "Debug");
            } catch (IOException ex) {
                ex.printStackTrace();
                //Toast.makeText(getActivity(), "Place coordinates found", Toast.LENGTH_SHORT).show();
            }
            if (addressList.size() != 0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                Double lat = address.getLatitude();
                Double lon = address.getLongitude();
                Log.d(lat.toString(), lon.toString());
                mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                //Toast.makeText(getActivity(),lat+lon+"", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Place not found on maps", Toast.LENGTH_SHORT).show();
            }
        }

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
            return Fragment_MyPlacePics.newInstance((HashMap<String, Object>) place, position);
        }
        @Override
        public int getCount(){return count;}

        @Override
        public float getPageWidth(int position){

            return 0.5f;//(You can choose it .For full screen per page you should give 1f)
        }


    }


    public static class Fragment_MyPlacePics extends Fragment {
        private static final String ARG_PLACE = "place";
        private HashMap<String, Object> place;
        private int position;
        private static final String POSITION = "position";
        public static Fragment_MyPlacePics newInstance(HashMap<String, Object> place, int position) {

            Fragment_MyPlacePics fragment = new Fragment_MyPlacePics();
            Bundle args = new Bundle();
            args.putSerializable(ARG_PLACE, place);
            args.putSerializable(POSITION, position);
            fragment.setArguments(args);
            return fragment;
        }

        public Fragment_MyPlacePics() {

        }
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

            if (getArguments()!=null) {
                place = (HashMap<String, Object>) getArguments().getSerializable(ARG_PLACE);
                position = (int) getArguments().getSerializable(POSITION);
            }
//        setRetainInstance(true);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.d("Detail View", "On Create View");

            View rootView = inflater.inflate(R.layout.places_pics, container, false);
            ImageView image = (ImageView) rootView.findViewById(R.id.placepics_home);

            ArrayList<String> images = (ArrayList<String>) place.get("images");

            byte[] decodedBytes = Base64.decode(images.get(position), 0);
            Bitmap image2 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            image.setImageBitmap(image2);
            return rootView;
        }

    }

}
