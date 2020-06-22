package com.vidyo.connector.instance.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vidyo.connector.instance.R;
import com.vidyo.connector.instance.event.ControlEvent;
import com.vidyo.connector.instance.fragment.VideoConferenceFragment;

import org.greenrobot.eventbus.EventBus;

public class HostFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_fragment);

        placeFragment(new VideoConferenceFragment());
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new ControlEvent<>(ControlEvent.Call.BACK_PRESSED));
    }

    private void placeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.addToBackStack(null);
        transaction.replace(R.id.navigation_container, fragment).commit();
    }
}