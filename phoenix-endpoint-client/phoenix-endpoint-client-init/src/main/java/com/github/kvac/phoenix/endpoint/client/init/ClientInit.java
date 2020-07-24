package com.github.kvac.phoenix.endpoint.client.init;

import com.github.kvac.phoenix.endpoint.client.db.DataBaseHeader;
import com.github.kvac.phoenix.endpoint.client.gui.ClientGui;
import com.github.kvac.phoenix.endpoint.client.gui.First_Settings;
import com.github.kvac.phoenix.endpoint.client.network.NetWorkHeader;
import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.Ping;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.github.kvac.phoenix.libs.objects.events.MyEvent.TYPE;
import com.github.kvac.phoenix.libs.objects.events.ra.request.MessageRequest;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.logger.Log.Level;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phoenixendpointclient.phoenix.endpoint.client.events.ClientEventHEADER;

public class ClientInit {

    protected static final Logger logger = LoggerFactory.getLogger(ClientInit.class);

    public static void main(String[] args) throws SQLException {
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, Level.INFO.toString());

        NetWorkHeader netWorkHeader = new NetWorkHeader();

        DataBaseHeader.getDataBase().connect();// YED 28.03.2020
        DataBaseHeader.getDataBase().createDB();// YED 28.03.2020

        // первоначальная настройка
        // Мои настройки
        if (DataBaseHeader.getDataBase().getSettingsDao().countOf() == 0) {
            logger.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaawwwwwwwwwwwwwwwwwwww");
            First_Settings first_Settings = new First_Settings();
            first_Settings.setVisible(true);

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                logger.error(e.toString());
            }
            first_Settings.getTextArea1().setText(first_Settings.getTextArea1().getText().replaceAll(" ", ""));
            while (!InitUtils.checkSettingsAv()) {
                //
                //
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    logger.error("IN while (!InitUtils.checkSettingsAv())", e);
                }
            }
            first_Settings.setVisible(false);

        }
        // Мои настройки
        // Начальные сервера
        // Начальные сервера
        // первоначальная настройка

        // DataBaseHeader.getDataBase().createDefault();// YED 28.03.2020
        DataBaseHeader.getDataBase().restoreInfo();// YED 28.03.2020

        DataBaseHeader.getDataBaseHandler().start();

        NetWorkHeader.getNetWorkD().start();
        NetWorkHeader.getClientHandler().start();

        ClientGui clientGui = new ClientGui();
        clientGui.setVisible(true);

        new Thread(() -> {
            Thread.currentThread().setName("My cs sender Thread");
            do {
                CS cs = NetWorkHeader.getMycs();
                MyEvent event = new MyEvent();
                event.setObject(cs);
                event.setType(TYPE.CS_C);
                EventHEADER.getBus_cs_clear().post(event);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    logger.error(Thread.currentThread().getName(), e);
                }
            } while (true);
        }).start();

        new Thread(() -> {
            Thread.currentThread().setName("Client Ping");
            Ping ping = new Ping();
            do {
                EventHEADER.getBus_Ping().post(ping);
                //       logger.info("count:" + NetWorkHeader.getHostPortConnectedList().size());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            } while (true);
        }).start();

        new Thread(() -> {
            Thread.currentThread().setName("INFO");
            do {

                //  System.out.println(INFO.getExternalIP());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    logger.error(Thread.currentThread().getName(), e);
                }
            } while (true);
        }).start();
        new Thread(() -> {
            do {
                MessageRequest request = new MessageRequest();
                request.setWho(NetWorkHeader.getMycs());
                ClientEventHEADER.getREQUEST_REMOTE_EVENT_BUS().post(request);
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ex) {
                    logger.error("", ex);
                }
            } while (true);
        }, "MessageGetterNet").start();
    }

    public static void getExternalIpAdress() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in;
        try (InputStream stream = whatismyip.openStream()) {
            InputStreamReader isr = new InputStreamReader(stream, StandardCharsets.UTF_8);
            in = new BufferedReader(isr);
            in.close();
            isr.close();
        }
        String ip = in.readLine();
        System.out.println(ip);
    }
}
