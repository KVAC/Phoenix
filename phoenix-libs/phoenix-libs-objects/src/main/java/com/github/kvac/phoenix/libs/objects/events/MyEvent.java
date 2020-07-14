package com.github.kvac.phoenix.libs.objects.events;

import com.github.kvac.phoenix.libs.objects.MySettings;

import lombok.Getter;
import lombok.Setter;

public class MyEvent {
    
	public static enum TYPE {
		ALL_C,
		//
		Network_scan, Network_C,
		//
		THREAD_TERMINATE,
		//
		Message_C_save, Message_C_show,
		//
		Database,
		//
		CS_C, Database_CS_SAVE,
		//
		MySettings_save, MySettings_save_addr, MySettings_save_name,
		//
		MySettings_show, CS_SearchR,CS_SHOW
	}

	@Getter
	@Setter
	private TYPE type;
	@Getter
	@Setter
	private Object object;

	public static boolean isSettingsUpdate(MyEvent event) {
		if (event.getType().equals(TYPE.MySettings_show) && event.getObject() instanceof MySettings) {
			return true;
		}
		return false;
	}
}
