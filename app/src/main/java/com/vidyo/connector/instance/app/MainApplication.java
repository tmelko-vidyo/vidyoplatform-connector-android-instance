package com.vidyo.connector.instance.app;

import android.app.Application;

import com.vidyo.connector.instance.instance.ConnectorSingleInstance;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ConnectorSingleInstance.initialize();
    }
}
