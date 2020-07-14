package com.github.kvac.phoenix.libs.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.Setter;

@DatabaseTable(tableName = "Settings")
public class MySettings {

    @Getter
    @Setter
    @DatabaseField(id = true)
    private String MyID;

    @Getter
    @Setter
    @DatabaseField
    private long NameTime;
    @Getter
    @Setter
    @DatabaseField
    private String Name;

    @Getter
    @Setter
    @DatabaseField
    private long AddressTime;
    @Getter
    @Setter
    @DatabaseField
    private boolean  shareAddress;
    @Getter
    @Setter
    @DatabaseField
    private String Address;

    @Getter
    @Setter
    @DatabaseField
    private int port;
    @Getter
    @Setter
    @DatabaseField
    private int portMin;
    @Getter
    @Setter
    @DatabaseField
    private int portMax;

}
