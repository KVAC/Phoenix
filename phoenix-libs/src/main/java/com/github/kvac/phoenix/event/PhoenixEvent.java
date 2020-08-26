package com.github.kvac.phoenix.event;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import com.github.kvac.phoenix.libs.objects.PhoenixObject;

/**
 *
 * @author jdcs_dev
 */
public class PhoenixEvent implements Serializable {

    private static final long serialVersionUID = 240513231950251807L;

    public enum TYPE {
        MESSAGE_SHOW, MESSAGE_SAVE, MESSAGE_CLEAR
    }
    @Getter
    @Setter
    private TYPE type;

    @Getter
    @Setter
    private PhoenixObject object;

}
