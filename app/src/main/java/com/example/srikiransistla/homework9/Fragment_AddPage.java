package com.example.srikiransistla.homework9;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
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
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Srikiran Sistla on 4/24/2016.
 */
public class Fragment_AddPage extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    String name;
    Button next,next2;
    ImageView displayPic;
    TextView  title_prev, desc_prev, date_prev;
    EditText title, desc, uname, date;
    Movie newRecord = new Movie();
    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Location mLastLocation;

    public static Fragment_AddPage newInstance() {

        Fragment_AddPage fragment = new Fragment_AddPage();
        //Bundle args = new Bundle();
        //args.putSerializable(ARG_MOVIE_DATA, (Serializable) movieData);
        //fragment.setArguments(args);
        return fragment;
    }

    public Fragment_AddPage() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            buildGoogleApiClient();
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fv_googlemap);
        if (mMapFragment != null) {
            mMapFragment.getMapAsync(this);
        }
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
        LatLng syracuse = new LatLng(43.0481221, -76.1474244);
        mMap.addMarker(new MarkerOptions().position(syracuse).title("Marker in Syracuse"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(syracuse, 12.0f));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.new_record, container, false);
        displayPic = (ImageView) rootView.findViewById(R.id.fv_icon_prev);
        title = (EditText) rootView.findViewById(R.id.fv_addtitle);
        title_prev = (TextView) rootView.findViewById(R.id.fv_title_prev);
        desc = (EditText) rootView.findViewById(R.id.fv_adddesc);
        desc_prev = (TextView) rootView.findViewById(R.id.fv_description_prev);
        uname = (EditText) rootView.findViewById(R.id.fv_adduname);
        date = (EditText) rootView.findViewById(R.id.fv_adddate);
        date_prev = (TextView) rootView.findViewById(R.id.fv_rating_num_prev);
        next = (Button) rootView.findViewById(R.id.fv_done);

        desc.setBackgroundColor(Color.WHITE);
        uname.setBackgroundColor(Color.WHITE);
        date.setBackgroundColor(Color.WHITE);
        title.setBackgroundColor(Color.WHITE);



        final Fragment_List.OnListItemSelectedListener2 mListener;
        try {
            mListener = (Fragment_List.OnListItemSelectedListener2) getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }

        displayPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("Hello", "From button addimages");
                Intent galleyIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(galleyIntent, "Select Picture"), RESULT_LOAD_IMAGE);

            }
        });

        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    List<Address> addressList = null;
                    name = String.valueOf(title.getText());
                    newRecord.setName(name);
                    title_prev.setText(name);
                    if (name != null && !name.equals("")) {
                        Geocoder geocoder = new Geocoder(getContext());
                        try {
                            addressList = geocoder.getFromLocationName(name, 1);
                            Log.d("Map", "Debug");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        if (addressList.size() != 0) {
                            Address address = addressList.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            Double lat = address.getLatitude();
                            Double lon = address.getLongitude();
                            Log.d(lat.toString(), lon.toString());
                            mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        } else {
                            Toast.makeText(getActivity(), "Place not found on maps", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        desc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    newRecord.setDescription(String.valueOf(desc.getText()));
                    desc_prev.setText(String.valueOf(desc.getText()));
                }

            }
        });

        uname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    newRecord.setId(name + "-" + String.valueOf(uname.getText()));
                    Toast.makeText(getActivity(), "Uname saved", Toast.LENGTH_SHORT);
                    Log.d("Srikiran's Debug", "Inside onFocus");
                }

            }
        });

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    newRecord.setStars(String.valueOf(date.getText()));
                    date_prev.setText(String.valueOf(date.getText()));
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListItemSelected(0, newRecord);
            }
        });




        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //this method is called when a user selected a picture from the gallery
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && data != null) {

            Uri selectedImage = data.getData();
            displayPic.setImageURI(selectedImage);
            Bitmap image = ((BitmapDrawable) displayPic.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // The output stream holds the byte representation of the image
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // Compress the image to JPEG with Quality 100 and put into Bytearraystream
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT); //Encoded string value of the image

            //docoding logic
            /*byte[] decodedBytes = Base64.decode(encodedImage, 0);
            Bitmap image2 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            Log.d("Image String", encodedImage);*/

            newRecord.setImage(encodedImage);
        }
    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
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
        if (latLng_Prev == null) {
            latLng_Prev = latLng_Now;
        }
        //draw line between two locations:
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(latLng_Prev, latLng_Now)
                .width(5)
                .color(Color.RED));
        latLng_Prev = latLng_Now;
    }


}
