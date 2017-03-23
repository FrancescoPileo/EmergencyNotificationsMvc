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
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModel;
import com.univpm.cpp.emergencynotificationsmvc.models.user.UserModelImpl;
import com.univpm.cpp.emergencynotificationsmvc.views.login.LoginView;
import com.univpm.cpp.emergencynotificationsmvc.views.login.LoginViewImpl;

public class LoginFragment extends Fragment implements
        LoginView.LogAsGuestBtnViewListner,
        LoginView.LoginBtnViewListener,
        LoginView.RegistrationBtnViewListener
{

    private LoginView mLoginView;
    private UserModel mUserModel;
    private UserLoginTask mAuthTask;

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

    @Override
    public void onLogAsGuestClick() {
        Log.w("Guest", "ok");
    }

    @Override
    public void onRegistrationClick() {
        /*
        Fragment newFragment = new RegistrationFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
        */
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
            // TODO: attempt authentication against a network service.
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
                Log.w(username, password);
            } else {
                mLoginView.setPasswordError(getString(R.string.error_invalid_credentials));
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            mLoginView.showProgress(false);
        }
    }


}
