package com.github.kvac.phoenix.server.init;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.network.Ping;
import com.github.kvac.phoenix.server.db.DataBaseHeader;
import com.github.kvac.phoenix.server.network.header.NetWorkHeader;

import java.io.IOException;
import java.sql.SQLException;
import org.slf4j.LoggerFactory;

/**
 * @author jdcs_dev
 *
 */
public class ServerInit {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(ServerInit.class);

    /**
     * @param args
     *
     */
    public static void main(String[] args) {
        try {
            DataBaseHeader.getDataBase().init();

            DataBaseHeader.getDataBase().connect();

            DataBaseHeader.getDataBase().create();
            DataBaseHeader.getDataBaseHandler().start();

            NetWorkHeader.getMcssh().start();//MinaCSSessionHandler
            NetWorkHeader.getServer().start();

            new Thread(() -> {
                Ping pong = new Ping();
                do {
                    EventHEADER.getBus_Pong().post(pong);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        logger.warn("", ex);
                    }
                } while (true);
            }, "SERVER_PONG").start();
        } catch (SQLException | IOException ex) {
            logger.warn("", ex);
        }
    }
}
