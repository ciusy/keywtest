<%@page contentType="text/html" pageEncoding="gbk"%>
<%@page import="com.keyplot.gp.user.domain.*;" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
        }
        %>
        <meta http-equiv="Content-Type" content="text/html; charset=gbk" />
        <link rel="shortcut icon" href="../../resources/img/favicon.ico" />
        <title>若君围棋网</title>
        <link rel="stylesheet" href="../css/home.css" type="text/css" />
        <script type="text/javascript" src="jquery-1.3.1.js"></script>
        <script type="text/javascript">
            $(document).ready(
            function(){
                if(<%=!user.getUserEmail().equals("")%>){
                    $("#bUid",window.parent.document).html('<%=(user == null ? "游客" : user.getNickname())%>, 您好！');
                    $("#bUid",window.parent.document).css("background-color", "yellow");                    
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
        <div  id="top"  >
            <div id="logo" >
                <p ><img src="../../resources/img/logo.jpg"/></p>
            </div>
            <div id="welcome">
                <P>欢迎你：<%=user.getUserEmail()%></P>
            </div>
        </div>
        <div id="center">
            <div id="nav" >
                <ul>
                    <li><a href="#" onclick="showTab('c1');">围棋首页</a></li>
                    <li><a href="#" onclick="showTab('c2');">网上对战</a></li>
                    <li><a href="#">我的好友</a></li>
                    <li><a href="#">即时消息</a></li>
                    <li><a href="#">人机对战</a></li>
                    <li><a href="#">棋谱库</a></li>
                    <li><a href="#">若君商城</a></li>
                    <li><a href="#">退出</a></li>
                </ul>
            </div>
            <div id="content">
                <iframe id="c1" src="../../shouye/web/index.jsp" frameborder="0" ></iframe>
                <iframe id="c2" src="../../netplay/web/index.jsp" frameborder="0" style="display:none;"></iframe>



            </div>
        </div>
    </body>
</html>