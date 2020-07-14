package com.github.kvac.phoenix.server.network;

import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.server.network.handler.NetWorkHandler;
import com.github.kvac.phoenix.server.network.server.Server;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

public class NetWorkHeader {

    @Getter
    @Setter
    private static NetWorkHandler netWorkHandler = new NetWorkHandler();

    @Getter
    @Setter
    private static Server server = new Server();

    @Getter
    @Setter
    private static ConcurrentHashMap chmConnectedCS = new ConcurrentHashMap<String, CS>();
}
