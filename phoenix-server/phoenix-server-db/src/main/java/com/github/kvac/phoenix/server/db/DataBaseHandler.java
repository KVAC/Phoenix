package com.github.kvac.phoenix.server.db;

import com.github.kvac.phoenix.event.eventheader.EventHEADER;
import com.github.kvac.phoenix.event.PhoenixEvent;
import com.github.kvac.phoenix.event.msg.MessageEvent;
import com.github.kvac.phoenix.libs.objects.Message;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.google.common.eventbus.Subscribe;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author jdcs_dev
 */
public class DataBaseHandler extends Thread implements Runnable {

    DataBaseHandler thisHandler = this;

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(DataBaseHandler.class);
    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public DataBaseHandler() {
        registerEvent();
    }

    private void registerEvent() {
        EventHEADER.getBus_cs_save().register(thisHandler);
        EventHEADER.getMESSAGES_EVENT_BUS().register(thisHandler);
        logger.info("com.github.kvac.phoenix.server.db.DataBaseHandler.registerEvent()");
    }

    @Subscribe
    public void myEventHandler(MyEvent event) throws SQLException {
        MyEvent.TYPE type = event.getType();
        Object object = event.getObject();
        if ((type.equals(MyEvent.TYPE.DATABASE_CS_SAVE))
                && object instanceof CS) {
            DataBaseHeader.getDataBase().saveCS((CS) object);
        }
    }

    @Subscribe
    public void messageEventHandler(MessageEvent event) throws SQLException {
        PhoenixEvent.TYPE type = event.getType();
        Object object = event.getObject();
        if (type.equals(PhoenixEvent.TYPE.MESSAGE_SAVE) && object instanceof Message) {
            Message message = (Message) object;
            DataBaseHeader.getDataBase().saveMessage(message);
        }
    }

    @Override
    public void run() {
        //executorService.scheduleAtFixedRate(DataBaseHandler::myTask, 0, 2, TimeUnit.SECONDS);
    }

    private static void myTask() {

    }
}
