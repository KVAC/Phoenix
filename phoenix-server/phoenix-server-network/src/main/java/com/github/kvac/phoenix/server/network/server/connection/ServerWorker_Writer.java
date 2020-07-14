/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.kvac.phoenix.server.network.server.connection;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.Ping;
import com.github.kvac.phoenix.libs.objects.events.ra.request.RSearchCS;
import com.google.common.eventbus.Subscribe;
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
public class ServerWorker_Writer extends Thread implements Runnable {

    protected static final Logger logger = LoggerFactory.getLogger(ServerWorker_Writer.class);

    @Getter
    @Setter
    private ServerWorker_Writer workerThis = this;
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
            register();
            while (getParent().isMarker() == false) {
                Thread.sleep(300);
            }
        } catch (IOException | InterruptedException e) {
            //ВОЗМОЖНО ТУТ
            getParent().setMarker(true);
        }
        unregister();

        logger.info(Thread.currentThread().getName() + " is stoped");
    }

    private void register() {
        EventHEADER.getBus_Pong().register(workerThis);
        EventHEADER.getBus_cs_clear().register(workerThis);
        EventHEADER.getSERVERS_ANSWER_BUS().register(workerThis);
    }

    private void unregister() {
        EventHEADER.getSERVERS_ANSWER_BUS().unregister(workerThis);
        EventHEADER.getBus_Pong().unregister(workerThis);
        EventHEADER.getBus_cs_clear().unregister(workerThis);

    }

    public void send(Object object) {
        try {
            getOos().writeObject(object);
            getOos().flush();
        } catch (IOException e) {
            getParent().setMarker(true);
        }
    }

    @Subscribe
    public void ping(Ping ping) {
        send(ping);
    }

    @Subscribe
    public void answer(RSearchCS request) {
        if (getParent().getClientCS() != null) {
            String idT = getParent().getClientCS().getID();
            String idR = request.getWho().getID();
            if (idR.equals(idT)) {
                send(request);
            }
        }
    }

    @Subscribe
    public void aaa(Object object) {
        if (!(object instanceof Ping)) {
            System.out.println(object.getClass());
        }
    }

}
