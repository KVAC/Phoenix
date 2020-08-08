package com.github.kvac.phoenix.server.network.header;

import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.server.network.handler.MinaCSSessionHandler;
import com.github.kvac.phoenix.server.network.handler.NetWorkHandler;
import com.github.kvac.phoenix.server.network.server.MinaServerHandler;
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
    private static MinaCSSessionHandler mcssh = new MinaCSSessionHandler();

    @Getter
    @Setter
    private static MinaServerHandler minaServerHandler = new MinaServerHandler();

    @Getter
    @Setter
    private static int serverport = 9123;

    @Getter
    @Setter
    private static Server server = new Server();

    @Getter
    @Setter
    private static ConcurrentHashMap chmConnectedCS = new ConcurrentHashMap<String, CS>();
}
