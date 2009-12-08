/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.keyplot.gp.gogame.controller;

import com.keyplot.gp.common.StringUtils;
import com.keyplot.gp.gogame.business.MatchManager;
import com.keyplot.gp.gogame.domain.Match;
import com.keyplot.gp.user.domain.User;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.springframework.web.struts.DispatchActionSupport;

/**
 *
 * @author ruojun
 */
public class MatchAction extends DispatchActionSupport {

    public ActionForward sitdown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return mapping.findForward("");
        }

        String seat = request.getParameter("seatId");
        String seatStyle = seat.split("_")[0];
        String seat_id = seat.split("_")[1];
        Match match = MatchManager.getMatchBySeatId(seat_id);
        if (seatStyle.equals("leftSeat")) {
            if (StringUtils.isBlank(match.leftSeatPerson)) {
                match.leftSeatPerson = user.getNickname();
                return mapping.findForward("successSitdown");
            } else {
                return mapping.findForward("cannotSitdown");
            }
        } else if (seatStyle.equals("rightSeat")) {
            if (StringUtils.isBlank(match.rightSeatPerson)) {
                match.rightSeatPerson = user.getNickname();
                return mapping.findForward("successSitdown");
            } else {
                return mapping.findForward("cannotSitdown");
            }
        }



        return mapping.findForward("match");
    }
}
