package com.univpm.cpp.emergencynotificationsmvc.views.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.utils.TouchImageView;

import java.util.ArrayList;


public class HomeViewImpl implements HomeView{

    private View mRootView;

    private View homeFormView;
    private MapSpnItemSelectedViewListener mapSelectedListener;

    private TouchImageView mapTiv;
    private Spinner mapsSpn;

    private View progressView;
    private Toolbar toolbar;

    private Context context;

    public HomeViewImpl(LayoutInflater inflater, @Nullable ViewGroup container, Context context) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        this.context = context;

        init();

        mapsSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mapSelectedListener != null){
                    mapSelectedListener.onMapSpnItemSelected((String) adapterView.getItemAtPosition(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (mapSelectedListener != null){
                    mapSelectedListener.onMapSpnItemSelected((String) adapterView.getItemAtPosition(0));
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

    @Override
    public void populateSpinner (ArrayList<String> list) {

        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList = list;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, stringArrayList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mapsSpn.setAdapter(dataAdapter);
    }

    @Override
    public void setMapSelectedListener(MapSpnItemSelectedViewListener listener) {
        this.mapSelectedListener = listener;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void setMapOnView(String path) {

        String str = path;
        int resID = context.getResources().getIdentifier(str , "drawable", context.getPackageName());
        Bitmap map = BitmapFactory.decodeResource(context.getResources(), resID);
        mapTiv.setImageBitmap(map);


        //questo l'abbiamo lasciato se ci servirà calcolare le dimensioni dello schermo, qui non serve

        /*WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthScreen = size.x;
        int heightScreen = size.y;*/

    }

    @Override
    public void setPosition (int x, int y) {

        Log.w("Coordinates", String.valueOf(x) + ", " + String.valueOf(y));

        Bitmap map = mapTiv.getBitmap();
        Bitmap marker= BitmapFactory.decodeResource(context.getResources(), R.drawable.marker);
        Bitmap overlay = Bitmap.createBitmap(map.getWidth(), map.getHeight(), map.getConfig());

        Log.w("Map dimension", String.valueOf(map.getWidth()) + ", " + String.valueOf(map.getHeight()));

        Canvas canvas = new Canvas (overlay);
        canvas.drawBitmap(map, 0, 0, null);
        canvas.drawBitmap(marker, x - marker.getWidth()/2, map.getHeight() - y - marker.getHeight(), null);

        mapTiv.setImageBitmap(overlay);
    }

}
