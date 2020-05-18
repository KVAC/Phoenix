package com.github.kvac.phoenix.endpoint.client.network.connection;

import com.github.kvac.phoenix.endpoint.client.network.NetWorkHeader;
import com.github.kvac.phoenix.libs.objects.HostPortConnected;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client implements Runnable {

    @Getter
    @Setter
    private Client clientThis = this;

    protected static final Logger logger = LoggerFactory.getLogger(Client.class);
    //marker
    @Getter
    @Setter
    private boolean marker = false;
    //marker

    //DATA
    @Getter
    @Setter
    private Socket socket;
    @Getter
    @Setter
    private HostPortConnected hpc;
    //DATA

    //STREAMS
    @Getter
    @Setter
    private ObjectOutputStream oos;

    public void openWriterStream() throws IOException {
        setOos(new ObjectOutputStream(getSocket().getOutputStream()));
        logger.info(Thread.currentThread().getName() + " openWriterStream() is opened");
    }

    //STREAMS
    @Override
    public void run() {
        Thread.currentThread().setName("(Writer): " + getHpc().getHost() + ":" + getHpc().getPort());
        try {
            openWriterStream();
            register();
            new Thread(new Client_Reader(clientThis)).start();
            while (marker == false) {
                Thread.sleep(300);
            }
        } catch (SocketTimeoutException e) {
            setMarker(true);
        } catch (Exception e) {
            logger.warn("", e);
        }

        NetWorkHeader.getHostPortConnectedList().remove(getHpc());
        logger.info(Thread.currentThread().getName() + " Hpc removed");

        try {
            logger.info(Thread.currentThread().getName() + " closing");
            socket.close();
            logger.info(Thread.currentThread().getName() + " closed");
        } catch (IOException e) {
            logger.info(Thread.currentThread().getName() + " close", e);
        }
        logger.info(Thread.currentThread().getName() + " is stoped");

    }

    public void connect() throws SocketException, IOException {
        Socket so = new Socket();
        so.connect(new InetSocketAddress(getHpc().getHost(), getHpc().getPort()));
        so.setSoTimeout(10000);
        setSocket(so);
    }

    private void register() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
