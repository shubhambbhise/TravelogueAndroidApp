package com.example.srikiransistla.homework9;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

/**
 * Created by Srikiran Sistla on 4/24/2016.
 */
public class Activity_AddPage extends AppCompatActivity implements Fragment_List.OnListItemSelectedListener2{
    private Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_addpage);

        mContent = Fragment_AddPage.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_addPage, mContent)
                .commit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onListItemSelected(int value, Movie movie) {

        switch (value) {
            case 0:
                mContent = Fragment_AddRestaurants.newInstance(movie);
                break;

            case 1:
                mContent = Fragment_AddInns.newInstance(movie);
                break;

            case 2:
                mContent = Fragment_AddPlace.newInstance(movie);
                break;


        }
        mContent.setEnterTransition(new Slide(Gravity.RIGHT));
        mContent.setExitTransition(new Slide(Gravity.LEFT));
        getSupportFragmentManager().beginTransaction() //loading fragment
                .replace(R.id.container_addPage, mContent)
                .addToBackStack(null)
                .commit();

    }


}
