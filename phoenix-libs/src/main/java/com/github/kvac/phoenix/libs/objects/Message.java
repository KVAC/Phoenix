package com.github.kvac.phoenix.libs.objects;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.event.PhoenixEvent.TYPE;
import com.github.kvac.phoenix.event.msg.MessageEvent;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ougi
 *
 */
@DatabaseTable(tableName = "Messages")
public class Message extends PhoenixObject implements Serializable {

    private static final long serialVersionUID = -8416017450078607461L;

    public enum field {
        MESSAGE_ID,
        //
        MESSAGE_From, MESSAGE_To,
        //
        MESSAGE, MESSAGE_encrypted,
        //
        MESSAGE_status
    }

    @Getter
    @Setter
    @DatabaseField(id = true, unique = true)
    private String messageID;
    @Getter
    @Setter

    @DatabaseField(foreign = true, columnName = "From_id")
    private CS from;
    @Getter
    @Setter
    @DatabaseField(foreign = true, columnName = "To_id")
    private CS to;
    @Getter
    @Setter

    @DatabaseField
    private String message;
    @Getter
    @Setter
    @DatabaseField
    private String message_encrypted;

    public enum StatusType {
        CREATED, DELIVERED
    }

    public enum TtypeEncrypt {
        SIMPLE, PGP
    }

    @Getter
    @Setter
    @DatabaseField
    private TtypeEncrypt typeEncrypt;
    @Getter
    @Setter
    @DatabaseField
    private StatusType status;
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
        MessageEvent event = new MessageEvent();
        event.setObject(this);
        event.setType(TYPE.MESSAGE_SAVE);
        EventHEADER.getMESSAGES_EVENT_BUS().post(event);
    }

    // public int compareTo(Message o) {
    //    return this.timeCreated < o.timeCreated ? -1 : this.timeCreated > o.timeCreated ? 1 : 0;
    // }
}
