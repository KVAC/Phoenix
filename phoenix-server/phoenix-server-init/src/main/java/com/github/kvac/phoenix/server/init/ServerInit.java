package com.github.kvac.phoenix.server.init;

import com.github.kvac.phoenix.libs.network.Ping;
import com.github.kvac.phoenix.server.db.DataBaseHeader;
import com.github.kvac.phoenix.server.network.header.NetWorkHeader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.LoggerFactory;

/**
 * @author jdcs_dev
 *
 */
public class ServerInit {

    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        ServerInit serverInit = new ServerInit();
        serverInit.init();
    }

    private void init() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("EXIT");
            }));

            DataBaseHeader.getDataBase().init();
            DataBaseHeader.getDataBase().connect();
            DataBaseHeader.getDataBase().create();
            DataBaseHeader.getDataBaseHandler().start();

            NetWorkHeader.getServer().start();
            executorService.scheduleAtFixedRate(pingToClients(), 0, 2, TimeUnit.SECONDS);
        } catch (SQLException | IOException ex) {
            logger.warn("", ex);
        }
    }

    private Runnable pingToClients() {
        return () -> {
            NetWorkHeader.getAcceptor().broadcast(new Ping("Pong"));
        };
    }
}
