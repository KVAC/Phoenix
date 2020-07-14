package com.github.kvac.phoenix.endpoint.client.network;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;

public class NetWorkD extends Thread implements Runnable {
    
    public NetWorkD() {
        EventHEADER.getBus_cs_clear().register(this);
    }
    
    @Override
    public void run() {
        // PING
        new Thread(() -> {
            Thread.currentThread().setName("NetWorkD.PING");
            do {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }).start();
        // PING

    }
}
