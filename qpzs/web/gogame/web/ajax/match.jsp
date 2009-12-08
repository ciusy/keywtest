<%@page contentType="text/xml" pageEncoding="UTF-8" language="java"%>
<%@page import="com.keyplot.gp.user.domain.*,com.keyplot.gp.gogame.business.MatchManager,com.keyplot.gp.gogame.domain.*;"%>

<%
String method=request.getParameter("method");
if(method==null)method="";
 String value="";
if(method.equals("sitdown")){
     value=MatchManager.sitdown(request);
}else{
     }
%>
<root>
    <seat>
        <id><%=request.getParameter("seatId")%></id>
        <method>
            <name><%=method%></name>
            <value><%=value%></value>
        </method>
    </seat>
</root>
