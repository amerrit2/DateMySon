package com.adamjaynick.datemyson.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import com.adamjaynick.datemyson.R;


/**
 * Created by Adam on 10/11/2014.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    //Member functions
    protected abstract Fragment createFragment();


    protected int getLayoutRes(){   return R.layout.fragment_container;  }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }

    }
}
