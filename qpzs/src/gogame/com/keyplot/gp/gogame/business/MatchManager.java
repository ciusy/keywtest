/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.keyplot.gp.gogame.business;

import com.keyplot.gp.common.StringUtils;
import com.keyplot.gp.gogame.domain.Match;
import com.keyplot.gp.user.domain.User;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ruojun
 */
public class MatchManager {

    public static Map<String, Match> matchMap = new HashMap();

  

    public static String sitdown(HttpServletRequest request)
            throws Exception {
        String re = "cannotSitdown";
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "userIsNull";
        }
        Match match = null; 
        try {
            String seat = request.getParameter("seatId");
            String seatStyle = seat.split("_")[0];
            String seat_id = seat.split("_")[1];
            Match oldMatch=user.match;
            if(oldMatch!=null){
                if(!isPlaying(oldMatch)){
                    user.match.standUp(user);
                }
            }
            match = MatchManager.getMatchBySeatId(seat_id);
            if (seatStyle.equals("leftSeat")) {
                if (StringUtils.isBlank(match.leftSeatPerson)) {
                    match.leftSeatPerson = user.getNickname();
                    re = "successSitdown";
                    return (re);
                } else {
                    return ("cannotSitdown");
                }
            } else if (seatStyle.equals("rightSeat")) {
                if (StringUtils.isBlank(match.rightSeatPerson)) {
                    match.rightSeatPerson = user.getNickname();
                    re = "successSitdown";
                    return (re);
                } else {
                    return ("cannotSitdown");
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        } finally { 
            if (re.equals("successSitdown")) {
                    user.match = match; 
            }
        }
    }

    public static String begin(HttpServletRequest request){
        return null;
    }

    public static void main(String[] args) {
        System.out.println(System.nanoTime());
    }

    private static Match getMatchByUser(User user) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

      public static Match getMatchBySeatId(String seatId) {
        Match re = matchMap.get(seatId);
        if (re == null) {
            re = new Match();
            re.seatId = seatId;
            matchMap.put(seatId, re);
        }
        return re;
    }

    private static boolean isPlaying(Match oldMatch) {
       return (oldMatch.status.equals("playing"));
    }
}
