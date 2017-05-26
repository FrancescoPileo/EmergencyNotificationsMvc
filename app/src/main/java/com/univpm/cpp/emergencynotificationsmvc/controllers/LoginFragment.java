package com.univpm.cpp.emergencynotificationsmvc.controllers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferences;
import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModel;
import com.univpm.cpp.emergencynotificationsmvc.models.session.SessionModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.local.LocalPreferencesImpl;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModel;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.views.login.LoginView;
import com.univpm.cpp.emergencynotificationsmvc.views.login.LoginViewImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class LoginFragment extends Fragment implements
        LoginView.LogAsGuestBtnViewListner,
        LoginView.LoginBtnViewListener,
        LoginView.RegistrationBtnViewListener
{

    private LoginView mLoginView;
    private UserModel mUserModel;
    private UserLoginTask mAuthTask;
    private LastUserGuestTask mLastUserGuestTask;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Instanzio le ViewMvc MVC relazionate con questo fragment
        mLoginView = new LoginViewImpl(inflater, container);
        mLoginView.setLoginListener(this);
        mLoginView.setGuestListener(this);
        mLoginView.setRegistrationListener(this);
        mLoginView.setToolbar(this);

        // Instanzio i Models MVC relazionati con questo fragment
        mUserModel = new UserModelImpl();

        //Controllo se è aperta un sessione di login
        LocalPreferencesImpl localPreferences = new LocalPreferencesImpl(getContext());
        if (localPreferences.alreadyLoged()){
            mLoginView.showProgress(true);
            mAuthTask = new UserLoginTask(localPreferences.getUsername(), localPreferences.getPassword());
            mAuthTask.execute((Void) null);
        }

        return mLoginView.getRootView();
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
        mLastUserGuestTask = new LastUserGuestTask();
        mLastUserGuestTask.execute((Void) null);
    }

    @Override
    public void onRegistrationClick() {
        Fragment newFragment = new RegistrationFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String password;

        UserLoginTask(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            User user = mUserModel.getUser(username);
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
                LocalPreferencesImpl localPreferences = new LocalPreferencesImpl(getContext());
                if (!localPreferences.alreadyLoged()) {
                    localPreferences.rememberLogin(username, password);
                }

                //inizia la sessione
                SessionTask mSessionTask = new SessionTask(username);
                mSessionTask.execute((Void) null);

                //carica il fragment della home
                Fragment newFragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
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




    public class LastUserGuestTask extends AsyncTask<Void, Void, Boolean> {

        private  User lastGuestUser;

        LastUserGuestTask() {
            this.lastGuestUser = null;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            lastGuestUser = mUserModel.getLastGuestUser();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mLastUserGuestTask = null;

            if (success) {
                int index = 0;
                if (lastGuestUser != null) index = Integer.parseInt(lastGuestUser.getUsername().substring(6));
                RegisterNewGuestTask task = new RegisterNewGuestTask(index + 1);
                task.execute((Void) null);
            }
        }

        @Override
        protected void onCancelled() {
            mLastUserGuestTask = null;
            mLoginView.showProgress(false);
        }

    }

    public class RegisterNewGuestTask extends AsyncTask<Void, Void, Boolean> {

        private final int index;

        RegisterNewGuestTask(int index) {
            this.index = index;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return mUserModel.newGuestUser(index);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mLoginView.showProgress(false);
            if (success) {
                //salva le credenziali in locale
                LocalPreferencesImpl localPreferences = new LocalPreferencesImpl(getContext());
                localPreferences.rememberLogin("Guest-" + String.valueOf(index),null);

                //inizia la sessione
                SessionTask mSessionTask = new SessionTask(localPreferences.getUsername());
                mSessionTask.execute((Void) null);

                //carica il fragment della home
                Fragment newFragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();

            } else {
                Log.w("Errore: ", "utente guest");
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
            User user = mUserModel.getUser(username);
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+1"));
            session = new Session();
            session.setUser(user);
            session.setTimeIn(dateFormat.format(date));
            SessionModel mSessionModel = new SessionModelImpl();
            return mSessionModel.newSession(session);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success){
                LocalPreferences localPreferences = new LocalPreferencesImpl(getContext());
                localPreferences.storeSession(session);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

}
