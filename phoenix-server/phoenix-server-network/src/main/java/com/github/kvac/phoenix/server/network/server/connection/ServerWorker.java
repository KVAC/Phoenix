package com.github.kvac.phoenix.server.network.server.connection;

import com.github.kvac.phoenix.libs.objects.Auth;
import com.github.kvac.phoenix.libs.objects.Message;
import com.github.kvac.phoenix.libs.objects.Ping;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.ra.request.MessageRequest;
import com.github.kvac.phoenix.libs.objects.events.ra.request.RSearchCS;
import com.github.kvac.phoenix.libs.objects.events.ra.request.Request;
import com.github.kvac.phoenix.server.db.DataBaseHeader;
import com.github.kvac.phoenix.server.network.NetWorkHeader;
import com.github.kvac.phoenix.server.network.handler.NetWorkHandler;
import com.github.kvac.phoenix.server.network.server.Server;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerWorker implements Runnable {

    protected static final Logger logger = LoggerFactory.getLogger(ServerWorker.class);
    @Getter
    @Setter
    private ServerWorker serverWorkerThis = this;

    @Getter
    @Setter
    private CS clientCS;
    //marker
    @Getter
    @Setter
    private boolean marker = false;
    @Getter
    @Setter
    private boolean didd = false;

    //marker
    @Getter
    @Setter
    private Server server;

    @Getter
    @Setter
    private Socket client;

    @Getter
    @Setter
    private ObjectInputStream ois;
    @Getter
    @Setter
    private ServerWorker_Writer writer;

    public void openReaderStream() throws IOException {
        setOis(new ObjectInputStream(getClient().getInputStream()));
        logger.info("openReaderStream()");
    }

    public ServerWorker(Server server, Socket client) {
        setServer(server);
        setClient(client);
    }

    private void createWriter() throws IOException {
        openReaderStream();
        this.writer = new ServerWorker_Writer(serverWorkerThis);
        writer.start();
    }

    @Override
    public void run() {
        Thread.currentThread().setName("(ServerWorker_READER): " + getClient().getRemoteSocketAddress());
        try {
            createWriter();
            receive();
        } catch (java.lang.ClassCastException e) {
            logger.warn("java.lang.ClassCastException", e);
            setMarker(true);
            try {
                getClient().close();
            } catch (IOException ex) {
            }
        } catch (EOFException e) {
            logger.warn("incorrect data received", e);
            setMarker(true);
        } catch (StreamCorruptedException e) {
            logger.warn("incorrect data received");
            setMarker(true);
        } catch (SocketException | SocketTimeoutException e) {
            setMarker(true);
        } catch (IOException | ClassNotFoundException e) {
            logger.warn("", e);
        }
        endSession();
    }

    private void endSession() {
        setMarker(true);

        try {
            if (NetWorkHeader.getChmConnectedCS().containsKey(clientCS.getID())) {
                NetWorkHeader.getChmConnectedCS().remove(clientCS.getID());
            }
        } catch (Exception e) {
            logger.info("clientCS not removed", e);
        }

        try {
            logger.info(Thread.currentThread().getName() + " closing");
            getClient().close();
            logger.info(Thread.currentThread().getName() + " closed");
        } catch (IOException e) {
            logger.info(Thread.currentThread().getName() + " close", e);
        }
        logger.info(Thread.currentThread().getName() + " is stoped");
    }

    private void receive() throws IOException, ClassNotFoundException {
        Object object;
        while (marker == false) {
            if (getOis() == null) {
                break;
            }
            object = getOis().readObject();
            if (object instanceof Ping) {
            } else if (object instanceof Auth) {
                logger.info("AUTH  started");
                Auth auth = (Auth) object;
                setClientCS(auth.getWho());
                try {
                    if (NetWorkHeader.getChmConnectedCS().containsKey(auth.getWho().getID())) {
                        logger.error("УЖЕ ПОДКЛЮЧЕН");
                        setMarker(true);
                    } else {
                        NetWorkHeader.getChmConnectedCS().put(auth.getWho().getID(), auth.getWho());
                    }
                    logger.info("AUTH  completed*");
                } catch (Exception e) {
                    logger.error("AUTH", e);
                }
            } else if (object instanceof CS) {
                CS cs = (CS) object;
                cs.save();
            } else if (object instanceof Request) {
                Request request = (Request) object;
                if (request instanceof RSearchCS) {
                    RSearchCS rSearchCS = (RSearchCS) request;
                    NetWorkHandler.getAnswerForRequest(rSearchCS);
                } else if (request instanceof MessageRequest) {
                    try {
                        MessageRequest messageRequest = (MessageRequest) request;
                        CS who = messageRequest.getWho();
                        Dao<Message, String> messageDao = DataBaseHeader.getDataBase().getMessageDao();
                        QueryBuilder<Message, String> queryBuilder = messageDao.queryBuilder();
                        List<Message> answ = queryBuilder.where().eq("From_id", who.getID()).or().eq("To_id", who.getID()).query();
                        MessageRequest answer = new MessageRequest();
                        answer.setItAnswere(true);
                        answer.setAnswereData(answ);
                        getWriter().send(answer);
                    } catch (SQLException e) {
                        logger.error("MessageRequest:", e);
                    }
                }
            } else if (object instanceof Message) {
                Message message = (Message) object;
                message.save();
            } else {
                NetWorkHandler.handleOtherObject(object);
            }
        }
    }

}
