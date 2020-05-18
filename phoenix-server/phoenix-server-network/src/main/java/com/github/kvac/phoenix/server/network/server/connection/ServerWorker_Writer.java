/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.kvac.phoenix.server.network.server.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jdcs_dev
 */
public class ServerWorker_Writer implements Runnable {

    
    protected static final Logger logger = LoggerFactory.getLogger(ServerWorker_Writer.class);
    @Getter
    @Setter
    private ServerWorker parent;
    @Getter
    @Setter
    private ObjectOutputStream oos;

    public void openWriteStream() throws IOException {
        setOos(new ObjectOutputStream(getParent().getClient().getOutputStream()));
        logger.info("openWriteStream()");
    }

    ServerWorker_Writer(ServerWorker serverWorkerThis) {
        setParent(serverWorkerThis);
    }

    @Override
    public void run() {
        Thread.currentThread().setName("(ServerWorker_Writer): " + getParent().getClient().getRemoteSocketAddress());
        try {
            openWriteStream();
            while (getParent().isMarker() == false) {
                Thread.sleep(300);
                System.out.println("com.github.kvac.phoenix.server.network.server.connection.ServerWorker_Writer.run()");
            }
        } catch (Exception e) {
        }
        logger.info(Thread.currentThread().getName() + " is stoped");
    }
}