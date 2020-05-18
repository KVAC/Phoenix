package com.github.kvac.phoenix.endpoint.client.db;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

public class DataBaseHandler extends Thread implements Runnable {

    protected static final org.slf4j.Logger logger = LoggerFactory.getLogger(DataBaseHandler.class);

    public DataBaseHandler() {
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
    }
}
