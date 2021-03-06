package com.univpm.cpp.emergencynotificationsmvc.views.login;

import android.support.v4.app.Fragment;

import com.univpm.cpp.emergencynotificationsmvc.views.ViewMvc;

/**
 * Interfaccia che rappresenta la schermata di login nell'applicazione
 */
public interface LoginView extends ViewMvc {

    interface LoginBtnViewListener {
        /**
         *  Questo callback è invocato quando è premuto il bottone di accesso
         */
        void onLoginClick(String username, String password);
    }
    interface RegistrationBtnViewListener {
        /**
         * Questo callback è invocato quando è premuto il bottone di registrazione
         */
        void onRegistrationClick();
    }

    interface LogAsGuestBtnViewListner {
        /**
         * Questo callback è invocato quando è premuto il bottone di accesso
         * come utente non registrato
         */
        void onLogAsGuestClick();
    }

    interface ReconnectBtnViewListener {

        void onReconnectClick();
    }

    /**
     * Imposta un listener che verrà notificato da questa ViewMvc
     * @param listener
     */
    void setLoginListener(LoginBtnViewListener listener);

    void setRegistrationListener(RegistrationBtnViewListener listener);

    void setGuestListener(LogAsGuestBtnViewListner listener);

    void setReconnectListener(ReconnectBtnViewListener listener);

    String getUsername();

    String getPassword();

    void setPasswordError(String error);

    void setUsernameError(String error);

    void showProgress(boolean show);

    void setToolbar(Fragment fragment);

    void reconnectVisible(boolean flag);

}

