package com.github.kvac.phoenix.libs.objects.events;

import com.github.kvac.phoenix.libs.objects.MySettings;

import lombok.Getter;
import lombok.Setter;

public class MyEvent {

    public static enum TYPE {
        ALL_C,
        //
        NETWORK_SCAN, NETWORK_C,
        //
        THREAD_TERMINATE,
        //
        MESSAGE_C_SAVE, MESSAGE_C_SHOW,
        //
        DATABASE,
        //
        CS_C, DATABASE_CS_SAVE,
        //
        MYSETTINGS_SAVE, MYSETTINGS_SAVE_ADDR, MYSETTINGS_SAVE_NAME,
        //
        MYSETTINGS_SHOW, CS_SEARCHR, CS_SHOW
    }

    @Getter
    @Setter
    private TYPE type;
    @Getter
    @Setter
    private Object object;

    public static boolean isSettingsUpdate(MyEvent event) {
        return event.getType().equals(TYPE.MYSETTINGS_SHOW) && event.getObject() instanceof MySettings;
    }
}
