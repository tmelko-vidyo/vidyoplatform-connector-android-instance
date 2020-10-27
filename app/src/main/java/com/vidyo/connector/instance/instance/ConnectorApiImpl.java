package com.vidyo.connector.instance.instance;

import android.view.View;

import com.vidyo.VidyoClient.Connector.Connector;
import com.vidyo.VidyoClient.Device.LocalCamera;
import com.vidyo.VidyoClient.Device.RemoteCamera;
import com.vidyo.VidyoClient.Device.VideoFrame;
import com.vidyo.VidyoClient.Endpoint.Participant;
import com.vidyo.connector.instance.event.ControlEvent;
import com.vidyo.connector.instance.utils.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Connector instance implementation.
 */
public class ConnectorApiImpl extends ConnectorListenersAdapter implements ConnectorApi {

    private static final int REMOTE_COUNT = 8;

    private final Connector connector;

    /* For the future rendering */
    private final Map<FrameType, View> frameTypeViewMap = new HashMap<>();

    private LocalCamera localCamera;
    private RemoteCamera remoteCamera;

    public ConnectorApiImpl() {
        this.connector = new Connector(null, Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default,
                REMOTE_COUNT, "debug@VidyoClient info@VidyoConnector info warning", "", 0);

        this.connector.registerLocalCameraEventListener(this);
        this.connector.registerLocalMicrophoneEventListener(this);
        this.connector.registerLocalSpeakerEventListener(this);

        this.connector.registerRemoteCameraEventListener(this);

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
    public void registerFrameContainer(FrameType frameType, View view) {
        this.frameTypeViewMap.put(frameType, view);
    }

    @Override
    public void restartFrameListener() {
        /* Re-select camera in order to get onSelected and start listen to frames over */
        this.connector.selectDefaultCamera();
    }

    @Override
    public void stopFramesListener() {
        if (localCamera != null)
            connector.unregisterLocalCameraFrameListener(localCamera);

        if (remoteCamera != null)
            connector.unregisterRemoteCameraFrameListener(remoteCamera);

        remoteCamera = null;
        localCamera = null;

        connector.selectLocalCamera(null);
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
    public void onLocalCameraSelected(LocalCamera localCamera) {
        if (localCamera == null) return;

        /* Drop listener for the old local camera because we might re-select it with cycle. */
        if (this.localCamera != null)
            connector.unregisterLocalCameraFrameListener(this.localCamera);

        this.localCamera = localCamera;

        if (!connector.registerLocalCameraFrameListener(this, this.localCamera, 1280, 720, 0)) {
            Logger.e("registerLocalCameraFrameListener failed");
        } else {
            Logger.i("registerLocalCameraFrameListener success");
        }
    }

    @Override
    public void onLocalCameraRemoved(LocalCamera localCamera) {
        if (localCamera != null) this.connector.unregisterLocalCameraFrameListener(localCamera);
        this.localCamera = null;
    }

    @Override
    public void onLocalCameraFrame(LocalCamera localCamera, VideoFrame videoFrame) {
        View view = frameTypeViewMap.get(FrameType.SELF);
        if (view != null) render(videoFrame, view);

        Logger.i(" local camera frame called videoFrameSize ------->  " + videoFrame.data.length + " VideoFrameFormat :: " + videoFrame.format
                + " width = " + videoFrame.width + " height = " + videoFrame.height);
    }

    @Override
    public void onRemoteCameraAdded(RemoteCamera remoteCamera, Participant participant) {
        if (localCamera == null) return;
        this.remoteCamera = remoteCamera;

        if (!connector.registerRemoteCameraFrameListener(this, remoteCamera, 360, 640, 0)) {
            Logger.e("registerRemoteCameraFrameListener failed");
        } else {
            Logger.i("registerRemoteCameraFrameListener success");
        }
    }

    @Override
    public void onRemoteCameraRemoved(RemoteCamera remoteCamera, Participant participant) {
        if (remoteCamera != null) connector.unregisterRemoteCameraFrameListener(remoteCamera);
        this.remoteCamera = null;
    }

    @Override
    public void onRemoteCameraFrame(RemoteCamera remoteCamera, Participant participant, VideoFrame videoFrame) {
        View view = frameTypeViewMap.get(FrameType.REMOTE);
        if (view != null) render(videoFrame, view);

        Logger.i(" remote camera frame called videoFrameSize ------->  " + videoFrame.data.length + " VideoFrameFormat :: " + videoFrame.format
                + " participant name :: " + participant.name + " width = " + videoFrame.width + " height = " + videoFrame.height);
    }

    private void render(VideoFrame videoFrame, View view) {
        // Render frame here. do Magic.
    }
}