<%-- 
    Document   : wait
    Created on : 2009-4-23, 21:44:34
    Author     : ruojun
--%>
<%@page contentType="text/html" pageEncoding="gbk"%>
<%@page import="com.keyplot.gp.user.domain.*;" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%User user = (User) session.getAttribute("user");%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript" src="jquery-1.3.1.js"></script>
        <script type="text/javascript">
            $(document).ready(
            function(){
                if(<%=!user.getUserEmail().equals("")%>){
                    $("#bUid",window.parent.document).html('<%=(user == null ? "游客" : user.getNickname())%>, 您好！');
                    $("#bUid",window.parent.document).css("background-color", "yellow");
                    $("#isLogin",window.parent.document).attr('value', "true");
                     $("#loginLink",window.parent.document).css('display', "none");

                    setTimeout(function(){
                        $("#bUid",window.parent.document).css("background-color", "");
                    }, 5000);
                }
            }
        );

            var activeTabID="c1";

            function hide(id){
                $("#"+id).hide();
            }
            function show(id){
                $("#"+id).show();
            }
            function showTab(id){
                hide(activeTabID);
                show(id);
                activeTabID=id;
            }
        </script>
    </head>
    <body>
        <h1>登录成功</h1>
    </body>
</html>
