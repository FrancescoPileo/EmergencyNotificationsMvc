package com.univpm.cpp.emergencynotificationsmvc.views.login;

import android.support.v4.app.Fragment;

import com.univpm.cpp.emergencynotificationsmvc.views.ViewMvc;

/**
 * Questa interfaccia rappresenta al schermata di login nell'applicazione
 */
public interface LoginView extends ViewMvc {

    interface LoginBtnViewMcvListener {
        /**
         *  Questo callback è invocato quando è premuto il bottone di accesso
         */
        void onLoginClick(String username, String password);
    }

    interface RegisterBtnViewMvcListener {
        /**
         * Questo callback è invocato quando è premute il bottone di registrazione
         */

        void onRegistrationClick();
    }

    interface LogAsGuestBtnViewMvcListner {
        /**
         * Questo callback è invocato quando è premute il bottone di accesso
         * come utente non registrato
         */
        void onLogAsGuestClick();
    }

    /**
     * Imposta un listener che verrà notificato da questa ViewMvc
     * @param listener
     */
    void setLoginListener(LoginBtnViewMcvListener listener);

    void setRegistrationListener(RegisterBtnViewMvcListener listener);

    void setGuestListener(LogAsGuestBtnViewMvcListner listener);

    String getUsername();

    String getPassword();

    void setPasswordError(String error);

    void setUsernameError(String error);

    void showProgress(boolean show);

    void setToolbar(Fragment fragment);

}

