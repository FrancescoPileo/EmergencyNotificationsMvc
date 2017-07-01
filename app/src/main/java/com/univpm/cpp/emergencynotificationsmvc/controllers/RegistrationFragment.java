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
import android.widget.Toast;

import com.univpm.cpp.emergencynotificationsmvc.MainActivity;
import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;
import com.univpm.cpp.emergencynotificationsmvc.views.registration.RegistrationView;
import com.univpm.cpp.emergencynotificationsmvc.views.registration.RegistrationViewImpl;

//todo controllo email esistente
public class RegistrationFragment extends Fragment implements
        RegistrationView.RegisterBtnViewListener,
        RegistrationView.UsernameChangeViewListener
{

    public static final String TAG = "REGISTRATION_FRAGMENT";

    private RegistrationView mRegistrationView;
    private RegistrationFragment.UserRegTask mRegTask;
    private MainActivity activity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRegistrationView = new RegistrationViewImpl(inflater, container, getContext());
        mRegistrationView.setRegisterListener(this);
        mRegistrationView.setUsernameListener(this);
        mRegistrationView.setToolbar(this);

        activity = (MainActivity) getActivity();

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
                                String username, String email, String password, String passwordConfirm) {
        Log.w("Fragment", name);
        if (checkRegistrationFields(name, surname, age, mobilephone, username, email, password, passwordConfirm)){
            User user = new User(-1, name, surname, username, age, mobilephone, email, password);
            mRegistrationView.showProgress(true);
            mRegTask = new RegistrationFragment.UserRegTask(user);
            mRegTask.execute((Void) null);
        }
    }

    @Override
    public void onUsernameChange(String username) {
        //mRegistrationView.showProgress(true);
        CheckUsernameTask checkUsernameTask = new CheckUsernameTask(username);
        checkUsernameTask.execute((Void) null);
    }

    private boolean checkRegistrationFields(String name, String surname, int age, String mobilephone,
                                            String username, String email, String password, String passwordConfirm ){
        boolean f = true;



        if (TextUtils.isEmpty(passwordConfirm)){
            mRegistrationView.setPasswordConfirmError(getString(R.string.error_field_required));
            f = false;
        } else if (!passwordConfirm.equals(password)){
            mRegistrationView.setPasswordConfirmError(getString(R.string.error_invalid_repeatpassword));
            f = false;
        }

        if (TextUtils.isEmpty(password)){
            mRegistrationView.setPasswordError(getString(R.string.error_field_required));
            f = false;
        } else if (password.length() > 20) {
            mRegistrationView.setPasswordError(getString(R.string.error_lenght_password));
            f = false;
        } else if (!(password.length() > 8 && password.matches(".*[0-9].*")
                && password.matches(".*[a-z].*") && password.matches(".*[A-Z].*"))){
            mRegistrationView.setPasswordError(getString(R.string.error_char_password));
            f = false;
        }

        if (TextUtils.isEmpty(username)){
            mRegistrationView.setUsernameError(getString(R.string.error_field_required));
            f = false;
        } else if (username.length() > 20) {
            mRegistrationView.setUsernameError(getString(R.string.error_lenght_username));
            f = false;
        }

        // todo da controllare
        if (!TextUtils.isEmpty(mobilephone)) {
            if (mobilephone.length() > 20) {
                mRegistrationView.setMobilephoneError(getString(R.string.error_lenght_mobilephone));
                f = false;
            } else if (!mobilephone.matches(".*[+0-9].*")) {
                mRegistrationView.setMobilephoneError(getString(R.string.error_invalid_mobilephone));
                f = false;
            }
        }

        if (TextUtils.isEmpty(email)){
            mRegistrationView.setEmailError(getString(R.string.error_field_required));
            f = false;
        } else if (email.length() > 50) {
            mRegistrationView.setEmailError(getString(R.string.error_lenght_email));
            f = false;
        } else if (!(email.length() > 5 && email.contains("@") && email.contains("."))){
            mRegistrationView.setEmailError(getString(R.string.error_invalid_email));
            f = false;
        }

        if (TextUtils.isEmpty(surname)) {
            mRegistrationView.setSurnameError(getString(R.string.error_field_required));
            f = false;
        } else if (name.length() > 50){
            mRegistrationView.setSurnameError(getString(R.string.error_lenght_surname));
            f = false;
        }

        if (TextUtils.isEmpty(name)) {
            mRegistrationView.setNameError(getString(R.string.error_field_required));
            f = false;
        } else if (name.length() > 50){
            mRegistrationView.setNameError(getString(R.string.error_lenght_name));
            f = false;
        }

        return f;
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
            return activity.getUserModel().newUser(user);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            mRegistrationView.showProgress(false);

            if (success) {
                //visualizza messaggio corretta registrazione
                CharSequence text = getString(R.string.toast_registration_ok);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getContext(), text, duration);
                toast.show();

                //carica il fragment di login
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new LoginFragment(), LoginFragment.TAG);
                transaction.addToBackStack(null);

                transaction.commit();
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
            User user = activity.getUserModel().getUser(username);
            return (user == null);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            //mRegistrationView.showProgress(false);
            if (!success) {
                mRegistrationView.setUsernameError(getString(R.string.error_existing_username));
            }
        }

        @Override
        protected void onCancelled() {
            mRegTask = null;
            //mRegistrationView.showProgress(false);
        }
    }







}
