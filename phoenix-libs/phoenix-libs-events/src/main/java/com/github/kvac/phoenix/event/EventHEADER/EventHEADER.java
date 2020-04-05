package com.github.kvac.phoenix.event.EventHEADER;

import com.google.common.eventbus.EventBus;

import lombok.Getter;

public class EventHEADER {

	@Getter
	private static EventBus bus_mysettings = new EventBus();

	@Getter
	private static EventBus bus_cs_save = new EventBus();

	@Getter
	private static EventBus bus_cs_clear = new EventBus();

	@Getter
	private static EventBus bus_Ping = new EventBus();
	@Getter
	private static EventBus bus_Pong = new EventBus();

}
