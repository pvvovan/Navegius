package com.example.hellojni;

import android.app.Fragment;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.pvvovan.Navegius.View.MainFragment;
import com.pvvovan.Navegius.View.OnFragmentInteractionListener;

//import layout.DimensionsFragment;
//import layout.MainFragment;
//import layout.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameLayoutFrsg, MainFragment.newInstance("p1", "222"))
                    .commit();
        }
    }


    public void Close(View v){
        finish();
    }

    @Override
    public void onFinishActivity(String uri) {
        finish();
    }

    @Override
    public void onShowFragment(android.support.v4.app.Fragment newFragment) {
//        FragmentManager fm = getSupportFragmentManager();
//        fm.beginTransaction()
//                .remove(fm.findFragmentById(R.id.fragmentMm))
//                .commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutFrsg, newFragment)
                .commit();

    }

    @Override
    public void onBackPressed() {
        //return;
    }

}
