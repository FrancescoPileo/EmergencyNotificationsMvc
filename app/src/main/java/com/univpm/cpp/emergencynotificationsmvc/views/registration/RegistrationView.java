package com.univpm.cpp.emergencynotificationsmvc.views.registration;

import android.support.v4.app.Fragment;

import com.univpm.cpp.emergencynotificationsmvc.views.ViewMvc;

public interface RegistrationView extends ViewMvc {

    interface RegisterBtnViewListener {
        /**
         *  Questo callback è invocato quando è premuto il bottone di accesso
         */
        void onRegisterClick(String name, String surname, int age, String mobilephone,
                             String username, String email, String password);
    }

    interface UsernameChangeViewListener {
        /**
         *  Questo callback è invocato quando è premuto il bottone di accesso
         */
        void onUsernameChange(String username);
    }

    void setRegisterListener(RegistrationView.RegisterBtnViewListener listener);
    void setUsernameListener(UsernameChangeViewListener listener);

    String getName();
    String getSurname();
    int getAge();
    String getUsername();
    String getMobilephone();
    String getEmail();
    String getPassword();

    void setNameError(String error);
    void setSurnameError(String error);
    void setUsernameError(String error);
    void setMobilephoneError(String error);
    void setEmailError(String error);
    void setPasswordError(String error);

    void showProgress(boolean show);

    void setToolbar(Fragment fragment);
}
