package com.example.srikiransistla.homework9;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

import com.example.srikiransistla.homework9.Movie;
import com.example.srikiransistla.homework9.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MyFirebaseRecylerAdapter extends FirebaseRecyclerAdapter<Movie, MyFirebaseRecylerAdapter.MovieViewHolder> {

    private Context mContext;
    static OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        public void onOverflowMenuClick(View v,final int position);
        public void onItemClick(View view, int position);

    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public MyFirebaseRecylerAdapter(Class<Movie> modelClass, int modelLayout,
                                    Class<MovieViewHolder> holder, Query ref, Context context) {
        super(modelClass, modelLayout, holder, ref);
        this.mContext = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void populateViewHolder(MovieViewHolder movieViewHolder, Movie movie, int i) {

        //TODO: Populate viewHolder by setting the movie attributes to cardview fields
        movieViewHolder.vTitle.setText(movie.getName());
        movieViewHolder.vDescription.setText(movie.getDescription());
        //Float rating = movie.getRating() / 2;
        //movieViewHolder.vRating.setRating(rating);
        movieViewHolder.vRatingnum.setText(movie.getStars());
        String decodedImage=movie.getImage();
        byte[] decodedBytes = Base64.decode(decodedImage, 0);
        Bitmap image2 = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        Drawable d = new BitmapDrawable(Resources.getSystem(), image2);



        //Log.d("Image String", decodedImage);
        //Picasso.with(mContext).load(movie.getUrl()).into(movieViewHolder.vIcon);
        movieViewHolder.vIcon.setImageBitmap(image2);
        //Picasso.with(mContext).load().into(movieViewHolder.vIcon);
        movieViewHolder.vIcon.setTransitionName(movie.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            movieViewHolder.vIcon.setTransitionName((String) movie.getName());
        }

    }

    //TODO: Populate ViewHolder and add listeners.
    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView vIcon;
        public TextView vTitle;
        public TextView vDescription;
        //public RatingBar vRating;
        public TextView vRatingnum;
        public ImageView vMenu;

        public MovieViewHolder(View v) {
            super(v);
            vIcon = (ImageView) v.findViewById(R.id.rv_icon);
            vTitle = (TextView) v.findViewById(R.id.rv_title);
            vDescription = (TextView) v.findViewById(R.id.rv_description);
            //vRating = (RatingBar) v.findViewById(R.id.rv_rating);
            vRatingnum = (TextView) v.findViewById(R.id.rv_rating_num);
            vMenu = (ImageView) v.findViewById(R.id.rv_overflow);

            if(vMenu!=null){
                vMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener!=null){
                            mItemClickListener.onOverflowMenuClick(v,getAdapterPosition());
                        }
                    }
                });
            }
            if(v != null){
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener!=null){
                            mItemClickListener.onItemClick(vIcon, getAdapterPosition());
                        }
                    }
                });
            }
        }
    }

}
