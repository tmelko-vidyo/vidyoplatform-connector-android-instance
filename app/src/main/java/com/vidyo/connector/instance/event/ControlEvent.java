package com.vidyo.connector.instance.event;

public class ControlEvent<T> extends BusBase<T, ControlEvent.Call> {

    public ControlEvent(Call call, T... value) {
        super(call, value);
    }

    public enum Call implements CallBase {
        CONNECT_DISCONNECT, MUTE_CAMERA, MUTE_MIC, MUTE_SPEAKER, CYCLE_CAMERA, DEBUG_OPTION, SEND_LOGS, BACK_PRESSED
    }

    @Override
    public Call getCall() {
        return super.getCall();
    }
}