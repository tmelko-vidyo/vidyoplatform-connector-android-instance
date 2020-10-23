package com.vidyo.connector.instance.instance;

import android.app.Activity;

import com.vidyo.VidyoClient.Connector.ConnectorPkg;

public class ConnectorSingleInstance {

    /**
     * A way to synchronize the connector instance so it won't be created twice at some point
     */
    private static class Holder {
        private static final ConnectorApi INSTANCE = new ConnectorApiImpl();
    }

    public static ConnectorApi getInstance() {
        /* Connector instance will be created via class initializer */
        return Holder.INSTANCE;
    }

    public static void initialize() {
        /* Initial init block. Called once per app lifecycle. */
        ConnectorPkg.initialize();
    }

    /**
     * You can provide context of ANY activity. Ideally it should be a root activity.
     * @param activityContext
     */
    public static void setActivityContext(Activity activityContext) {
        ConnectorPkg.setApplicationUIContext(activityContext);
    }
}