package com.example.srikiransistla.homework9;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

public class Fragment_RecyclerView extends Fragment {

    private static final String ARG_MOVIE_DATA = "section_number";
    RecyclerView mRecyclerView;
    MyFirebaseRecylerAdapter mRecyclerViewAdapter;
    Movie movieData = new Movie();
    private List<Map<String, ?>> movieDataList;
    LinearLayoutManager mLayoutManager;
    final Firebase ref = new Firebase("https://srikiranhomework9.firebaseio.com/moviedata");


    public static Fragment_RecyclerView newInstance() {

        Fragment_RecyclerView fragment = new Fragment_RecyclerView();
        Bundle args = new Bundle();
        //args.putSerializable(ARG_MOVIE_DATA, (Serializable) movieData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.findItem(R.id.menu_search) == null)
            inflater.inflate(R.menu.menu_recycler, menu);

        Log.d("Message", (String) getActivity().getTitle());

        if (getActivity().getTitle().equals("Task 3")) {
            Log.d("Message", (String) getActivity().getTitle());
            Drawable drawable = menu.findItem(R.id.menu_search).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
        }

        SearchView search = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        Log.d("Message", "OnMenuOptions");
        if (search != null) {
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    int position1 =0;

                    while (position1<mRecyclerViewAdapter.getItemCount()){
                        Movie place = mRecyclerViewAdapter.getItem(position1);
                        if(place.getName().toLowerCase().contains(query.toLowerCase())){
                            break;
                        }
                        position1++;

                    }
                    if (position1 > 0) {
                        mRecyclerView.scrollToPosition(position1);
                        Toast.makeText(getContext(), "Place listed!", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getContext(), "Place not listed!", Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        final Fragment_List.OnListItemSelectedListener mListener;
        try {
            mListener = (Fragment_List.OnListItemSelectedListener) getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException("The hosting activity of the fragment" +
                    "forgot to implement onFragmentInteractionListener");
        }
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerViewAdapter = new MyFirebaseRecylerAdapter(Movie.class, R.layout.cardview,
                MyFirebaseRecylerAdapter.MovieViewHolder.class, ref, getActivity());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);


        mRecyclerViewAdapter.setOnItemClickListener(new MyFirebaseRecylerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position){

                Movie place=mRecyclerViewAdapter.getItem(position);
                //ArrayList<Object> PlacesVisited = place.getPlacesVisited();

                mListener.onListItemSelected(position, place, view);
            }

            @Override
            public void onOverflowMenuClick(View v, final int position) {
            }
        });

        //defaultAnimation();
        adapterAnimation();
        itemAnimation();


        return rootView;

    }

    //Animation
    private void defaultAnimation() {
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(500);
        animator.setRemoveDuration(100);

        mRecyclerView.setItemAnimator(animator);
    }

    private void itemAnimation() {
        ScaleInAnimator animator = new ScaleInAnimator();
        //SlideInLeftAnimator animator=new SlideInLeftAnimator();
        animator.setInterpolator(new OvershootInterpolator());

        animator.setAddDuration(300);
        animator.setRemoveDuration(300);

        mRecyclerView.setItemAnimator(animator);
    }

    private void adapterAnimation() {
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mRecyclerViewAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        //SlideInRightAnimationAdapter radapter=new SlideInRightAnimationAdapter(scaleAdapter);
        mRecyclerView.setAdapter(scaleAdapter);
    }


    public Fragment_RecyclerView() {

    }
}
