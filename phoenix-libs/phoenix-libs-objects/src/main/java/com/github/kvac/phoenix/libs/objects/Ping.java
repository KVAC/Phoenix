package com.github.kvac.phoenix.libs.objects;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class Ping implements Serializable {

    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private String message;

    public Ping() {
    }

    public Ping(String msg) {
        setMessage(msg);
    }
}
