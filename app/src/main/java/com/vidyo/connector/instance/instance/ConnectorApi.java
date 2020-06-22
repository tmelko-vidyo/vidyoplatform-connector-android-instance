package com.vidyo.connector.instance.instance;

import android.view.View;

import com.vidyo.VidyoClient.Connector.Connector;

/**
 * Connector instance API
 */
public interface ConnectorApi {

    void connect(String portal, String room, String displayName, String pin, Connector.IConnect callback);

    void showViewAt(View handler, int width, int height);

    void setMode(Connector.ConnectorMode mode);

    String getVersion();

    void setCameraPrivacy(boolean state);

    void setMicrophonePrivacy(boolean state);

    void setSpeakerPrivacy(boolean state);

    void disconnect();

    void cycleCamera();

    void enableDebug(int port, String level);

    void disableDebug();

    boolean isConnected();

    void release();
}