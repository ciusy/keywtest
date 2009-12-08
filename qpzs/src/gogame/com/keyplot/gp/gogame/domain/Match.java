/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.keyplot.gp.gogame.domain;

import com.keyplot.gp.user.domain.User;
import java.util.*;

/**
 *
 * @author ruojun
 */
public class Match {
   public String whiteName="";
   public String BlackName="";
   public Date begin;
   public Date end;
   public String result=null;
   public List content=new ArrayList();
   public String seatId="";
   public String leftSeatPerson="";
   public String rightSeatPerson="";
   public String status="notplaying";//notplaying-未开始，playing-正在对局

   public void standUp(User user){
       String userId=user.getNickname();
       if(leftSeatPerson.equals(userId))leftSeatPerson="";
       if(rightSeatPerson.equals(userId))rightSeatPerson="";
   }
}
