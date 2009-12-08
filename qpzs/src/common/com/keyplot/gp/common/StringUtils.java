/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.keyplot.gp.common;

/**
 *
 * @author ruojun
 */
public class StringUtils {

    public static boolean isBlank(String s) {
        if (s == null || s.equals("")) {
            return true;
        } else {
            return false;
        }
    }
}
