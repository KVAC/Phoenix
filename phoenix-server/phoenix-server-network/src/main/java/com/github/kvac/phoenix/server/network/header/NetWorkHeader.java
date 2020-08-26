package com.github.kvac.phoenix.server.network.header;

import com.github.kvac.phoenix.server.network.handler.NetWorkHandler;
import com.github.kvac.phoenix.server.network.server.MinaServerHandler;
import com.github.kvac.phoenix.server.network.server.Server;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import lombok.Getter;
import lombok.Setter;

public class NetWorkHeader {

    private NetWorkHeader() {
    }

    @Getter
    @Setter
    private static NetWorkHandler netWorkHandler = new NetWorkHandler();

    @Getter
    @Setter
    private static MinaServerHandler minaServerHandler = new MinaServerHandler();

    @Getter
    @Setter
    private static int serverport = 31000;

    @Getter
    @Setter
    private static Server server = new Server();

    @Getter
    @Setter
    private static NioSocketAcceptor acceptor = new NioSocketAcceptor();

}
