package com.vidyo.connector.instance.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vidyo.connector.instance.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int PERMISSIONS_REQUEST_ALL = 0x7c9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setContentView(R.layout.activity_home);

        setSupportActionBar(findViewById(R.id.home_toolbar));

        enable(false);
        requestPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                onSettings();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onConnectRequested(View view) {
        startActivity(new Intent(this, VideoConferenceActivity.class));
    }

    public void onConnectFragmentRequested(View view) {
        startActivity(new Intent(this, HostFragmentActivity.class));
    }

    public void onSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT > 22) {
            List<String> permissionsNeeded = new ArrayList<>();
            for (String permission : PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                    permissionsNeeded.add(permission);
            }

            if (permissionsNeeded.size() > 0) {
                ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[0]), PERMISSIONS_REQUEST_ALL);
            } else {
                enable(true);
            }
        } else {
            enable(true);
        }
    }

    private void enable(boolean enable) {
        findViewById(R.id.home_connect).setEnabled(enable);
        findViewById(R.id.home_connect).setAlpha(enable ? 1f : 0.2f);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ALL) {
            requestPermissions();
        }
    }
}