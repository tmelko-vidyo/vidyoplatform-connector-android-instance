package com.vidyo.connector.instance.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.vidyo.VidyoClient.Connector.Connector;
import com.vidyo.connector.instance.R;
import com.vidyo.connector.instance.event.ControlEvent;
import com.vidyo.connector.instance.instance.ConnectorApi;
import com.vidyo.connector.instance.instance.ConnectorSingleInstance;
import com.vidyo.connector.instance.utils.AppUtils;
import com.vidyo.connector.instance.utils.ConnectParams;
import com.vidyo.connector.instance.view.ControlToolbarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class VideoRawFramesActivity extends FragmentActivity {

    private ControlToolbarView controlView;
    private View progressBar;

    private ConnectorApi connectorApi;

    private FrameLayout selfViewFrame;
    private FrameLayout remoteViewFrame;

    private boolean quitState = false;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        if (connectorApi != null) {
            ControlToolbarView.State state = controlView.getState();
            connectorApi.setMode(Connector.ConnectorMode.VIDYO_CONNECTORMODE_Foreground);

            connectorApi.setCameraPrivacy(state.isMuteCamera());
            connectorApi.setMicrophonePrivacy(state.isMuteMic());
            connectorApi.setSpeakerPrivacy(state.isMuteSpeaker());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

        if (connectorApi != null) {
            connectorApi.setMode(Connector.ConnectorMode.VIDYO_CONNECTORMODE_Background);

            connectorApi.setCameraPrivacy(true);
            connectorApi.setMicrophonePrivacy(true);
            connectorApi.setSpeakerPrivacy(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frames_conference_layout);

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        controlView = findViewById(R.id.control_view);

        selfViewFrame = findViewById(R.id.self_video_frame);
        remoteViewFrame = findViewById(R.id.remote_video_frame);

        /* Here comes the only one initialization */
        connectorApi = ConnectorSingleInstance.getInstance();
        controlView.showVersion(connectorApi.getVersion(), false);

        connectorApi.registerFrameContainer(ConnectorApi.FrameType.SELF, selfViewFrame);
        connectorApi.registerFrameContainer(ConnectorApi.FrameType.REMOTE, remoteViewFrame);

        connectorApi.restartFrameListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void controlEvents(ControlEvent<?> event) {
        if (connectorApi == null) return;

        switch (event.getCall()) {
            case SUCCESS:
                Toast.makeText(this, R.string.connected, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                controlView.connectedCall(true);
                controlView.disable(false);
                break;
            case DISCONNECTED:
                Connector.ConnectorDisconnectReason disconnectReason = (Connector.ConnectorDisconnectReason) event.getValue();

                Toast.makeText(this, R.string.disconnected, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                controlView.connectedCall(false);
                controlView.disable(false);

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                if (quitState) finish();
                break;
            case FAILED:
                Connector.ConnectorFailReason failReason = (Connector.ConnectorFailReason) event.getValue();

                Toast.makeText(this, failReason.name(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                controlView.connectedCall(false);
                controlView.disable(false);

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                break;

            case CONNECT_DISCONNECT:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                progressBar.setVisibility(View.VISIBLE);
                controlView.disable(true);

                toggleConnectOrDisconnect((Boolean) event.getValue());
                break;
            case MUTE_CAMERA:
                connectorApi.setCameraPrivacy((Boolean) event.getValue());
                break;
            case MUTE_MIC:
                connectorApi.setMicrophonePrivacy((Boolean) event.getValue());
                break;
            case MUTE_SPEAKER:
                connectorApi.setSpeakerPrivacy((Boolean) event.getValue());
                break;
            case CYCLE_CAMERA:
                connectorApi.cycleCamera();
                break;
            case DEBUG_OPTION:
                boolean value = (Boolean) event.getValue();
                if (value) {
                    connectorApi.enableDebug(7776, "");
                } else {
                    connectorApi.disableDebug();
                }

                Toast.makeText(this, getString(R.string.debug_option) + value, Toast.LENGTH_SHORT).show();
                break;
            case SEND_LOGS:
                AppUtils.sendLogs(this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!connectorApi.isConnected()) {
            super.onBackPressed();
        } else {
            if (quitState) return;

            quitState = true;
            toggleConnectOrDisconnect(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectorApi.stopFramesListener();

        connectorApi.hideView(selfViewFrame);
        connectorApi.hideView(remoteViewFrame);
    }

    private void toggleConnectOrDisconnect(boolean connectOrDisconnect) {
        progressBar.setVisibility(View.VISIBLE);
        controlView.disable(true);

        if (connectOrDisconnect) {
            connectorApi.connect(ConnectParams.PORTAL_HOST, ConnectParams.PORTAL_ROOM, ConnectParams.DISPLAY_NAME, ConnectParams.PORTAL_PIN);
        } else {
            if (connectorApi != null) connectorApi.disconnect();
        }
    }
}
