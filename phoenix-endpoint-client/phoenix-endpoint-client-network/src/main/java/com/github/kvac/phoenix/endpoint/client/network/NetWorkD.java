package com.github.kvac.phoenix.endpoint.client.network;

public class NetWorkD extends Thread implements Runnable {

    @Override
    public void run() {
        // PING
        new Thread(() -> {
            Thread.currentThread().setName("NetWorkD.PING");
            do {
                NetWorkHeader.getConnector().broadcast(NetWorkHeader.PING);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }, "broadcast:ping").start();
        // PING
    }
}
