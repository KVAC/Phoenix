package com.github.kvac.phoenix.event.EventHEADER;

import com.google.common.eventbus.EventBus;

import lombok.Getter;

public class EventHEADER {

    @Getter
    private static final EventBus MESSAGES_EVENT_BUS = new EventBus();

    @Getter
    private static final EventBus SERVERS_EVENT_BUS = new EventBus();

    @Getter
    private static final EventBus SERVERS_REQUEST_BUS = new EventBus();
    @Getter
    private static final EventBus SERVERS_ANSWER_BUS = new EventBus();

    @Getter
    private static final EventBus bus_mysettings = new EventBus();

    @Getter
    private static final EventBus bus_cs_save = new EventBus();
    @Getter
    private static final EventBus bus_cs_show = new EventBus();

    @Getter
    private static final EventBus bus_cs_clear = new EventBus();

    @Getter
    private static final EventBus bus_Ping = new EventBus();
    @Getter
    private static final EventBus bus_Pong = new EventBus();
}
