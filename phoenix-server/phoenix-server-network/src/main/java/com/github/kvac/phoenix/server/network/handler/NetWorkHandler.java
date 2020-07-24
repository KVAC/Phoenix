package com.github.kvac.phoenix.server.network.handler;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.ra.request.RSearchCS;
import com.github.kvac.phoenix.libs.objects.events.ra.request.Request;
import com.github.kvac.phoenix.server.db.DataBaseHeader;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.Where;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetWorkHandler extends Thread implements Runnable {

    protected static final Logger logger = LoggerFactory.getLogger(NetWorkHandler.class);

    public static void handleOtherObject(Object object) {

    }

    public NetWorkHandler() {
    }

    public static void getAnswerForRequest(Request request) {
        new Thread(() -> {
            if (request instanceof RSearchCS) {
                getAnswerForSearchCS((RSearchCS) request);
            }
        }, "GetAnswerForRequest").start();

    }

    private static void getAnswerForSearchCS(RSearchCS rSearchCS) {
        Thread.currentThread().setName(Thread.currentThread().getName() + ":GetAnswerForSearchCS");
        Dao<CS, String> csdao = DataBaseHeader.getDataBase().getCsDao();
        try {
            Where<CS, String> q = csdao.queryBuilder().where().like("name", "%" + rSearchCS.getRequestData() + "%");
            PreparedQuery<CS> aw = q.prepare();
            List<CS> list = csdao.query(aw);

            RSearchCS answer = new RSearchCS();

            answer.setWho(rSearchCS.getWho());

            answer.setItAnswere(true);
            answer.setAnswereData(list);

            EventHEADER.getSERVERS_ANSWER_BUS().post(answer);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}
