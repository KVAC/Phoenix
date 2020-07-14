/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.kvac.phoenix.libs.objects.events.ra.request;

import com.github.kvac.phoenix.libs.objects.cs.CS;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jdcs_dev
 */
public class Request implements Serializable {

    @Getter
    @Setter
    private CS who;

    public static enum Type {

    }

    @Getter
    @Setter
    private Object requestData;

    //
    //#######################################
    @Getter
    @Setter
    private boolean itAnswere = false;
    //#######################################
    //

    @Getter
    @Setter
    private Object answereData;
    //#######################################
}
