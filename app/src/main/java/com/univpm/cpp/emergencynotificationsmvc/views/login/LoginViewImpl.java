package com.univpm.cpp.emergencynotificationsmvc.views.login;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.univpm.cpp.emergencynotificationsmvc.R;

/**
 * Una implementazione dell'interfaccia {@link LoginView}
 */
public class LoginViewImpl implements LoginView {

    private View mRootView;
    private LoginBtnViewListener loginListener;
    private LogAsGuestBtnViewListner guestListener;
    private RegistrationBtnViewListener registrationListener;


    /**
     * Elementi UI
     */
    private EditText usernameETxt;
    private EditText passwordETxt;
    private View progressView;
    private View loginFormView;
    private Toolbar toolbar;
    private Button loginBtn;
    private Button registrationBtn;
    private Button logAsGuestBtn;

    public LoginViewImpl(LayoutInflater inflater, @Nullable ViewGroup container){
        mRootView = inflater.inflate(R.layout.fragment_login, container, false);

        init();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginListener != null) {
                    loginListener.onLoginClick(getUsername(), getPassword());
                }
            }
        });

        logAsGuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (guestListener != null){
                    guestListener.onLogAsGuestClick();
                }
            }
        });

        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registrationListener != null){
                    registrationListener.onRegistrationClick();
                }
            }
        });

    }

    private void init() {
        usernameETxt = (EditText) mRootView.findViewById(R.id.username);
        passwordETxt = (EditText) mRootView.findViewById(R.id.password);
        progressView = mRootView.findViewById(R.id.login_progress);
        loginFormView = mRootView.findViewById(R.id.login_form);
        toolbar = (Toolbar) mRootView.findViewById(R.id.tool_bar);
        loginBtn = (Button) mRootView.findViewById(R.id.sign_in_button);
        registrationBtn = (Button) mRootView.findViewById(R.id.registration_button);
        logAsGuestBtn = (Button) mRootView.findViewById(R.id.sign_as_guest_button);

        usernameETxt.setError(null);
        passwordETxt.setError(null);

        usernameETxt.requestFocus();

    }

    @Override
    public void setToolbar(Fragment fragment) {
        toolbar.setTitle(R.string.title_activity_login);
        ((AppCompatActivity)fragment.getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void setLoginListener(LoginBtnViewListener listener) {
        loginListener = listener;
    }

    @Override
    public void setRegistrationListener(RegistrationBtnViewListener listener) {
        registrationListener = listener;
    }

    @Override
    public void setGuestListener(LogAsGuestBtnViewListner listener) {
        guestListener = listener;
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
    public String getUsername() {
        return usernameETxt.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordETxt.getText().toString();
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

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
