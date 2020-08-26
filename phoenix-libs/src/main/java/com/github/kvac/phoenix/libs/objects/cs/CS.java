package com.github.kvac.phoenix.libs.objects.cs;

import java.io.Serializable;

import com.github.kvac.phoenix.event.eventheader.EventHEADER;
import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.github.kvac.phoenix.libs.objects.events.MyEvent.TYPE;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jdcs_dev
 *
 */
@DatabaseTable(tableName = "CS")
public class CS implements Serializable {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final long serialVersionUID = -1806362634493171206L;

    @Getter
    @Setter
    @DatabaseField(id = true, unique = true)
    private String id;

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
    private String address;
    @Getter
    @Setter
    @DatabaseField
    private long addressTime;

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
    private int port;
    @Getter
    @Setter
    @DatabaseField
    private long portTime;
    @Getter
    @Setter
    @DatabaseField
    private boolean itsMe;

    @Getter
    @Setter
    @DatabaseField
    private long portUpdaterTime;
    @Getter
    @Setter
    @DatabaseField
    private int portUpdater;

    public void save() {
        try {
            if (getId() == null) {
                logger.error("ID == null", this);
                throw new NullPointerException("CS id NPE");
            }
            if (this.getId().equals("null")) {
                logger.error("ID equals null", this);
                throw new NullPointerException("CS id not correct (null)");
            }
            MyEvent event = new MyEvent();
            event.setType(TYPE.DATABASE_CS_SAVE);
            event.setObject(this);
            EventHEADER.getBus_cs_save().post(event);
        } catch (NullPointerException e) {
            logger.error("CS.save():EVENT ", e);
        }
    }

    @Override
    public String toString() {
        return "CS [ID=" + id + ", name=" + name + ", nameTime=" + nameTime + ", Address=" + address + ", Address_time="
                + addressTime + ", addressFSocket=" + addressFSocket + ", addressFSocketTime=" + addressFSocketTime
                + ", Port=" + port + ", PortTime=" + portTime + ", itsMe=" + itsMe + ", port_updaterTime="
                + portUpdaterTime + ", port_updater=" + portUpdater + "]";
    }
}
