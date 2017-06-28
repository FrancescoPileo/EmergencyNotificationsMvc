package com.univpm.cpp.emergencynotificationsmvc.controllers;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univpm.cpp.emergencynotificationsmvc.EmergencyNotificationsMvc;
import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferences;
import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModel;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferencesImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.views.login.LoginView;
import com.univpm.cpp.emergencynotificationsmvc.views.login.LoginViewImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginFragment extends Fragment implements
        LoginView.LogAsGuestBtnViewListner,
        LoginView.LoginBtnViewListener,
        LoginView.RegistrationBtnViewListener
{

    private EmergencyNotificationsMvc application;
    private LoginView mLoginView;
    private UserLoginTask mAuthTask;
    private RegisterNewGuestTask mRegisterNewGuestTask;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        application = ((EmergencyNotificationsMvc) getActivity().getApplication());

        // Instanzio le ViewMvc MVC relazionate con questo fragment
        mLoginView = new LoginViewImpl(inflater, container);
        mLoginView.setLoginListener(this);
        mLoginView.setGuestListener(this);
        mLoginView.setRegistrationListener(this);
        mLoginView.setToolbar(this);

        return mLoginView.getRootView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //Controllo connessione
        if (!application.isConnectionEnabled()){
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog_Alert);

            builder.setTitle("Modalità Offline")
                    .setMessage(R.string.alert_offine_mode)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if (application.getLocalPreferences().alreadyLoged()){    //Controllo se è aperta un sessione di login
            mLoginView.showProgress(true);
            User user = application.getLocalPreferences().getUser();
            mAuthTask = new UserLoginTask(user.getUsername(), user.getPassword());
            mAuthTask.execute((Void) null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onLoginClick(String username, String password) {
        if (checkLoginFields(username, password)) {
            mLoginView.showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }


    /**
     * Controlla che i campi inseriti nella view siano validi
     * @param username username inserito nella view
     * @param password password inserita nella view
     * @return true: entrambi i campi validi, false: alemeno uno dei due campi non è valido
     */
    private boolean checkLoginFields(String username, String password){
        boolean f = true;
        if (TextUtils.isEmpty(username)){
            mLoginView.setUsernameError(getString(R.string.error_field_required));
            f = false;
        }
        if (TextUtils.isEmpty(password)){
            mLoginView.setPasswordError(getString(R.string.error_field_required));
            f = false;
        }

        return f;
    }

    //da fare in un task
    @Override
    public void onLogAsGuestClick() {
        mLoginView.showProgress(true);
        mRegisterNewGuestTask = new RegisterNewGuestTask();
        mRegisterNewGuestTask.execute((Void) null);
    }

    @Override
    public void onRegistrationClick() {
        if (application.isConnectionEnabled()) {
            Fragment newFragment = new RegistrationFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            transaction.commit();
        } else {
            AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);

            builder.setTitle("Errore")
                    .setMessage("Operazione non consentita in modalità offline, attiva una connessione e riprova")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String password;
        private User user;

        UserLoginTask(String username, String password) {
            this.username = username;
            this.password = password;
            this.user = null;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            user = application.getUserModel().getUser(username);
            if (user != null){
                return user.getPassword().equals(password);
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            mLoginView.showProgress(false);

            if (success) {
                //salva le credenziali in locale
                if (!application.getLocalPreferences().alreadyLoged()) {
                    application.getLocalPreferences().rememberUser(user);
                }

                //inizia la sessione se c'è connessione
                if (((EmergencyNotificationsMvc) getActivity().getApplication()).isConnectionEnabled()){
                    SessionTask mSessionTask = new SessionTask(username);
                    mSessionTask.execute((Void) null);
                } else {
                    //carica il fragment della home
                    Fragment newFragment = new HomeFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }


            }

            else {
                mLoginView.setPasswordError(getString(R.string.error_invalid_credentials));
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            mLoginView.showProgress(false);
        }
    }

    public class RegisterNewGuestTask extends AsyncTask<Void, Void, Boolean> {
        private int index;

        RegisterNewGuestTask() {
            index = 0;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            User lastGuestUser = application.getUserModel().getLastGuestUser();
            if (lastGuestUser != null) index = Integer.parseInt(lastGuestUser.getUsername().substring(6)) + 1;
            return application.getUserModel().newGuestUser(index);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mLoginView.showProgress(false);
            if (success) {

                //salva le credenziali in locale

                User guestUser = new User();
                guestUser.setUsername("Guest-" + String.valueOf(index));
                guestUser.setGuest(true);
                application.getLocalPreferences().rememberUser(guestUser);

                //inizia la sessione
                SessionTask mSessionTask = new SessionTask(guestUser.getUsername());
                mSessionTask.execute((Void) null);

            } else if (!application.isConnectionEnabled()){

                //salva le credenziali in locale
                LocalPreferencesImpl localPreferences = new LocalPreferencesImpl(getContext());
                User offlineGuestUser = new User();
                offlineGuestUser.setUsername("Guest-offline");
                offlineGuestUser.setGuest(true);
                localPreferences.rememberUser(offlineGuestUser);

                //carica il fragment della home
                Fragment newFragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();


            } else {
                Log.w("Errore", "Guest");
            }
        }

        @Override
        protected void onCancelled() {
            mLoginView.showProgress(false);
        }
    }


    public class SessionTask extends AsyncTask<Void, Void, Boolean> {

        private String username;
        private Session session;

        SessionTask(String username){

            this.username = username;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Log.w("Log", "SessionStart");
            User user = application.getUserModel().getUser(username);
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+1"));
            session = new Session();
            session.setUser(user);
            session.setTimeIn(dateFormat.format(date));

            SessionModel mSessionModel = new SessionModelImpl();
            boolean flag = mSessionModel.newSession(session);
            return flag;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success){
                Log.w("ook", "OK");
                application.getLocalPreferences().storeSession(session);

                //carica il fragment della home
                Fragment newFragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            } else {
                Log.w("Porco", "");
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

}
