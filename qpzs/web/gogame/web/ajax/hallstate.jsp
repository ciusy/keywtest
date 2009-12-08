<%@page contentType="text/xml" pageEncoding="UTF-8" language="java"%>  
<%@page import="com.keyplot.gp.user.domain.*,com.keyplot.gp.gogame.business.MatchManager,com.keyplot.gp.gogame.domain.*"%>
<%@page import="com.keyplot.gp.common.*,java.util.*"%>
<root>
    <%

        Match temp = null;
        for (int i = 0; i < 25; i++) {
            temp  = MatchManager.getMatchBySeatId((i + 1)+"");
            if (temp != null) {

    %>

    <seat>
        <id><%=temp.seatId%></id>
        <left>
            <isSeatdown><%=!StringUtils.isBlank(temp.leftSeatPerson)%></isSeatdown>
            <userId><%=temp.leftSeatPerson%></userId>
            <img>yiyi.gif</img>
        </left>
        <right>
            <isSeatdown><%=!StringUtils.isBlank(temp.rightSeatPerson)%></isSeatdown> 
            <userId><%=temp.rightSeatPerson%></userId>
            <img>clyde.gif</img>
        </right>
    </seat>

    <%
            }
        }
    %>



</root> 
