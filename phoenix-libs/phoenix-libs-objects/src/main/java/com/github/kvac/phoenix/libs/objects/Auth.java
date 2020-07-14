package com.github.kvac.phoenix.libs.objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.github.kvac.phoenix.libs.objects.cs.CS;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jdcs_dev
 */
public class Auth implements Serializable{

    @Getter
    @Setter
    private CS who;

}
