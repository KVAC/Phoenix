/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.kvac.phoenix.server.network.handler;

import com.github.kvac.phoenix.libs.objects.HostPortConnected;
import java.util.Map;
import org.apache.mina.core.session.IoSession;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jdcs_dev
 */
public class MinaCSSessionHandler extends Thread implements Runnable {

    @Getter
    protected final org.slf4j.Logger loggerJ = LoggerFactory.getLogger(getClass());

    public static final ConcurrentHashMap<IoSession, HostPortConnected> SESSION_FOR_CS = new ConcurrentHashMap<>();

    public MinaCSSessionHandler() {

    }

    @Override
    public void run() {
        do {
            for (Map.Entry<IoSession, HostPortConnected> entry : SESSION_FOR_CS.entrySet()) {
                IoSession session = entry.getKey();
                HostPortConnected hpc = entry.getValue();
                if (!session.isConnected()) {
                    getLoggerJ().info(hpc.getHost() + ":" + hpc.getPort());
                }
            }

            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                getLoggerJ().error("sleep", ex);
            }
        } while (true);
    }

}
