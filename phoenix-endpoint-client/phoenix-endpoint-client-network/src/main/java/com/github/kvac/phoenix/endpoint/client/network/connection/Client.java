package com.github.kvac.phoenix.endpoint.client.network.connection;

import com.github.kvac.phoenix.endpoint.client.network.NetWorkHeader;
import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.event.PhoenixEvent;
import com.github.kvac.phoenix.event.msg.MessageEvent;
import com.github.kvac.phoenix.libs.objects.Auth;
import com.github.kvac.phoenix.libs.objects.HostPortConnected;
import com.github.kvac.phoenix.libs.objects.Message;
import com.github.kvac.phoenix.libs.objects.Ping;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.github.kvac.phoenix.libs.objects.events.MyEvent.TYPE;
import com.github.kvac.phoenix.libs.objects.events.ra.request.MessageRequest;
import com.github.kvac.phoenix.libs.objects.events.ra.request.RSearchCS;
import com.github.kvac.phoenix.libs.objects.events.ra.request.Request;
import com.google.common.eventbus.Subscribe;
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
import phoenixendpointclient.phoenix.endpoint.client.events.ClientEventHEADER;

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
            //IDENT
            authMe();
            //IDENT
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
        unregister();
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
        EventHEADER.getBus_Ping().register(clientThis);
        EventHEADER.getBus_cs_clear().register(clientThis);
        EventHEADER.getSERVERS_REQUEST_BUS().register(clientThis);
        ClientEventHEADER.getFORMESSAG_EVENT_BUS().register(clientThis);

        ClientEventHEADER.getFORMESSAG_EVENT_BUS().register(clientThis);
        ClientEventHEADER.getREQUEST_REMOTE_EVENT_BUS().register(clientThis);
    }

    private void unregister() {
        EventHEADER.getBus_Ping().unregister(clientThis);
        EventHEADER.getBus_cs_clear().unregister(clientThis);
        EventHEADER.getSERVERS_REQUEST_BUS().unregister(clientThis);

        ClientEventHEADER.getFORMESSAG_EVENT_BUS().unregister(clientThis);
        ClientEventHEADER.getREQUEST_REMOTE_EVENT_BUS().unregister(clientThis);
    }

    public void send(Object object) {
        try {
            getOos().writeObject(object);
            getOos().flush();
        } catch (Exception e) {
            setMarker(true);
        }
    }

    @Subscribe
    public void ping(Ping ping) {
        send(ping);
    }

    @Subscribe
    public void sendOwnCS(MyEvent event) {
        if (event.getType().equals(TYPE.CS_C)
                && event.getObject() instanceof CS) {
            send((CS) event.getObject());
        }
    }

    @Subscribe
    public void sendMessages(MessageEvent messageEvent) {
        PhoenixEvent.TYPE typem = messageEvent.getType();
        if (typem.equals(PhoenixEvent.TYPE.MESSAGE_CLEAR)) {
            if (messageEvent.getObject() instanceof Message) {
                Message message = (Message) messageEvent.getObject();
                send(message);
            }
        }
    }

    @Subscribe
    public void sendRequest(Request request) {
        if (request instanceof MessageRequest) {
            send((MessageRequest) request);
        } else {
            System.out.println(request.getClass());
        }
    }

    @Subscribe
    public void sendRequestSearchCS(MyEvent event) {
        if (event.getType().equals(TYPE.CS_SearchR)
                && event.getObject() instanceof RSearchCS) {
            send((RSearchCS) event.getObject());
        }
    }

    private void authMe() {
        Auth auth = new Auth();
        auth.setWho(NetWorkHeader.getMycs());
        send(auth);
    }
}
