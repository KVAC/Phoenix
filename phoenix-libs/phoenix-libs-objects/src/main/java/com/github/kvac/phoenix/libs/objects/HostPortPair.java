/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.kvac.phoenix.libs.objects;

import lombok.Getter;
import lombok.Setter;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author jdcs_dev
 */
public class HostPortPair {

    @Getter
    @Setter
    private String host;
    @Getter
    @Setter
    private int port;

}
