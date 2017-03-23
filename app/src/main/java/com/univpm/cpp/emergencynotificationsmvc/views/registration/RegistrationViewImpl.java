package com.univpm.cpp.emergencynotificationsmvc.views.registration;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.univpm.cpp.emergencynotificationsmvc.R;

public class RegistrationViewImpl implements RegistrationView {

    private View mRootView;
    private RegistrationView.RegisterBtnViewListener registerListener;
    private UsernameChangeViewListener usernameListener;

    private EditText nameETxt;
    private EditText surnameETxt;
    private Spinner ageSpn;
    private EditText usernameETxt;
    private EditText mobilephoneETxt;
    private EditText emailETxt;
    private EditText passwordETxt;
    private Button registerBtn;
    private View progressView;
    private View registrationFormView;
    private Toolbar toolbar;

    public RegistrationViewImpl(LayoutInflater inflater, @Nullable ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_registration, container, false);

        init();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registerListener != null) {
                    registerListener.onRegisterClick(getName(), getSurname(), getAge(),
                            getMobilephone(), getUsername(), getEmail(), getPassword());
                }
            }
        });

        usernameETxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    //todo
                    Log.w("Controlla", "username");
                }
            }
        });
    }

    private void init() {
        nameETxt = (EditText) mRootView.findViewById(R.id.name);
        surnameETxt = (EditText) mRootView.findViewById(R.id.surname);
        ageSpn = (Spinner) mRootView.findViewById(R.id.age);
        mobilephoneETxt = (EditText) mRootView.findViewById(R.id.phone);
        emailETxt = (EditText) mRootView.findViewById(R.id.email);
        usernameETxt = (EditText) mRootView.findViewById(R.id.username);
        passwordETxt = (EditText) mRootView.findViewById(R.id.password);
        progressView = mRootView.findViewById(R.id.registration_progress);
        registrationFormView = mRootView.findViewById(R.id.registration_form);
        toolbar = (Toolbar) mRootView.findViewById(R.id.tool_bar);
        registerBtn = (Button) mRootView.findViewById(R.id.register_button);
    }

    @Override
    public void setToolbar(Fragment fragment) {
        toolbar.setTitle(R.string.title_activity_registration);
        ((AppCompatActivity)fragment.getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void setRegisterListener(RegistrationView.RegisterBtnViewListener listener) {
        registerListener = listener;
    }

    @Override
    public void setUsernameListener(UsernameChangeViewListener listener) {
        usernameListener = listener;
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    @Override
    public String getName() {
        return nameETxt.getText().toString();
    }

    @Override
    public String getSurname() {
        return surnameETxt.getText().toString();
    }

    @Override
    public int getAge() {
        return (Integer) ageSpn.getSelectedItem();
    }

    @Override
    public String getMobilephone() {
        return mobilephoneETxt.getText().toString();
    }

    @Override
    public String getEmail() {
        return emailETxt.getText().toString();
    }

    @Override
    public String getUsername() {
        return usernameETxt.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordETxt.getText().toString();
    }

    @Override
    public void setNameError(String error) {
        nameETxt.setError(error);
        nameETxt.requestFocus();
    }

    @Override
    public void setSurnameError(String error) {
        surnameETxt.setError(error);
        surnameETxt.requestFocus();
    }

    @Override
    public void setMobilephoneError(String error) {
        mobilephoneETxt.setError(error);
        mobilephoneETxt.requestFocus();
    }

    @Override
    public void setEmailError(String error) {
        emailETxt.setError(error);
        emailETxt.requestFocus();
    }

    @Override
    public void setPasswordError(String error) {
        passwordETxt.setError(error);
        passwordETxt.requestFocus();
    }

    @Override
    public void setUsernameError(String error) {
        usernameETxt.setError(error);
        usernameETxt.requestFocus();
    }

    @Override
    public void showProgress(boolean show){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = 200;

            registrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            registrationFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            registrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
