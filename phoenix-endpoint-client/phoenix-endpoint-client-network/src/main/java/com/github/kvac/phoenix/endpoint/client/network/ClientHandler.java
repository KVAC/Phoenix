package com.github.kvac.phoenix.endpoint.client.network;

import com.github.kvac.phoenix.endpoint.client.network.connection.Client;
import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.HostPortConnected;
import com.github.kvac.phoenix.libs.objects.HostPortPair;
import com.github.kvac.phoenix.libs.objects.cs.S;
import com.google.common.eventbus.Subscribe;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends Thread implements Runnable {

    public ClientHandler() {
        EventHEADER.getSERVERS_EVENT_BUS().register(this);
    }

    public static CopyOnWriteArrayList<HostPortPair> hostPortPairs = new CopyOnWriteArrayList<>();

    protected static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private final Thread appender = new Thread(() -> {
        do {
            //serversList=
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);
    });

    @Override
    public void run() {
        appender.start();
        boolean random = false;
        do {
            HostPortPair randomObject = null;

            if (random) {
                int minrand = 0;
                int maxrand = hostPortPairs.size();
                if (maxrand < 1) {
                    continue;
                }
                int randId = getRandomNumberInRange(minrand, maxrand - 1);
                System.err.println("minrand:" + minrand + "\nmaxrand:" + maxrand + "\nrandId:" + randId + "\nhostPortPairs:" + hostPortPairs.size());
                randomObject = hostPortPairs.get(randId);

            } else {
                for (HostPortPair hostPortPair : hostPortPairs) {
                    if (randomObject == null) {
                        randomObject = hostPortPair;
                        hostPortPairs.remove(hostPortPair);
                    }
                }
            }

            if (randomObject != null) {
                String host = randomObject.getHost();
                int port = randomObject.getPort();
                HostPortConnected hpc = new HostPortConnected();
                hpc.setHost(host);
                hpc.setPort(port);

                if (!HostPortConnected.avInList(NetWorkHeader.getHostPortConnectedList(), hpc)) {
                    new Thread(() -> {
                        try {
                            Client client = new Client();
                            client.setHpc(hpc);
                            NetWorkHeader.getHostPortConnectedList().add(client.getHpc());

                            try {
                                client.connect();
                                logger.info("connected to " + client.getHpc().getHost() + ":" + client.getHpc().getPort());
                                //
                                //
                                new Thread(client).start();
                                //
                                //
                                // System.out.println(client.getHpc().getHost() + ":" + client.getHpc().getPort() + ":"             + (true == (client.getOos() != null)) + ":" + (true == (client.getOis() != null)));
                            } catch (IOException e) {
                                NetWorkHeader.getHostPortConnectedList().remove(client.getHpc());
                            }
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            logger.error("InterruptedException | IOException", ex);
                        }
                    }).start();
                }

                // logger.info("count connections:" + NetWorkHeader.getHostPortConnectedList().size());
                //NetWorkHeader.getHostPortConnectedList().forEach((hostPortConnected) -> {
                //      logger.info("HP:" + hostPortConnected);
                //    });
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                logger.error("InterruptedException:100", ex);
            }

        } while (true);
    }

    private static int getRandomNumberInRange(int min, int max) throws IllegalArgumentException {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
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
