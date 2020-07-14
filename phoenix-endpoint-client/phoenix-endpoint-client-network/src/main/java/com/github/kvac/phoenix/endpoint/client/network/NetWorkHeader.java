package com.github.kvac.phoenix.endpoint.client.network;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.HostPortConnected;
import com.github.kvac.phoenix.libs.objects.MySettings;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.google.common.eventbus.Subscribe;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import lombok.Setter;

public class NetWorkHeader {
    
    @Getter
    @Setter
    private static CopyOnWriteArrayList<HostPortConnected> HostPortConnectedList = new CopyOnWriteArrayList<HostPortConnected>();
    
    @Getter
    private static CS mycs = new CS();
    
    {
        EventHEADER.getBus_mysettings().register(this);
        mycs.setItsMe(true);
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
                mycs.setID(settings.getMyID());
            }
            if (settings.getName() != null) {
                if (settings.getNameTime() > mycs.getNameTime()) {
                    mycs.setName(settings.getName());
                    mycs.setNameTime(settings.getNameTime());
                }
            }
            if (settings.getAddress() != null) {
                if (settings.getAddressTime() > mycs.getAddress_time()) {
                    mycs.setAddress(settings.getAddress());
                    mycs.setAddress_time(settings.getAddressTime());
                }
            }
            if (!settings.isShareAddress()) {
                mycs.setAddress(null);
            }
            mycs.setPort(settings.getPort());
            //
        }
        
    }
}
