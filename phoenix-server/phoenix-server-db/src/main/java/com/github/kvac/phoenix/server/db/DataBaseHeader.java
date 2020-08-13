package com.github.kvac.phoenix.server.db;

import com.github.kvac.phoenix.libs.objects.ServerConfig;
import java.nio.charset.StandardCharsets;

import lombok.Getter;
import lombok.Setter;

public class DataBaseHeader {

    @Getter
    private static final Database dataBase = new Database();

    @Getter
    private static final DataBaseHandler dataBaseHandler = new DataBaseHandler();

    @Getter
    @Setter
    private static ServerConfig config = new ServerConfig();

    static StringBuilder dbPATH = new StringBuilder();

}
