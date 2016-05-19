package com.adamjaynick.datemyson.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import com.adamjaynick.datemyson.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by amora on 5/17/2016.
 */
public class FacebookLoginFragment extends Fragment {

    private static final String TAG="FacebookLoginFragment";

    private LoginButton        mLoginButton;
    private GridLayout         mTopContainer;

    private CallbackManager    mCallbackManager;
    private AccessTokenTracker mAccessTokenTracker;
    private AccessToken        mAccessToken;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getActivity());

        mCallbackManager = CallbackManager.Factory.create();
        mAccessTokenTracker = new AccessTokenTracker(){
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Current acess token changed! ");
                if(oldAccessToken != null){
                    stringBuilder.append(" OLD: " + oldAccessToken.toString() + "  | ");
                }else{
                    stringBuilder.append(" OLD: NULL | ");
                }
                if(currentAccessToken != null){
                    stringBuilder.append(" NEW: " + currentAccessToken.toString());
                }else{
                    stringBuilder.append(" NEW: NULL");
                }

                Log.e(TAG, stringBuilder.toString());
            }

        };

        mAccessTokenTracker.startTracking();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if(accessToken != null){
            Log.e(TAG, "User already logged in");
        }else{

            Log.e(TAG, "NO USER LOGGED IN");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAccessTokenTracker.stopTracking();
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



        mTopContainer = (GridLayout) view.findViewById(R.id.topContainer);

        Bitmap bitmapOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.test_nyc);
        Bitmap blurredBackground = createBitmap_ScriptIntrinsicBlur(bitmapOriginal, 25.0f );

        Drawable d = new BitmapDrawable(blurredBackground);

        mTopContainer.setBackground(d);


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap createBitmap_ScriptIntrinsicBlur(Bitmap src, float r) {

        //Radius range (0 < r <= 25)
        if(r <= 0){
            r = 0.1f;
        }else if(r > 25){
            r = 25.0f;
        }

        Bitmap bitmap = Bitmap.createBitmap(
                src.getWidth(), src.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(getActivity());

        Allocation blurInput = Allocation.createFromBitmap(renderScript, src);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(r);
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();
        return bitmap;
    }
}
