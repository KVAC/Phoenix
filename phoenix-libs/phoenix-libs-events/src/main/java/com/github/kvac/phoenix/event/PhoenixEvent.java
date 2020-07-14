/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.kvac.phoenix.event;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jdcs_dev
 */
public class PhoenixEvent implements Serializable {

    public static enum TYPE {
        MESSAGE_SHOW, MESSAGE_SAVE, MESSAGE_CLEAR
    }
    @Getter
    @Setter
    private TYPE type;

    @Getter
    @Setter
    private Object object;

}
