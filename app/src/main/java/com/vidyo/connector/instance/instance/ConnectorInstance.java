package com.vidyo.connector.instance.instance;

import android.app.Activity;
import android.view.View;

import com.vidyo.VidyoClient.Connector.Connector;
import com.vidyo.VidyoClient.Connector.ConnectorPkg;
import com.vidyo.VidyoClient.Device.Device;
import com.vidyo.VidyoClient.Device.LocalCamera;
import com.vidyo.VidyoClient.Endpoint.LogRecord;
import com.vidyo.connector.instance.utils.AppUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Connector instance implementation.
 */
public class ConnectorInstance implements ConnectorApi, Connector.IRegisterLocalCameraEventListener, Connector.IRegisterLogEventListener {

    private static final int REMOTE_COUNT = 8;

    private Connector connector;
    private ConnectionWrapper connectionWrapper;

    private View frame;

    private AtomicBoolean isReleased = new AtomicBoolean(true);

    public ConnectorInstance(Activity activity, View frame, Connector.IConnect callback) {
        if (isReleased.get() && connector == null) {

            /* Keep the UI update callback */
            this.connectionWrapper = new ConnectionWrapper();
            this.connectionWrapper.feed(callback);

            /* Initial init block */
            ConnectorPkg.initialize();
            ConnectorPkg.setApplicationUIContext(activity);

            /* Keep the frame to release it further */
            this.frame = frame;

            this.connector = new Connector(this.frame, Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default,
                    REMOTE_COUNT, "debug@VidyoClient info@VidyoConnector info warning",
                    AppUtils.configLogFile(activity), 0);

            this.connector.registerLocalCameraEventListener(this);
            this.connector.registerLogEventListener(this, "debug@VidyoClient");

            isReleased.set(false);
        }
    }

    @Override
    public void connect(String portal, String room, String displayName, String pin, Connector.IConnect callback) {
        assertInstance();

        if (this.connectionWrapper != null) this.connectionWrapper.feed(callback);

        connector.connectToRoomAsGuest(portal, displayName, room, pin, this.connectionWrapper);
    }

    @Override
    public void showViewAt(View handler, int width, int height) {
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

    @Override
    public void release() {
        if (!isReleased.get() && connector != null) {
            /* Free IConnect reference */
            if (connectionWrapper != null) connectionWrapper.feed(null);
            connectionWrapper = null;

            connector.hideView(this.frame);

            connector.unregisterLocalCameraEventListener();
            connector.unregisterLogEventListener();

            connector.selectLocalCamera(null);
            connector.selectLocalMicrophone(null);
            connector.selectLocalSpeaker(null);

            connector.disable();
            connector = null;

            ConnectorPkg.setApplicationUIContext(null);
            ConnectorPkg.uninitialize();

            isReleased.set(true);
        }
    }

    @Override
    public void onLocalCameraAdded(LocalCamera localCamera) {

    }

    @Override
    public void onLocalCameraRemoved(LocalCamera localCamera) {

    }

    @Override
    public void onLocalCameraSelected(LocalCamera localCamera) {

    }

    @Override
    public void onLocalCameraStateUpdated(LocalCamera localCamera, Device.DeviceState deviceState) {

    }

    @Override
    public void onLog(LogRecord logRecord) {

    }

    private void assertInstance() {
        if (isReleased.get() || connector == null)
            throw new RuntimeException("Connector is not available anymore!");
    }
}