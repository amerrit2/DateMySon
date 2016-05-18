package com.adamjaynick.datemyson.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.adamjaynick.datemyson.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

/**
 * Created by amora on 5/17/2016.
 */
public class FacebookLoginFragment extends Fragment {

    private static final String TAG="FacebookLoginFragment";

    private LoginButton     mLoginButton;
    private CallbackManager mCallbackManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getActivity());

        mCallbackManager = CallbackManager.Factory.create();



        Log.e(TAG, "CREATED FACEBOOK LOGIN FRAGMENT");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_facebook_login, container, false);

        mLoginButton = (LoginButton) view.findViewById(R.id.login_button);
        mLoginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));
        mLoginButton.setFragment(this);

        float fbIconScale = 1.45F;
        Drawable drawable = getActivity().getResources().getDrawable(
                com.facebook.R.drawable.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*fbIconScale),
                (int)(drawable.getIntrinsicHeight()*fbIconScale));

        mLoginButton.setCompoundDrawables(drawable, null, null, null);
        mLoginButton.setCompoundDrawablePadding(getActivity().getResources().
                getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
        mLoginButton.setPadding(
                getActivity().getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr),
                getActivity().getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_top),
                0,
                getActivity().getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_bottom));




        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "Successful login!");
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "Cancelled login");

            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "Failed login :(");
            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
