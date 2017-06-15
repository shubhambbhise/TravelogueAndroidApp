package com.example.srikiransistla.homework9;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Srikiran Sistla on 4/27/2016.
 */
public class Fragment_AddPlace extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    public static Movie movie;
    ImageView displaypic, displaypic2;
    Button addmore, finalize;
    TextView title;
    EditText name, experience;
    HashMap<String, Object> place = new HashMap<String, Object>();
    ArrayList<String> images = new ArrayList<String>();
    final Firebase ref = new Firebase("https://srikiranhomework9.firebaseio.com/moviedata");
    //YouTubePlayer mPlayer;
    //String mVideoId;
    //private int RECOVERY_DIALOG_REQUEST = 1;
    private File imageFile;

    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Location mLastLocation;
    String place_name;

    public Fragment_AddPlace() {
    }

    public static Fragment_AddPlace newInstance(Movie movieObject) {

        Fragment_AddPlace fragment = new Fragment_AddPlace();
        movie = movieObject;
        //Bundle args = new Bundle();
        //args.putSerializable(ARG_MOVIE_DATA, (Serializable) movieData);
        //fragment.setArguments(args);
        return fragment;
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
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.addplace_googlemap);
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.add_place, container, false);

        /*YouTubePlayerSupportFragment playerFragment =
                (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.youtube_fragment);
        playerFragment.initialize(getString(R.string.google_maps_key), this);*/


        if (place.containsKey("images")) {
            images = (ArrayList<String>) place.get("images");
        }

        displaypic = (ImageView) rootView.findViewById(R.id.addplace_displaypic);
        displaypic2 = (ImageView) rootView.findViewById(R.id.addplace_displaypic2);

        addmore = (Button) rootView.findViewById(R.id.addplace_addmore);
        finalize = (Button) rootView.findViewById(R.id.addplace_done);

        title = (TextView) rootView.findViewById(R.id.addplace_title);
        name = (EditText) rootView.findViewById(R.id.addplace_name);
        experience = (EditText) rootView.findViewById(R.id.addplace_experience);

        final Fragment_List.OnListItemSelectedListener2 mListener;
        try {
            mListener = (Fragment_List.OnListItemSelectedListener2) getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }

        experience.setBackgroundColor(Color.WHITE);
        name.setBackgroundColor(Color.WHITE);

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    List<Address> addressList = null;
                    place_name = String.valueOf(name.getText());
                    place.put("name", place_name);
                    title.setText(place_name);
                    if (place_name != null && !place_name.equals("")) {
                        Geocoder geocoder = new Geocoder(getContext());
                        try {
                            addressList = geocoder.getFromLocationName(place_name, 1);
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
                            mMap.addMarker(new MarkerOptions().position(latLng).title(place_name));
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        } else {
                            Toast.makeText(getContext(), "Place not found on maps", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        experience.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    place.put("experience", String.valueOf(experience.getText()));
                    Log.d("Srikiran's Debug", "In experice onFocus");
                }

            }
        });

        displaypic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleyIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleyIntent.setType("image/*");
                galleyIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                galleyIntent.setAction(Intent.ACTION_GET_CONTENT);// MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(galleyIntent, "Select Picture"), RESULT_LOAD_IMAGE);

                //images.add();
            }
        });

        displaypic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //imageFile=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"test.jpg");
                //Uri tempuri = Uri.fromFile(imageFile);
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
                //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);

                //images.add();
            }
        });

        addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place.put("images", images);
                movie.PlacesVisited.add(place);
                mListener.onListItemSelected(2, movie);
            }
        });

        finalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experience.clearFocus();
                place.put("images", images);
                movie.PlacesVisited.add(place);
                ref.child(movie.getId()).setValue(movie);
                Intent intent = new Intent(getActivity(), MainActivity.class); //help taken to put getActivity()
                startActivity(intent);
                //add data to firebase
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //this method is called when a user selected a picture from the gallery
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && data != null) {
            Uri selectedImage;
            ClipData clip = data.getClipData();

            for (int i = 0; i < clip.getItemCount(); i++) {
                Log.d("Srikiran's Debug", "Inside Image uploader");
                ClipData.Item item = clip.getItemAt(i);
                selectedImage = item.getUri();

                displaypic.setImageURI(selectedImage);
                Bitmap image = ((BitmapDrawable) displaypic.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // The output stream holds the byte representation of the image
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // Compress the image to JPEG with Quality 100 and put into Bytearraystream
                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT); //Encoded string value of the image
                images.add(encodedImage);
                //((BitmapDrawable)displaypic.getDrawable()).getBitmap().recycle();

                // Process the uri...
            }
            /*selectedImage = data.getData();
            displaypic.setImageURI(selectedImage);
            Bitmap image = ((BitmapDrawable) displaypic.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // The output stream holds the byte representation of the image
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // Compress the image to JPEG with Quality 100 and put into Bytearraystream
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT); //Encoded string value of the image
            images.add(encodedImage);*/

            //docoding logic
            /*byte[] decodedBytes = Base64.decode(encodedImage, 0);
            Bitmap image2 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            Log.d("Image String", encodedImage);*/


        }

        if (requestCode == 0 && data != null) {
            Log.d("Srikiran's debug", "Picked camera image");
            //Uri selectedImage = data.getData();
            Bitmap image = (Bitmap) data.getExtras().get("data");
            //displaypic2.setImageURI(selectedImage);
            displaypic2.setImageBitmap(image);
            //Bitmap image = ((BitmapDrawable) displaypic2.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // The output stream holds the byte representation of the image
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // Compress the image to JPEG with Quality 100 and put into Bytearraystream
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT); //Encoded string value of the image
            images.add(encodedImage);
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

    /*@Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restored) {

        mPlayer=youTubePlayer;
        //Here we can set some flags on the player

        //This flag tells the player to switch to landscape when in fullscreen, it will also return to portrait
        //when leaving fullscreen
        //mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        //This flag tells the player to automatically enter fullscreen when in landscape. Since we don't have
        //landscape layout for this activity, this is a good way to allow the user rotate the video player.
        //mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        //mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        //mVideoId = new MovieData().getItem(1).get("image").toString();

        if (mVideoId != null) {
            if (restored) {
                mPlayer.play();
            } else {
                mPlayer = youTubePlayer;
                mPlayer.setFullscreen(true);
                mPlayer.loadVideo("2zNSgSzhBfM");
                mPlayer.play();
                mPlayer.loadVideo(mVideoId);
            }
        }
    }
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
        } else {
            //Handle the failure
            Toast.makeText(getActivity(), "onInitializationFailure", Toast.LENGTH_LONG).show();
        }
    }*/
}

