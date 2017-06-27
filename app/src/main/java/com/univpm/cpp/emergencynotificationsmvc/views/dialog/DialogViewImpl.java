package com.univpm.cpp.emergencynotificationsmvc.views.dialog;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.univpm.cpp.emergencynotificationsmvc.R;

public class DialogViewImpl implements DialogView {

    private View mRootView;

    private OkButtonListener okListener;

    private TextView nodeName;
    private TextView temp;
    private TextView tempValue;
    private TextView hum;
    private TextView humValue;
    private TextView acc;
    private TextView accValue;
    private TextView gyr;
    private TextView gyrValue;
    private Button okButton;

    private Context context;


    public DialogViewImpl(LayoutInflater inflater, @Nullable ViewGroup container, Context context) {

        mRootView = inflater.inflate(R.layout.dialog, container, false);
        this.context = context;

        init();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(okListener != null) {
                    okListener.onOkButtonClick();
                }

            }
        });

    }

    private void init() {

        nodeName = (TextView) mRootView.findViewById(R.id.NodeName);
        temp = (TextView) mRootView.findViewById(R.id.Temp);
        tempValue = (TextView) mRootView.findViewById(R.id.TempValue);
        hum = (TextView) mRootView.findViewById(R.id.Hum);
        humValue = (TextView) mRootView.findViewById(R.id.HumValue);
        acc = (TextView) mRootView.findViewById(R.id.Acc);
        accValue = (TextView) mRootView.findViewById(R.id.AccValue);
        gyr = (TextView) mRootView.findViewById(R.id.Gyr);
        gyrValue = (TextView) mRootView.findViewById(R.id.GyrValue);
        okButton = (Button) mRootView.findViewById(R.id.Okbutton);
    }


    public View getmRootView() {
        return mRootView;
    }

    public void setmRootView(View mRootView) {
        this.mRootView = mRootView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
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
    public void setOkButtonListener(OkButtonListener listener) {
        this.okListener = listener;
    }

    @Override
    public void setNodeNameText(String string) {
        this.nodeName.setText(string);
    }

    @Override
    public void setTempValueText(String string) {
        this.tempValue.setText(string);
    }

    @Override
    public void setHumValueText(String string) {
        this.humValue.setText(string);
    }

    @Override
    public void setAccValueText(String string) {
        this.accValue.setText(string);
    }

    @Override
    public void setGyrValueText(String string) {
        this.gyrValue.setText(string);
    }

    @Override
    public void setAllVisible() {
        temp.setVisibility(View.VISIBLE);
        hum.setVisibility(View.VISIBLE);
        acc.setVisibility(View.VISIBLE);
        gyr.setVisibility(View.VISIBLE);
        tempValue.setVisibility(View.VISIBLE);
        humValue.setVisibility(View.VISIBLE);
        accValue.setVisibility(View.VISIBLE);
        gyrValue.setVisibility(View.VISIBLE);
    }

    @Override
    public void setAllInvisible() {
        temp.setVisibility(View.INVISIBLE);
        hum.setVisibility(View.INVISIBLE);
        acc.setVisibility(View.INVISIBLE);
        gyr.setVisibility(View.INVISIBLE);
        tempValue.setVisibility(View.INVISIBLE);
        humValue.setVisibility(View.INVISIBLE);
        accValue.setVisibility(View.INVISIBLE);
        gyrValue.setVisibility(View.INVISIBLE);
    }


}
