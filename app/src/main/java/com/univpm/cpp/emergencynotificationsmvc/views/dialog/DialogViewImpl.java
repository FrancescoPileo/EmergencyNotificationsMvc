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
    private TextView accXValue;
    private TextView accXLabel;
    private TextView accYValue;
    private TextView accYLabel;
    private TextView accZValue;
    private TextView accZLabel;
    //private TextView gyr;
    //private TextView gyrValue;
    private Button okButton;
    private View infoView;
    private View errorView;

    private Context context;


    public DialogViewImpl(LayoutInflater inflater, @Nullable ViewGroup container, Context context) {

        mRootView = inflater.inflate(R.layout.dialog_details, container, false);
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
        accXValue = (TextView) mRootView.findViewById(R.id.AccXValue);
        accYValue = (TextView) mRootView.findViewById(R.id.AccYValue);
        accZValue = (TextView) mRootView.findViewById(R.id.AccZValue);
        accXLabel = (TextView) mRootView.findViewById(R.id.AccXLable);
        accYLabel = (TextView) mRootView.findViewById(R.id.AccYLable);
        accZLabel = (TextView) mRootView.findViewById(R.id.AccZLable);
        //gyr = (TextView) mRootView.findViewById(R.id.Gyr);
        //gyrValue = (TextView) mRootView.findViewById(R.id.GyrValue);
        okButton = (Button) mRootView.findViewById(R.id.Okbutton);
        infoView = mRootView.findViewById(R.id.info_view);
        errorView = mRootView.findViewById(R.id.error_view);

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
        this.tempValue.setText(subString(string));
    }

    @Override
    public void setHumValueText(String string) {
        this.humValue.setText(subString(string));
    }

    @Override
    public void setAccXValueText(String string) {
        this.accXValue.setText(subString(string));
    }

    @Override
    public void setAccYValueText(String string) {
        this.accYValue.setText(subString(string));
    }


    @Override
    public void setAccZValueText(String string) {
        this.accZValue.setText(subString(string));
    }

    private String subString(String string){
        int beginIndex = 0;
        int endIndex = Math.min(string.length(), 10);
        return string.substring(beginIndex,endIndex);
    }


    /*@Override
    public void setGyrValueText(String string) {
        this.gyrValue.setText(string);
    }*/

    @Override
    public void setSuccess(boolean flag) {
        if (flag) {
            errorView.setVisibility(View.GONE);
            temp.setVisibility(View.VISIBLE);
            hum.setVisibility(View.VISIBLE);
            acc.setVisibility(View.VISIBLE);
            //gyr.setVisibility(View.VISIBLE);
            tempValue.setVisibility(View.VISIBLE);
            humValue.setVisibility(View.VISIBLE);
            accXValue.setVisibility(View.VISIBLE);
            accYValue.setVisibility(View.VISIBLE);
            accZValue.setVisibility(View.VISIBLE);
            accXLabel.setVisibility(View.VISIBLE);
            accYLabel.setVisibility(View.VISIBLE);
            accZLabel.setVisibility(View.VISIBLE);
            //gyrValue.setVisibility(View.VISIBLE);
        } else {
            errorView.setVisibility(View.VISIBLE);
            temp.setVisibility(View.GONE);
            hum.setVisibility(View.GONE);
            acc.setVisibility(View.GONE);
            //gyr.setVisibility(View.GONE);
            tempValue.setVisibility(View.GONE);
            humValue.setVisibility(View.GONE);
            accXValue.setVisibility(View.GONE);
            accYValue.setVisibility(View.GONE);
            accZValue.setVisibility(View.GONE);
            accXLabel.setVisibility(View.GONE);
            accYLabel.setVisibility(View.GONE);
            accZLabel.setVisibility(View.GONE);
            //gyrValue.setVisibility(View.GONE);
        }

    }


}
