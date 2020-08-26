package com.github.kvac.phoenix.endpoint.client.network;

import java.net.InetSocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;

import com.github.kvac.phoenix.event.eventheader.EventHEADER;
import com.github.kvac.phoenix.libs.network.HostPortPair;
import com.github.kvac.phoenix.libs.network.Ping;
import com.github.kvac.phoenix.libs.objects.cs.S;
import com.github.kvac.phoenix.libs.objects.events.ra.request.Request;
import phoenixendpointclient.phoenix.endpoint.client.events.ClientEventHEADER;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;

import com.google.common.eventbus.Subscribe;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends Thread implements Runnable {

    public ClientHandler() {
        register();
    }
    @Getter
    private static final CopyOnWriteArrayList<HostPortPair> hostPortPairs = new CopyOnWriteArrayList<>();

    @Getter
    protected final Logger loggerJ = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        if (loggerUse) {
            NetWorkHeader.getConnector().getFilterChain().addLast("logger", new LoggingFilter());
        }
        ObjectSerializationCodecFactory objectSerializationCodecFactory = new ObjectSerializationCodecFactory();
        objectSerializationCodecFactory.setDecoderMaxObjectSize(Integer.MAX_VALUE);
        objectSerializationCodecFactory.setEncoderMaxObjectSize(Integer.MAX_VALUE);

        NetWorkHeader.getConnector().getFilterChain().addLast("codec-Serializable", new ProtocolCodecFilter(objectSerializationCodecFactory));
        NetWorkHeader.getConnector().setConnectTimeoutMillis(30 * 1000);
        NetWorkHeader.getConnector().setHandler(new MinaClientHandler());
        NetWorkHeader.getExecutorService().scheduleAtFixedRate(connectToServers(), 0, 3, TimeUnit.SECONDS);

        NetWorkHeader.getExecutorService().scheduleAtFixedRate(pingServers(), 0, 3, TimeUnit.SECONDS);
    }

    private void register() {
        EventHEADER.getSERVERS_EVENT_BUS().register(this);
        ClientEventHEADER.getREQUEST_REMOTE_EVENT_BUS().register(this);
    }

    @Getter
    @Setter
    private boolean loggerUse = false;

    private boolean connected(HostPortPair hostPortPair) {
        String host = hostPortPair.getHost();
        //AFTER DNS
        try {
            InetAddress giriAddress = java.net.InetAddress.getByName(host);
            host = giriAddress.getHostAddress();
        } catch (UnknownHostException e) {
            //IGNORE
        }
        int port = hostPortPair.getPort();

        Map<Long, IoSession> sessions = NetWorkHeader.getConnector().getManagedSessions();
        for (Map.Entry<Long, IoSession> entry : sessions.entrySet()) {
            IoSession session = entry.getValue();
            SocketAddress addressSession = session.getRemoteAddress();

            String hostSession = StringUtils.substringBetween(addressSession.toString(), "/", ":");
            int portSession = Integer.parseInt(StringUtils.substringAfter(addressSession.toString(), ":"));

            if (host.equals(hostSession) && port == portSession) {
                return true;
            }
        }
        return false;
    }

    private Runnable connectToServers() {
        return () -> {
            hostPortPairs.forEach(hostPortPair -> {
                if (!connected(hostPortPair)) {
                    new Thread(() -> {
                        connect(hostPortPair);
                    }, "Connect to " + hostPortPair.toString()).start();
                }
            });
        };
    }

    private void connect(HostPortPair hostPortPair) throws IllegalStateException, IllegalArgumentException {
        ConnectFuture cf = NetWorkHeader.getConnector().connect(new InetSocketAddress(hostPortPair.getHost(), hostPortPair.getPort()));
        cf.awaitUninterruptibly();
    }

    @Subscribe
    private void requestHandle(Request request) {
        loggerJ.info("req:" + request.getWho().getId());
    }

    @Subscribe
    public static void newConnectionPrepare(S server) {
        if (!containsSinHOSTPORTlist(hostPortPairs, server)) {
            HostPortPair pair = new HostPortPair();
            pair.setHost(server.getHost());
            pair.setPort(server.getPort());
            hostPortPairs.add(pair);
        }
    }

    public static boolean containsSinHOSTPORTlist(CopyOnWriteArrayList<HostPortPair> hostPortPairsList, S aaa) {
        String host1 = aaa.getHost();
        int port1 = aaa.getPort();
        for (HostPortPair hostPortPair : hostPortPairsList) {
            String host2 = hostPortPair.getHost();
            int port2 = hostPortPair.getPort();
            if (host1.toLowerCase().equals(host2.toLowerCase())
                    && port1 == port2) {
                return true;
            }
        }
        return false;
    }

    private Runnable pingServers() {
        return () -> {
            NetWorkHeader.getConnector().broadcast(new Ping("ping"));
        };
    }
}
