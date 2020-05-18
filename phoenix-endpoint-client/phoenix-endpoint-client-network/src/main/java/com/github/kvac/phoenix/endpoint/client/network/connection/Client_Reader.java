/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.kvac.phoenix.endpoint.client.network.connection;

import com.github.kvac.phoenix.libs.objects.Ping;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.SocketTimeoutException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jdcs_dev
 */
public class Client_Reader implements Runnable {

    protected static final Logger logger = LoggerFactory.getLogger(Client_Reader.class);
    @Getter
    @Setter
    private Client parent;

    Client_Reader(Client clientThis) {
        setParent(clientThis);
    }
    @Getter
    @Setter
    private ObjectInputStream ois;

    private void openReadStream() throws IOException {
        setOis(new ObjectInputStream(getParent().getSocket().getInputStream()));
    }

    @Override
    public void run() {
        Thread.currentThread().setName("(Client_Reader): " + getParent().getHpc().getHost() + ":" + getParent().getHpc().getPort());
        try {
            openReadStream();
            Object object;
            while (getParent().isMarker() == false) {
                try {
                    object = getOis().readObject();
                    if (object instanceof Ping) {
                        logger.info("PING");
                    }
                } catch (SocketTimeoutException e) {
                    getParent().setMarker(true);
                    logger.warn("timeout");
                } catch (ClassNotFoundException e) {

                } catch (IOException e) {
                    getParent().setMarker(true);
                }
            }

        } catch (SocketTimeoutException e) {
            getParent().setMarker(true);
            logger.warn("openReadStream:timeout");
        } catch (EOFException e) {
            getParent().setMarker(true);
            logger.warn("openReadStream:connection break when opening ObjectInputStream");
        } catch (StreamCorruptedException e) {
            logger.warn("incorrect data received");
            getParent().setMarker(true);
        } catch (Exception e) {
            logger.warn("", e);
            getParent().setMarker(true);
        }
        logger.info(Thread.currentThread().getName() + " is stoped");
    }

}
