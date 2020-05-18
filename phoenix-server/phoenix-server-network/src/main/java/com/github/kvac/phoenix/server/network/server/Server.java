package com.github.kvac.phoenix.server.network.server;

import com.github.kvac.phoenix.server.network.server.connection.ServerWorker;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.github.kvac.phoenix.libs.network.HEADER_NETWORK;

import lombok.Getter;
import lombok.Setter;

public class Server extends Thread implements Runnable {

    @Getter
    @Setter
    private boolean stop = false;

    public Server() {

    }

    @Override
    public void run() {
        Thread.currentThread().setName("SERVER");
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server started on " + serverSocket.getLocalPort());
            while (stop == false) {
                try {
                    Socket client = serverSocket.accept();
                    client.setSoTimeout(HEADER_NETWORK.getTimeout());
                    ServerWorker clientWorker = new ServerWorker(this, client);
                    new Thread(clientWorker).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println(Thread.currentThread() + " is closed");
    }

    public static int getAVSERVERPORT() {
        int PortMin = 2000;
        int PortMax = 2003;
        int port = PortMin;

        while (simplePortScan("127.0.0.1", port) && port <= PortMax) {
            port = port + 1;
        }
        return port;
    }

    public static boolean simplePortScan(String Host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(Host, port));
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
