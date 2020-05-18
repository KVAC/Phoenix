package com.github.kvac.phoenix.server.init;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.network.HEADER_NETWORK;
import com.github.kvac.phoenix.libs.objects.Ping;
import com.github.kvac.phoenix.server.db.DataBaseHeader;
import com.github.kvac.phoenix.server.network.NetWorkHeader;
import com.github.kvac.phoenix.server.network.server.connection.ServerWorker;
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

            NetWorkHeader.getServer().start();

            new Thread(() -> {
                Thread.currentThread().setName("SERVER_PONG");
                Ping pong = new Ping();
                do {
                    EventHEADER.getBus_Pong().post(pong);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        logger.warn("", ex);
                    }
                } while (true);
            }).start();
        } catch (SQLException | IOException ex) {
            logger.warn("", ex);
        }
    }
}
