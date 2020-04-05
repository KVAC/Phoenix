package com.github.kvac.phoenix.libs.objects.cs;

import java.beans.EventHandler;
import java.io.Serializable;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.github.kvac.phoenix.libs.objects.events.MyEvent.TYPE;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author jdcs_dev
 *
 */
@DatabaseTable(tableName = "CS")
public class CS implements Serializable {

	private static final long serialVersionUID = -1806362634493171206L;

	@Getter
	@Setter
	@DatabaseField(id = true)
	private String ID;

	@Getter
	@Setter
	@DatabaseField
	private String name;
	@Getter
	@Setter
	@DatabaseField
	private long nameTime;

	@Getter
	@Setter
	@DatabaseField
	private String Address;
	@Getter
	@Setter
	@DatabaseField
	private long Address_time;

	@Getter
	@Setter
	@DatabaseField
	private String addressFSocket;
	@Getter
	@Setter
	@DatabaseField
	private long addressFSocketTime;

	@Getter
	@Setter
	@DatabaseField
	private int Port;
	@Getter
	@Setter
	@DatabaseField
	private long PortTime;
	@Getter
	@Setter
	@DatabaseField
	private boolean itsMe;

	@Getter
	@Setter
	@DatabaseField
	private long port_updaterTime;
	@Getter
	@Setter
	@DatabaseField
	private int port_updater;

	public void save() {
		for (int i = 0; i < 1; i++) {
			if (getID() == null) {
				continue;
			}
			if (this.getID().equals("null")) {
				continue;
			}
			try {
				MyEvent event = new MyEvent();
				event.setType(TYPE.Database_CS_SAVE);
				event.setObject(this);
				EventHEADER.getBus_cs_save().post(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		return "CS [ID=" + ID + ", name=" + name + ", nameTime=" + nameTime + ", Address=" + Address + ", Address_time="
				+ Address_time + ", addressFSocket=" + addressFSocket + ", addressFSocketTime=" + addressFSocketTime
				+ ", Port=" + Port + ", PortTime=" + PortTime + ", itsMe=" + itsMe + ", port_updaterTime="
				+ port_updaterTime + ", port_updater=" + port_updater + "]";
	}
}