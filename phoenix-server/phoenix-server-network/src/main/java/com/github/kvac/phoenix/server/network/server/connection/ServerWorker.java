package com.github.kvac.phoenix.server.network.server.connection;

import com.github.kvac.phoenix.libs.objects.Ping;
import com.github.kvac.phoenix.server.network.server.Server;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerWorker implements Runnable {

    protected static final Logger logger = LoggerFactory.getLogger(ServerWorker.class);
    @Getter
    @Setter
    private ServerWorker serverWorkerThis = this;

    //marker
    @Getter
    @Setter
    private boolean marker = false;
    //marker

    @Getter
    @Setter
    private Server server;

    @Getter
    @Setter
    private Socket client;

    @Getter
    @Setter
    private ObjectInputStream ois;

    public void openReaderStream() throws IOException {
        setOis(new ObjectInputStream(getClient().getInputStream()));
        logger.info("openReaderStream()");
    }

    public ServerWorker(Server server, Socket client) {
        setServer(server);
        setClient(client);
    }

    @Override
    public void run() {
        Thread.currentThread().setName("(ServerWorker_READER): " + getClient().getRemoteSocketAddress());
        try {
            openReaderStream();
            new Thread(new ServerWorker_Writer(serverWorkerThis)).start();
            Object object;
            while (marker == false) {
                object = getOis().readObject();
                if (object instanceof Ping) {
                    logger.info("PING");
                }
            }
        } catch (EOFException e) {
            logger.warn("incorrect data received", e);
            setMarker(true);
        } catch (StreamCorruptedException e) {
            logger.warn("incorrect data received");
            setMarker(true);
        } catch (SocketTimeoutException e) {
            setMarker(true);
        } catch (Exception e) {
            logger.warn("", e);
        }
        setMarker(true);
        try {
            logger.info(Thread.currentThread().getName() + " closing");
            getClient().close();
            logger.info(Thread.currentThread().getName() + " closed");
        } catch (IOException e) {
            logger.info(Thread.currentThread().getName() + " close", e);
        }
        logger.info(Thread.currentThread().getName() + " is stoped");

    }
}
