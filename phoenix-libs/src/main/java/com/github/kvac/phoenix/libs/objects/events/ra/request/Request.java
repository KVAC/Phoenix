/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.kvac.phoenix.libs.objects.events.ra.request;

import com.github.kvac.phoenix.libs.objects.PhoenixObject;
import com.github.kvac.phoenix.libs.objects.SerializableArrayList;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jdcs_dev
 */
public class Request extends PhoenixObject implements Serializable {

    private static final long serialVersionUID = 240513231950251807L;

    @Getter
    @Setter
    private CS who;

    public static enum Type {

    }

    @Getter
    @Setter
    private SerializableArrayList requestData = new SerializableArrayList();

    //
    //#######################################
    @Getter
    @Setter
    private boolean itAnswere = false;
    //#######################################
    //

    @Getter
    @Setter
    private SerializableArrayList answereData = new SerializableArrayList();
    //#######################################
}
