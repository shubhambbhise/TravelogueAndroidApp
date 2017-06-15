package com.example.srikiransistla.homework9;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.HashMap;

/**
 * Created by Srikiran Sistla on 2/25/2016.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        Fragment_MyRestaurants.OnViewPagerSelectedListener, Fragment_MyHotels.OnMyHotelSelectedListener,
        Fragment_MyPlaces.OnMyPlaceSelectedListener, Fragment_List.OnListItemSelectedListener,NavigationView.OnNavigationItemSelectedListener  {
    private Fragment mContent;
    private Fragment mContent1;
    Toolbar mToolBar;
    android.support.v7.app.ActionBar mActionBar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    final Firebase ref = new Firebase("https://srikiranhomework9.firebaseio.com");

    private static final String TAG_ADD = "sortAdd";
    Intent intent;

    /*@Override
    public void onListItemSelected(int position, HashMap<String, ?> movie) {
        Log.d("called interface","Hello");
        //Load another fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recycler_container, Fragment_DetailView.newInstance(movie))
                .addToBackStack(null)
                .commit();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_container);

        mToolBar = (Toolbar) findViewById(R.id.rv_toolbar);
        setSupportActionBar(mToolBar);
        mActionBar = getSupportActionBar();

        //mActionBar.setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //mActionBar.setLogo(R.drawable.me);

        //android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, mToolBar, R.string.open_drawer, R.string.close_drawer) {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);

                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }

                };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        if (savedInstanceState != null) {
            if (getSupportFragmentManager().getFragment(savedInstanceState, "mContent") != null) {
                mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
            } else {
                mContent = Fragment_RecyclerView.newInstance();
            }
        } else {
            mContent = Fragment_RecyclerView.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recycler_container, mContent)
                .commit();

        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.ic_add_white);



        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();

        actionButton.setTag(TAG_ADD);
        actionButton.setOnClickListener(this);

        /*ImageView subicon1 = new ImageView(this); // Create an icon
        subicon1.setImageResource(R.drawable.floating);

        ImageView subicon2 = new ImageView(this); // Create an icon
        subicon2.setImageResource(R.drawable.floating);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this); // repeat many times:
        SubActionButton buttonsubicon1 = itemBuilder.setContentView(subicon1).build();

        SubActionButton.Builder itemBuilder2 = new SubActionButton.Builder(this); // repeat many times:
        SubActionButton buttonsubicon2 = itemBuilder2.setContentView(subicon2).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonsubicon1)
                .addSubActionView(buttonsubicon2)
                        // ...
                .attachTo(icon)
                .build();*/



    }

    @Override
    public void onClick(View v){
        if (v.getTag().equals(TAG_ADD)){
            intent = new Intent(this, Activity_AddPage.class); //help taken to put getActivity()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //Log.d("Animator","inside if");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(this, v, "activityAnimation");
                this.startActivity(intent, options.toBundle());
            }
            else {
                startActivity(intent);
                //Log.d("Animator", "inside else");
            }
            //startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.about_me:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.recycler_view:
                Intent intent2 = new Intent(this, Activity_AddPage.class);
                startActivity(intent2);
                break;
            case R.id.recycler_view2:
                ref.unauth();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            default:
                startActivity(new Intent(this, MainActivity.class));
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mContent.isAdded())
            getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }


    public void onViewPagerItemSelected( HashMap<String, Object> restaurant) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recycler_container, Fragment_RestuarantDetailView.newInstance(restaurant))
                .addToBackStack(null)
                .commit();
    }

    public void onMyHotelItemSelected( HashMap<String, Object> hotel) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recycler_container, Fragment_HotelDetailView.newInstance(hotel))
                .addToBackStack(null)
                .commit();
    }

    public void onMyPlaceItemSelected( HashMap<String, Object> place) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recycler_container, MyPlaceDetailFragment.newInstance(place))
                .addToBackStack(null)
                .commit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onListItemSelected(int position, Movie myPlace, View v){
        //setContentView(R.layout.activity_recycler);

        MyPlaceFragment fragment = MyPlaceFragment.newInstance(myPlace);

        fragment.setSharedElementEnterTransition(new DetailsTransition());
        fragment.setEnterTransition(new Fade());
        fragment.setExitTransition(new Fade());
        fragment.setSharedElementReturnTransition(new DetailsTransition());

        getSupportFragmentManager().beginTransaction()
                .addSharedElement(v,v.getTransitionName())
                .replace(R.id.recycler_container, MyPlaceFragment.newInstance(myPlace))
                .addToBackStack(null)
                .commit();
        //Intent intent = new Intent(this, MyPlaces_Activity.class);
        // startActivity(intent);
    }

    public class DetailsTransition extends TransitionSet {
        public DetailsTransition() {
            setOrdering(ORDERING_TOGETHER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addTransition(new ChangeBounds())
                        .addTransition(new ChangeTransform())
                        .addTransition(new ChangeImageTransform());
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity2, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();

        switch (id) {
            case R.id.action_home:
                intent = new Intent(this, Activity_AddPage.class);
                startActivity(intent);
                return false;
            case R.id.action_logout:
                ref.unauth();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return false;

        }

        return super.onOptionsItemSelected(item);
    }

}
