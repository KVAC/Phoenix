package com.github.kvac.phoenix.endpoint.client.network.connection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.github.kvac.phoenix.libs.objects.Message;
import com.github.kvac.phoenix.libs.objects.Ping;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.ra.request.MessageRequest;
import com.github.kvac.phoenix.libs.objects.events.ra.request.RSearchCS;
import com.github.kvac.phoenix.libs.objects.events.ra.request.Request;
import phoenixendpointclient.phoenix.endpoint.client.events.ClientEventHEADER;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
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
                        //  logger.info("PING");
                    } else if (object instanceof Request) {
                        Request request = (Request) object;
                        if (request instanceof RSearchCS) {
                            RSearchCS rscs = (RSearchCS) request;
                            if (rscs.isItAnswere()) {
                                Object data = rscs.getAnswereData();
                                if (data instanceof ArrayList) {
                                    ArrayList dataList = (ArrayList) data;
                                    if (dataList.size() > 0) {
                                        Class<? extends Object> dataObjType = dataList.get(0).getClass();
                                        if (dataObjType.equals(CS.class)) {
                                            ArrayList<CS> dataListC = dataList;
                                            ClientEventHEADER.getFORSEARCHANSWER_EVENT_BUS().post(dataListC);
                                        }
                                    }
                                }
                            }
                        } else if (request instanceof MessageRequest) {
                            MessageRequest messageRequest = (MessageRequest) request;
                            Object answ = messageRequest.getAnswereData();
                            if (answ instanceof List) {
                                List list = (List) answ;
                                if (list.size() > 0) {
                                    if (list.get(0) instanceof Message) {
                                        List<Message> messages = list;
                                        messages.forEach(message -> {
                                            message.save();
                                        });
                                    }
                                }

                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    logger.warn("class not found", e);
                } catch (SocketTimeoutException e) {
                    getParent().setMarker(true);
                    logger.warn("timeout");
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
        } catch (IOException e) {
            logger.warn("", e);
            getParent().setMarker(true);
        }
        logger.info(Thread.currentThread().getName() + " is stoped");
    }

}
