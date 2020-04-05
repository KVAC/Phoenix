package com.github.kvac.phoenix.libs.objects;

import java.io.Serializable;

import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.github.kvac.phoenix.libs.objects.events.MyEvent.TYPE;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ougi
 *
 */
@DatabaseTable(tableName = "Messages")
public class Message implements Comparable<Message>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8416017450078607461L;

	@Getter
	@Setter
	@DatabaseField(id = true)
	private String messageID;
	@Getter
	@Setter

	@DatabaseField
	private String From;
	@Getter
	@Setter
	@DatabaseField
	private String To;
	@Getter
	@Setter

	@DatabaseField
	private String message;
	@Getter
	@Setter
	@DatabaseField
	private String message_encrypted;

	public enum statusType {
		created, delivered
	}

	public enum ttypeEncrypt {
		simple, pgp
	}

	@Getter
	@Setter
	@DatabaseField
	private ttypeEncrypt typeEncrypt;
	@Getter
	@Setter
	@DatabaseField
	private statusType status;
	@Getter
	@Setter
	@DatabaseField
	private long timeCreated;
	@Getter
	@Setter
	@DatabaseField
	private long timeDelivered;

	@Override
	public String toString() {
		return message;
	}

	public void save() {
		MyEvent event = new MyEvent();

		event.setType(TYPE.Message_C_save);
		event.setObject(this);

	}

	public int compareTo(Message o) {
		return this.timeCreated < o.timeCreated ? -1 : this.timeCreated > o.timeCreated ? 1 : 0;
	}

}
