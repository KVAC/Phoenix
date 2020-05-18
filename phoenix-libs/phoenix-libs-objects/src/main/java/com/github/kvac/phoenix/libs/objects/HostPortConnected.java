package com.github.kvac.phoenix.libs.objects;

import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Getter;
import lombok.Setter;

public class HostPortConnected {

    
    @Getter
    @Setter
    private String host;
    @Getter
    @Setter
    private int port;
    @Getter
    @Setter
    private boolean connected;

    @Override
    public String toString() {
        return "HostPortConnected{" + "host=" + host + ", port=" + port + ", connected=" + connected + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HostPortConnected) {
            HostPortConnected hpc = (HostPortConnected) obj;
            if (this.getHost().equals(hpc.getHost())) {
                if (this.getPort() == hpc.getPort()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean avInList(CopyOnWriteArrayList<HostPortConnected> hostPortConnectedList,
            HostPortConnected hpc) {
        return hostPortConnectedList.stream().anyMatch((hostPortConnected) -> {
            return hostPortConnected.equals(hpc);
        });
    }
}
