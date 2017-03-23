package com.univpm.cpp.emergencynotificationsmvc.controllers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModel;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.views.registration.RegistrationView;
import com.univpm.cpp.emergencynotificationsmvc.views.registration.RegistrationViewImpl;

public class RegistrationFragment extends Fragment implements
        RegistrationView.RegisterBtnViewListener,
        RegistrationView.UsernameChangeViewListener
{

    private RegistrationView mRegistrationView;
    private UserModel mUserModel;
    private RegistrationFragment.UserRegTask mRegTask;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRegistrationView = new RegistrationViewImpl(inflater, container);
        mRegistrationView.setRegisterListener(this);
        mRegistrationView.setUsernameListener(this);
        mRegistrationView.setToolbar(this);

        mUserModel = new UserModelImpl();

        return mRegistrationView.getRootView();
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
    public void onRegisterClick(String name, String surname, int age, String mobilephone,
                                String username, String email, String password) {
        if (checkRegistrationFields(name, surname, age, mobilephone, username, email, password)){
            User user = new User(-1, name, surname, username, age, mobilephone, email, password);
            mRegistrationView.showProgress(true);
            mRegTask = new RegistrationFragment.UserRegTask(user);
            mRegTask.execute((Void) null);
        }
    }

    @Override
    public void onUsernameChange(String username) {
        CheckUsernameTask checkUsernameTask = new CheckUsernameTask(username);
        checkUsernameTask.execute((Void) null);
    }

    private boolean checkRegistrationFields(String name, String surname, int age, String mobilephone,
                                            String username, String email, String password ){
        //todo:
        return true;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegTask extends AsyncTask<Void, Void, Boolean> {

        private final User user;

        UserRegTask(User user) {
            this.user = user;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return mUserModel.newUser(user);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            mRegistrationView.showProgress(false);

            if (success) {
                Log.w("Logga", "Logga"); //todo deve loggare
            } else {
                mRegistrationView.setPasswordError(getString(R.string.error_registration));
            }
        }

        @Override
        protected void onCancelled() {
            mRegTask = null;
            mRegistrationView.showProgress(false);
        }

    }

    public class CheckUsernameTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;

        CheckUsernameTask(String username ) {
            this.username = username;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            User user = mUserModel.getUser(username);
            return (user == null);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            mRegistrationView.showProgress(false);
            if (!success) {
                mRegistrationView.setUsernameError(getString(R.string.error_existing_username));
            }
        }

        @Override
        protected void onCancelled() {
            mRegTask = null;
            mRegistrationView.showProgress(false);
        }
    }





}
