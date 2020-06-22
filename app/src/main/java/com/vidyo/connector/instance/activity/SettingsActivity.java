package com.vidyo.connector.instance.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vidyo.connector.instance.R;
import com.vidyo.connector.instance.utils.ConnectParams;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView portal = findViewById(R.id.portal);
        TextView room = findViewById(R.id.room);
        TextView display = findViewById(R.id.display);

        portal.setText(ConnectParams.PORTAL_HOST);
        room.setText(ConnectParams.PORTAL_ROOM);
        display.setText(ConnectParams.DISPLAY_NAME);
    }
}