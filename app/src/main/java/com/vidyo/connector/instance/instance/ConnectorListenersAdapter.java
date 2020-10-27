package com.vidyo.connector.instance.instance;

import com.vidyo.VidyoClient.Connector.Connector;
import com.vidyo.VidyoClient.Device.Device;
import com.vidyo.VidyoClient.Device.LocalCamera;
import com.vidyo.VidyoClient.Device.LocalMicrophone;
import com.vidyo.VidyoClient.Device.LocalSpeaker;
import com.vidyo.VidyoClient.Device.RemoteCamera;
import com.vidyo.VidyoClient.Device.VideoFrame;
import com.vidyo.VidyoClient.Endpoint.LogRecord;
import com.vidyo.VidyoClient.Endpoint.Participant;

/**
 * Abstract implementation of Connector device/event listeners.
 * Should be extended within ConnectorApiImpl.
 * Example: onLocalCameraAdded
 */
public abstract class ConnectorListenersAdapter implements
        Connector.IRegisterLocalCameraEventListener,
        Connector.IRegisterLocalCameraFrameListener,

        Connector.IRegisterLocalMicrophoneEventListener,
        Connector.IConnect, Connector.IRegisterLocalSpeakerEventListener,

        Connector.IRegisterRemoteCameraEventListener,
        Connector.IRegisterRemoteCameraFrameListener,

        Connector.IRegisterLogEventListener {

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

    @Override
    public void onLocalMicrophoneAdded(LocalMicrophone localMicrophone) {

    }

    @Override
    public void onLocalMicrophoneRemoved(LocalMicrophone localMicrophone) {

    }

    @Override
    public void onLocalMicrophoneSelected(LocalMicrophone localMicrophone) {

    }

    @Override
    public void onLocalMicrophoneStateUpdated(LocalMicrophone localMicrophone, Device.DeviceState deviceState) {

    }

    @Override
    public void onLocalSpeakerAdded(LocalSpeaker localSpeaker) {

    }

    @Override
    public void onLocalSpeakerRemoved(LocalSpeaker localSpeaker) {

    }

    @Override
    public void onLocalSpeakerSelected(LocalSpeaker localSpeaker) {

    }

    @Override
    public void onLocalSpeakerStateUpdated(LocalSpeaker localSpeaker, Device.DeviceState deviceState) {

    }

    @Override
    public void onRemoteCameraAdded(RemoteCamera remoteCamera, Participant participant) {

    }

    @Override
    public void onRemoteCameraRemoved(RemoteCamera remoteCamera, Participant participant) {

    }

    @Override
    public void onRemoteCameraStateUpdated(RemoteCamera remoteCamera, Participant participant, Device.DeviceState deviceState) {

    }

    @Override
    public void onRemoteCameraFrame(RemoteCamera remoteCamera, Participant participant, VideoFrame videoFrame) {

    }

    @Override
    public void onLocalCameraFrame(LocalCamera localCamera, VideoFrame videoFrame) {

    }
}
