package com.gzgamut.vivitar.been;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by LGB on 2017/7/22.
 */

public class Event {

    @IntDef({EVENT_LIVE_DATA, EVENT_SYNC_SET, EVENT_SYNC_DATA, EVENT_SYNC_DATA_LIVE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type{}
    public static final int EVENT_LIVE_DATA = 1001;
    public static final int EVENT_SYNC_SET = 1002;
    public static final int EVENT_SYNC_DATA = 1003;
    public static final int EVENT_SYNC_DATA_LIVE = 1004;

    private int eventType;

    public Event(@Type int eventType) {this.eventType = eventType;}

    public int getEventType() {
        return eventType;
    }
}
