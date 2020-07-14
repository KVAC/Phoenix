package com.github.kvac.phoenix.endpoint.client.db;

import lombok.Getter;

public class DataBaseHeader {

    /**
     *
     */
    public static final String DB_NAME_STRING = "jdbc:sqlite:JDCS.s3db";
    @Getter
    private static final DataBase dataBase = new DataBase();
    @Getter
    private static final DataBaseHandler dataBaseHandler = new DataBaseHandler();
}
