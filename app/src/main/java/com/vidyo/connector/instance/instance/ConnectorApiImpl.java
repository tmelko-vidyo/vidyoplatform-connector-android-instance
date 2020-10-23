package com.vidyo.connector.instance.instance;

import com.vidyo.VidyoClient.Connector.Connector;
import com.vidyo.VidyoClient.Device.LocalCamera;
import com.vidyo.connector.instance.event.ControlEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Connector instance implementation.
 */
public class ConnectorApiImpl extends ConnectorListenersAdapter implements ConnectorApi {

    private static final int REMOTE_COUNT = 8;

    private Connector connector;

    public ConnectorApiImpl() {
        this.connector = new Connector(null, Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default,
                REMOTE_COUNT, "debug@VidyoClient info@VidyoConnector info warning", "", 0);

        this.connector.registerLocalCameraEventListener(this);
        this.connector.registerLocalMicrophoneEventListener(this);
        this.connector.registerLocalSpeakerEventListener(this);

        this.connector.registerLogEventListener(this, "debug@VidyoClient");
    }

    @Override
    public void onSuccess() {
        EventBus.getDefault().post(new ControlEvent<>(ControlEvent.Call.SUCCESS));
    }

    @Override
    public void onFailure(Connector.ConnectorFailReason connectorFailReason) {
        EventBus.getDefault().post(new ControlEvent<>(ControlEvent.Call.FAILED, connectorFailReason));
    }

    @Override
    public void onDisconnected(Connector.ConnectorDisconnectReason connectorDisconnectReason) {
        EventBus.getDefault().post(new ControlEvent<>(ControlEvent.Call.DISCONNECTED, connectorDisconnectReason));
    }

    @Override
    public void assignRenderer(android.view.View view) {
        connector.assignViewToCompositeRenderer(view, Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default, REMOTE_COUNT);
    }

    @Override
    public void hideView(android.view.View view) {
        connector.hideView(view);
    }

    @Override
    public void connect(String portal, String room, String displayName, String pin) {
        connector.connectToRoomAsGuest(portal, displayName, room, pin, this);
    }

    @Override
    public void showViewAt(android.view.View handler, int width, int height) {
        connector.showViewAt(handler, 0, 0, width, height);
    }

    @Override
    public void setMode(Connector.ConnectorMode mode) {
        connector.setMode(mode);
    }

    @Override
    public String getVersion() {
        return connector.getVersion();
    }

    @Override
    public void setCameraPrivacy(boolean state) {
        connector.setCameraPrivacy(state);
    }

    @Override
    public void setMicrophonePrivacy(boolean state) {
        connector.setMicrophonePrivacy(state);
    }

    @Override
    public void setSpeakerPrivacy(boolean state) {
        connector.setSpeakerPrivacy(state);
    }

    @Override
    public void disconnect() {
        connector.disconnect();
    }

    @Override
    public void cycleCamera() {
        connector.cycleCamera();
    }

    @Override
    public void enableDebug(int port, String level) {
        connector.enableDebug(port, level);
    }

    @Override
    public void disableDebug() {
        connector.disableDebug();
    }

    @Override
    public boolean isConnected() {
        Connector.ConnectorState state = connector.getState();
        return state != Connector.ConnectorState.VIDYO_CONNECTORSTATE_Idle && state != Connector.ConnectorState.VIDYO_CONNECTORSTATE_Ready;
    }

    /**
     * EVENTS
     */

    @Override
    public void onLocalCameraAdded(LocalCamera localCamera) {
        /* Whenever we need this callback. Just implement here */
    }
}