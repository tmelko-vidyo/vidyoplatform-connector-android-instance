package com.vidyo.connector.instance.instance;

import com.vidyo.VidyoClient.Connector.Connector;

/**
 * Connector instance API
 */
public interface ConnectorApi {

    void assignRenderer(android.view.View view);

    void hideView(android.view.View view);

    void connect(String portal, String room, String displayName, String pin);

    void showViewAt(android.view.View handler, int width, int height);

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
}