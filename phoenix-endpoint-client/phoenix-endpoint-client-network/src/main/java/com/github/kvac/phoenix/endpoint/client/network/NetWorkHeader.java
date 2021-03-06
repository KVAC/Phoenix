package com.github.kvac.phoenix.endpoint.client.network;

import com.github.kvac.phoenix.event.eventheader.EventHEADER;
import com.github.kvac.phoenix.libs.network.Ping;
import com.github.kvac.phoenix.libs.objects.MySettings;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import lombok.Setter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.github.kvac.phoenix.libs.network.HostPortConnected;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class NetWorkHeader {

    @Getter
    static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static final Ping PING = new Ping("ping");

    @Getter
    @Setter
    private static NioSocketConnector connector = new NioSocketConnector();

    @Getter
    @Setter
    private static CopyOnWriteArrayList<HostPortConnected> HostPortConnectedList = new CopyOnWriteArrayList<>();

    @Getter
    private static CS mycs = new CS() {
        {
            this.setItsMe(true);
        }
    };

    {
        EventHEADER.getBus_mysettings().register(this);
    }

    @Getter
    private static ClientHandler clientHandler = new ClientHandler();
    @Getter
    private static NetWorkD netWorkD = new NetWorkD();

    @Subscribe
    private void update(MyEvent event) {
        if (MyEvent.isSettingsUpdate(event)) {
            MySettings settings = (MySettings) event.getObject();

            if (settings.getMyID() != null) {
                mycs.setId(settings.getMyID());
            }
            if (settings.getName() != null) {
                if (settings.getNameTime() > mycs.getNameTime()) {
                    mycs.setName(settings.getName());
                    mycs.setNameTime(settings.getNameTime());
                }
            }
            if (settings.getAddress() != null) {
                if (settings.getAddressTime() > mycs.getAddressTime()) {
                    mycs.setAddress(settings.getAddress());
                    mycs.setAddressTime(settings.getAddressTime());
                }
            }
            if (!settings.isShareAddress()) {
                mycs.setAddress(null);
            }
            mycs.setPort(settings.getPort());
        }

    }
}
