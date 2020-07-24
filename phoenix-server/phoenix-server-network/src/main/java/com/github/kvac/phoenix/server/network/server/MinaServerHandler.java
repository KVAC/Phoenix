package com.github.kvac.phoenix.server.network.server;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.Auth;
import com.github.kvac.phoenix.libs.objects.Message;
import com.github.kvac.phoenix.libs.objects.Ping;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.ra.request.MessageRequest;
import com.github.kvac.phoenix.libs.objects.events.ra.request.RSearchCS;
import com.github.kvac.phoenix.libs.objects.events.ra.request.Request;
import com.github.kvac.phoenix.server.db.DataBaseHeader;
import com.github.kvac.phoenix.server.network.header.NetWorkHeader;
import com.github.kvac.phoenix.server.network.handler.NetWorkHandler;
import com.google.common.eventbus.Subscribe;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import java.sql.SQLException;
import java.util.List;
import lombok.Getter;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jdcs_dev
 */
public class MinaServerHandler extends IoHandlerAdapter {

    static final String cs_attr = "CS:";

    //FOR SESSION
    private void register() {
        EventHEADER.getBus_Pong().register(this);
        EventHEADER.getBus_cs_clear().register(this);
        EventHEADER.getSERVERS_ANSWER_BUS().register(this);
    }

    //FOR SESSION
    private void unregister() {
        EventHEADER.getSERVERS_ANSWER_BUS().unregister(this);
        EventHEADER.getBus_Pong().unregister(this);
        EventHEADER.getBus_cs_clear().unregister(this);

    }

    //   @Subscribe
    //   public void ping(Ping ping) {
    //      send(ping);
    //  }
    //  @Subscribe
    //   public void answer(RSearchCS request) {
    //   if (getParent().getClientCS() != null) {
    //          String idT = getParent().getClientCS().getID();
    //      String idR = request.getWho().getID();
    //      if (idR.equals(idT)) {
    //           send(request);
    //        }
    //    }
    // }
    @Getter
    protected final Logger loggerJ = LoggerFactory.getLogger(getClass());

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message instanceof Auth) {
            Auth auth = (Auth) message;
            // setClientCS(auth.getWho());
            try {
                CS cs = auth.getWho();
                Object cur_attr = session.getAttribute(cs_attr);
                if (cur_attr == null) {
                    session.setAttribute(cs_attr, cs);
                }
                getLoggerJ().info("AUTH  completed*");
            } catch (Exception e) {
                getLoggerJ().error("AUTH", e);
            }
            //TODO
        } else if (message instanceof Ping) {
            Ping ping = (Ping) message;
            System.out.println("message:" + ping.getMessage());
        } else if (message instanceof CS) {
            CS cs = (CS) message;
            cs.save();
        } else if (message instanceof Message) {
            Message msg = (Message) message;
            msg.save();
        } else if (message instanceof Request) {
            Request request = (Request) message;
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
                    session.write(answer);
                } catch (SQLException e) {
                    getLoggerJ().error("MessageRequest:", e);
                }
            }
        } else {
            System.out.println("message:" + message.getClass() + ":" + message.toString());
            NetWorkHandler.handleOtherObject(message);
        }
    }

    @Override
    public void event(IoSession session, FilterEvent event) throws Exception {
        super.event(session, event);
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        super.inputClosed(session);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        session.closeNow();
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {

    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        getLoggerJ().info("2");
        CS cs = null;

        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

        if (!session.containsAttribute(cs_attr)) {
            session.setAttribute(cs_attr, cs);
        }
        System.out.println(session.getAttribute(cs_attr));
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        getLoggerJ().info("1");
    }

}
