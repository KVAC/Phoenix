package com.github.kvac.phoenix.server.network.server;

import java.net.InetSocketAddress;
import com.github.kvac.phoenix.server.db.DataBaseHeader;
import com.github.kvac.phoenix.server.network.header.NetWorkHeader;

import lombok.Getter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server extends Thread implements Runnable {

    @Getter
    protected final Logger loggerJ = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        try {
            if (DataBaseHeader.getConfig().isDebug()) {
                NetWorkHeader.getAcceptor().getFilterChain().addLast("logger", new LoggingFilter());
            }
            ObjectSerializationCodecFactory objectSerializationCodecFactory = new ObjectSerializationCodecFactory();
            objectSerializationCodecFactory.setDecoderMaxObjectSize(Integer.MAX_VALUE);
            objectSerializationCodecFactory.setEncoderMaxObjectSize(Integer.MAX_VALUE);

            NetWorkHeader.getAcceptor().getFilterChain().addLast("codec-Serializable", new ProtocolCodecFilter(objectSerializationCodecFactory));
            NetWorkHeader.getAcceptor().setHandler(NetWorkHeader.getMinaServerHandler());

            NetWorkHeader.getAcceptor().getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
            NetWorkHeader.getAcceptor().bind(new InetSocketAddress(NetWorkHeader.getServerport()));
            loggerJ.info("Server started on " + NetWorkHeader.getServerport());
        } catch (Exception e) {
            getLoggerJ().error("", e);
            System.exit(3);
        }
    }
}
