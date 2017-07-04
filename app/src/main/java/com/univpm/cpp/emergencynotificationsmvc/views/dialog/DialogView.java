package com.univpm.cpp.emergencynotificationsmvc.views.dialog;

import com.univpm.cpp.emergencynotificationsmvc.views.ViewMvc;

/**
 * Interfaccia che rappresenta la finestra di dialogo che visualizza le informazioni di un beacon
 */
public interface DialogView extends ViewMvc {

    interface OkButtonListener {

        void onOkButtonClick();
    }

    void setOkButtonListener(OkButtonListener listener);

    void setNodeNameText(String string);
    void setTempValueText(String string);
    void setHumValueText(String string);
    void setAccXValueText(String string);
    void setAccYValueText(String string);
    void setAccZValueText(String string);
    //void setGyrValueText(String string);

    void setSuccess(boolean flag);

}
