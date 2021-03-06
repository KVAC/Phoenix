package com.github.kvac.phoenix.libs.objects.cs;

import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@DatabaseTable(tableName = "Servers")
public class S {

    @Getter
    @Setter
    String fingerprint;

    @Getter
    @Setter
    Object pgpPublicKey;
    @Getter
    @Setter
    Object sslCert;

    public static boolean validServer(String server) {
        try {
            String host = StringUtils.substringBefore(server, ":");
            int port = Integer.parseInt(StringUtils.substringAfter(server, ":"));
            if (port >= 0 && port <= 65535) {
                host.getClass();//TODO
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static S parseString(String string) {
        String id;//TODO

        String host = StringUtils.substringBefore(string, ":");
        int port = Integer.parseInt(StringUtils.substringAfter(string, ":"));

        S server = new S();
        server.setHost(host);
        server.setPort(port);

        return server;
    }
    @Getter
    @Setter
    @DatabaseField(id = true)
    private String id;
    @Getter
    @Setter
    @DatabaseField
    private String name;

    @Getter
    @Setter
    @DatabaseField
    private String host;
    @Getter
    @Setter
    @DatabaseField
    private int port;

    @Override
    public String toString() {
        return "S [id=" + id + ", name=" + name + ", host=" + host + ", port=" + port + "]";
    }

    public static boolean containsInLIST(List<S> seList, S server) {
        return seList.stream().filter(s -> (s.getHost().toLowerCase().equals(server.getHost().toLowerCase()))).anyMatch(s -> (s.getPort() == server.getPort()));
    }
}
