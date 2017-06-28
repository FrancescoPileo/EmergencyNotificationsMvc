package com.univpm.cpp.emergencynotificationsmvc.views.dialog;

import android.view.View;

import com.univpm.cpp.emergencynotificationsmvc.views.ViewMvc;


public interface DialogView extends ViewMvc {

    interface OkButtonListener {

        void onOkButtonClick();
    }

    void setOkButtonListener(OkButtonListener listener);

    void setNodeNameText(String string);
    void setTempValueText(String string);
    void setHumValueText(String string);
    void setAccValueText(String string);
    void setGyrValueText(String string);

    void setAllVisible();
    void setAllInvisible();
}
