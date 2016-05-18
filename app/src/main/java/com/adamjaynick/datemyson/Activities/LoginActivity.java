package com.adamjaynick.datemyson.Activities;

import android.support.v4.app.Fragment;
import com.adamjaynick.datemyson.Fragments.FacebookLoginFragment;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends SingleFragmentActivity {

    /*
    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }*/

    @Override
    protected Fragment createFragment() {
        return new FacebookLoginFragment();
    }
}

