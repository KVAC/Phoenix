package com.github.kvac.phoenix.endpoint.client.network;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.network.HostPortConnected;
import com.github.kvac.phoenix.libs.network.HostPortPair;
import com.github.kvac.phoenix.libs.objects.cs.S;
import com.github.kvac.phoenix.libs.objects.events.ra.request.Request;
import com.google.common.eventbus.Subscribe;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;

import lombok.Getter;
import lombok.Setter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phoenixendpointclient.phoenix.endpoint.client.events.ClientEventHEADER;

public class ClientHandler extends Thread implements Runnable {

    public ClientHandler() {
        register();
    }
    public static final CopyOnWriteArrayList<HostPortPair> hostPortPairs = new CopyOnWriteArrayList<HostPortPair>();

    @Getter
    protected final Logger loggerJ = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        minaMethodRun();
    }

    private void register() {
        EventHEADER.getSERVERS_EVENT_BUS().register(this);
        ClientEventHEADER.getREQUEST_REMOTE_EVENT_BUS().register(this);
    }

    @Getter
    @Setter
    private boolean loggerUse = false;

    private void minaMethodRun() {
        if (loggerUse) {
            NetWorkHeader.getConnector().getFilterChain().addLast("logger", new LoggingFilter());
        }
        ObjectSerializationCodecFactory objectSerializationCodecFactory = new ObjectSerializationCodecFactory();
        objectSerializationCodecFactory.setDecoderMaxObjectSize(Integer.MAX_VALUE);
        objectSerializationCodecFactory.setEncoderMaxObjectSize(Integer.MAX_VALUE);

        NetWorkHeader.getConnector().getFilterChain().addLast("codec-Serializable", new ProtocolCodecFilter(objectSerializationCodecFactory));
        NetWorkHeader.getConnector().setConnectTimeoutMillis(30 * 1000);
        NetWorkHeader.getConnector().setHandler(new MinaClientHandler());

        do {
            hostPortPairs.forEach(hostPortPair -> {
                HostPortConnected hpc = new HostPortConnected();
                hpc.setHost(hostPortPair.getHost());
                hpc.setPort(hostPortPair.getPort());
                if (!(HostPortConnected.avInList(NetWorkHeader.getHostPortConnectedList(), hpc))) {
                    new Thread(() -> {
                        try {
                            NetWorkHeader.getHostPortConnectedList().add(hpc);
                            connect(hpc);//TODO
                        } catch (Exception e) {
                            NetWorkHeader.getHostPortConnectedList().remove(hpc);
                        }
                    }).start();
                    hostPortPairs.remove(hostPortPair);
                }
            });
            //getLoggerJ().warn(NetWorkHeader.getHostPortConnectedList().size() + "");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                getLoggerJ().error("", ex);
            }
        } while (true);
    }

    private void connect(HostPortConnected hostPortConnected) throws IllegalStateException {
        ConnectFuture cf = NetWorkHeader.getConnector().connect(new InetSocketAddress(hostPortConnected.getHost(), hostPortConnected.getPort()));
        cf.awaitUninterruptibly();

        IoSession session = null;
        try {
            session = cf.getSession();

            hostPortConnected.setSession(session);
            //hostPortConnected.getSession().write(new Ping("ПИНГ"));
            //TODO
            //          Auth auth = new Auth();
            //        auth.setWho(NetWorkHeader.getMycs());
            //      cf.getSession().write(auth);
        } catch (Exception e) {
            cf.getSession().getCloseFuture().awaitUninterruptibly();
            //   NetWorkHeader.getConnector().dispose();
        }
    }

    private static int getRandomNumberInRange(int min, int max) throws IllegalArgumentException, NoSuchAlgorithmException {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = SecureRandom.getInstanceStrong();
        return r.nextInt((max - min) + 1) + min;
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
            if (host1.toLowerCase().equals(host2.toLowerCase())) {
                if (port1 == port2) {
                    return true;
                }
            }
        }
        return false;
    }

}
