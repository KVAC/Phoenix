package com.github.kvac.phoenix.server.network.server;

import java.net.InetSocketAddress;
import com.github.kvac.phoenix.server.network.header.NetWorkHeader;

import lombok.Getter;
import lombok.Setter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server extends Thread implements Runnable {

    @Getter
    protected final Logger loggerJ = LoggerFactory.getLogger(getClass());

    @Getter
    @Setter
    private boolean stop = false;

    @Getter
    @Setter
    private boolean loggerUse = false;

    @Override
    public void run() {
        try {
            NioSocketAcceptor acceptor = new NioSocketAcceptor();
            if (loggerUse) {
                acceptor.getFilterChain().addLast("logger", new LoggingFilter());
            }
            ObjectSerializationCodecFactory objectSerializationCodecFactory = new ObjectSerializationCodecFactory();
            objectSerializationCodecFactory.setDecoderMaxObjectSize(Integer.MAX_VALUE);
            objectSerializationCodecFactory.setEncoderMaxObjectSize(Integer.MAX_VALUE);

            acceptor.getFilterChain().addLast("codec-Serializable", new ProtocolCodecFilter(objectSerializationCodecFactory));
            acceptor.setHandler(NetWorkHeader.getMinaServerHandler());

            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
            acceptor.bind(new InetSocketAddress(NetWorkHeader.getServerport()));

        } catch (Exception e) {
            getLoggerJ().error("", e);
            System.exit(3);
        }

    }

}
