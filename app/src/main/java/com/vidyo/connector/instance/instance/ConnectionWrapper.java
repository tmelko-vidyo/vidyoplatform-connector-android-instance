package com.vidyo.connector.instance.instance;

import com.vidyo.VidyoClient.Connector.Connector;

/**
 * Bridge between connector & UI
 */
public class ConnectionWrapper implements Connector.IConnect {

    Connector.IConnect callback;

    void feed(Connector.IConnect callback) {
        this.callback = callback;
    }

    @Override
    public void onSuccess() {
        if (this.callback != null) this.callback.onSuccess();
    }

    @Override
    public void onFailure(Connector.ConnectorFailReason connectorFailReason) {
        if (this.callback != null) this.callback.onFailure(connectorFailReason);
    }

    @Override
    public void onDisconnected(Connector.ConnectorDisconnectReason connectorDisconnectReason) {
        if (this.callback != null) this.callback.onDisconnected(connectorDisconnectReason);
    }
}