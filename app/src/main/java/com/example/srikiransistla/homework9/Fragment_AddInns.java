package com.example.srikiransistla.homework9;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * Created by Srikiran Sistla on 4/27/2016.
 */
public class Fragment_AddInns extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;
    public static Movie movie;
    Button addmore, next;
    TextView title;
    EditText name,experience,cost,rating_num;
    ImageView displaypic;
    RatingBar rating;
    HashMap<String, Object> inn = new HashMap<String, Object>();

    public Fragment_AddInns() {
    }
    public static Fragment_AddInns newInstance(Movie movieObject) {

        Fragment_AddInns fragment = new Fragment_AddInns();
        movie=movieObject;
        //Bundle args = new Bundle();
        //args.putSerializable(ARG_MOVIE_DATA, (Serializable) movieData);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.add_hotel, container, false);

        addmore=(Button) rootView.findViewById(R.id.addinn_addmore);
        next=(Button) rootView.findViewById(R.id.addinn_done);

        title=(TextView) rootView.findViewById(R.id.addinn_title);
        name=(EditText) rootView.findViewById(R.id.addinn_name);
        experience=(EditText) rootView.findViewById(R.id.addinn_experience);
        cost=(EditText) rootView.findViewById(R.id.addinn_cost);
        rating_num=(EditText) rootView.findViewById(R.id.addinn_rating);

        displaypic=(ImageView) rootView.findViewById(R.id.addinn_displaypic);

        rating=(RatingBar) rootView.findViewById((R.id.addinn_ratingbar));

        final Fragment_List.OnListItemSelectedListener2 mListener;
        try {
            mListener = (Fragment_List.OnListItemSelectedListener2) getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }

        rating_num.setBackgroundColor(Color.WHITE);
        cost.setBackgroundColor(Color.WHITE);
        experience.setBackgroundColor(Color.WHITE);
        name.setBackgroundColor(Color.WHITE);

        displaypic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Hello", "From button addimages");
                Intent galleyIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleyIntent, RESULT_LOAD_IMAGE);

            }
        });

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    inn.put("name", String.valueOf(name.getText()));
                    title.setText(String.valueOf(name.getText()));
                }

            }
        });

        experience.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    inn.put("experience", String.valueOf(experience.getText()));
                    Toast.makeText(getContext(), String.valueOf(experience.getText()).equals("")+"", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cost.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try{
                        inn.put("cost", Float.valueOf(String.valueOf(cost.getText())));
                    }
                    catch (NumberFormatException ne){
                        Toast.makeText(getContext(), "Please add proper cost in 0.0 Format!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        rating_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    if (!hasFocus) {
                        inn.put("rating", Float.valueOf(String.valueOf(rating_num.getText())));
                        rating.setRating(Float.valueOf(String.valueOf(rating_num.getText())));
                    }
                }
                catch (NumberFormatException ne){
                    Toast.makeText(getContext(), "Please add proper Rating in 0.0 Format!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie.Hotel.add(inn);
                mListener.onListItemSelected(1,movie);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie.Hotel.add(inn);
                mListener.onListItemSelected(2,movie);
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
            displaypic.setImageURI(selectedImage);
            Bitmap image = ((BitmapDrawable) displaypic.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // The output stream holds the byte representation of the image
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // Compress the image to JPEG with Quality 100 and put into Bytearraystream
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT); //Encoded string value of the image

            //docoding logic
            /*byte[] decodedBytes = Base64.decode(encodedImage, 0);
            Bitmap image2 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            Log.d("Image String", encodedImage);*/

            inn.put("image",encodedImage);
        }
    }
}
