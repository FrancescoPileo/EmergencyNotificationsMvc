package com.univpm.cpp.emergencynotificationsmvc.views.dialog;

import android.view.View;

import com.univpm.cpp.emergencynotificationsmvc.views.ViewMvc;


public interface DialogView extends ViewMvc {

    interface OkButtonListener {

        void onOkButtonClick();
    }

    void setOkButtonListener(DialogView.OkButtonListener listener);

    void setNodeNameText (String string);
    void setTempValueText (String string);
    void setHumValueText (String string);
    void setAccValueText (String string);
    void setGyrValueText (String string);

    void setTempVisible();
    void setHumVisible();
    void setAccVisible();
    void setGyrVisible();
    void setTempInvisible ();
    void setHumInvisible ();
    void setAccInvisible ();
    void setGyrInvisible ();
}
