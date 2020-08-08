package com.github.kvac.phoenix.endpoint.client.db;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.event.PhoenixEvent;
import com.github.kvac.phoenix.event.msg.MessageEvent;
import com.github.kvac.phoenix.libs.objects.Message;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.google.common.eventbus.Subscribe;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import phoenixendpointclient.phoenix.endpoint.client.events.ClientEventHEADER;

public class DataBaseHandler extends Thread implements Runnable {

    DataBaseHandler thisHandler = this;
    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(DataBaseHandler.class);

    public DataBaseHandler() {
        EventHEADER.getMESSAGES_EVENT_BUS().register(thisHandler);
        logger.info(this.getClass().getName() + " is registered");
    }

    @Override
    public void run() {
        new Thread(() -> {
            do {
                try {
                    DataBaseHeader.getDataBase().restoreInfo();
                } catch (SQLException ex) {
                    Logger.getLogger(DataBaseHandler.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    Thread.sleep(600);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DataBaseHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while (true);
        }, "Settings get").start();
        new Thread(() -> {
            do {
                try {
                    DataBaseHeader.getDataBase().restoreServers();
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DataBaseHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while (true);
        }, "Servers getter").start();
        new Thread(() -> {
            do {
                try {
                    List<CS> list = DataBaseHeader.getDataBase().getCsDao().queryForAll();
                    for (CS cs : list) {
                        MyEvent event = new MyEvent();
                        event.setType(MyEvent.TYPE.CS_SHOW);
                        event.setObject(cs);
                        EventHEADER.getBus_cs_show().post(event);
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException | SQLException ex) {
                    logger.error("", ex);
                }
            } while (true);
        }, "CS getter").start();

        new Thread(() -> {
            do {
                try {
                    List<Message> messagesList = DataBaseHeader.getDataBase().getMessageDao().queryForAll();
                    messagesList.stream().map((message) -> {
                        MessageEvent event = new MessageEvent();
                        event.setType(PhoenixEvent.TYPE.MESSAGE_CLEAR);
                        event.setObject(message);
                        return event;
                    }).forEachOrdered((event) -> {
                        ClientEventHEADER.getFORMESSAG_EVENT_BUS().post(event);
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException | SQLException ex) {
                    logger.error("", ex);
                }
            } while (true);
        }, "MESSAGE getter").start();
    }

    @Subscribe
    public static void messageHandler(MessageEvent messageEvent) throws SQLException {
        Message message = (Message) messageEvent.getObject();
        if (messageEvent.getType().equals(PhoenixEvent.TYPE.MESSAGE_SAVE)) {
            DataBaseHeader.getDataBase().messageSave(message);
        }
    }
}
