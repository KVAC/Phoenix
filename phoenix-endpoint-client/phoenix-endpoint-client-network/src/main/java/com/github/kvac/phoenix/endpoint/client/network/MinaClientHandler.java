package com.github.kvac.phoenix.endpoint.client.network;

import com.github.kvac.phoenix.libs.objects.Auth;
import com.github.kvac.phoenix.libs.objects.events.ra.request.AuthRequest;
import com.github.kvac.phoenix.libs.objects.events.ra.request.Request;
import java.util.Iterator;
import java.util.Set;
import lombok.Getter;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaClientHandler extends IoHandlerAdapter {

    @Getter
    protected final Logger loggerJ = LoggerFactory.getLogger(getClass());

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        getLoggerJ().error("session", cause);
        //super.exceptionCaught(session, cause); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        session.closeOnFlush();
        //     super.sessionIdle(session, status); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        loggerJ.info("messageReceived");
        if (message instanceof Request) {
            Request request = (Request) message;
            if (request instanceof AuthRequest) {
                AuthRequest authRequest = (AuthRequest) request;
                Auth auth = new Auth();
                auth.setWho(NetWorkHeader.getMycs());
                session.write(auth);
            }
        } else {
            System.out.println(message.toString());
        }

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        //    super.sessionClosed(session); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        String attr = "Values: ";
        session.setAttribute(attr);
        System.out.println(session.getAttribute(attr));
    }

    @Override
    public void event(IoSession session, FilterEvent event) throws Exception {
        super.event(session, event); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        super.inputClosed(session); //To change body of generated methods, choose Tools | Templates.
    }

    public MinaClientHandler() {
    }

}
