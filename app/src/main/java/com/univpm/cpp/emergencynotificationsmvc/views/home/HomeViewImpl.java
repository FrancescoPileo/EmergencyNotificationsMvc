package com.univpm.cpp.emergencynotificationsmvc.views.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.utils.TouchImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeViewImpl implements HomeView{

    private View mRootView;

    private View homeFormView;
    private MapSpnItemSelectedViewListener mapSelectedLisnener;

    private TouchImageView mapTiv;
    private Spinner mapsSpn;

    private View progressView;
    private Toolbar toolbar;

    private Context context;

    public HomeViewImpl(LayoutInflater inflater, @Nullable ViewGroup container, Context context) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        this.context = context;

        init();

        populateSpinner();

        mapsSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mapSelectedLisnener != null){
                    mapSelectedLisnener.onMapSpnItemSelected((String) adapterView.getItemAtPosition(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (mapSelectedLisnener != null){
                    mapSelectedLisnener.onMapSpnItemSelected((String) adapterView.getItemAtPosition(0));
                }
            }
        });
    }

    private void init() {

        mapsSpn = (Spinner) mRootView.findViewById(R.id.maps_spinner);
        mapTiv = (TouchImageView) mRootView.findViewById(R.id.map);
        toolbar = (Toolbar) mRootView.findViewById(R.id.tool_bar);
        progressView = mRootView.findViewById(R.id.loading_progress);

    }


    private void populateSpinner (ArrayList<Map> list) {

        ArrayList<Map> lista = new ArrayList<Map>();
        lista = list;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lista);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapsSpn.setAdapter(dataAdapter);
    }

    @Override
    public void setMapSlectedListener(MapSpnItemSelectedViewListener listener) {
        this.mapSelectedLisnener = listener;
    }

    @Override
    public String getMap() {
        return null; //todo
    }

    @Override
    public void setToolbar(Fragment fragment) {
        toolbar.setTitle(R.string.title_activity_home);
        ((AppCompatActivity)fragment.getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    @Override
    public void showProgress(boolean show){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = 200;

            homeFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            homeFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    homeFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            homeFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
