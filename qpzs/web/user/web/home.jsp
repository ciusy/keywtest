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
        <title>����Χ����</title>
        <link rel="stylesheet" href="../css/home.css" type="text/css" />
        <script type="text/javascript" src="jquery-1.3.1.js"></script>
        <script type="text/javascript">
            $(document).ready(
            function(){
                if(<%=!user.getUserEmail().equals("")%>){
                    $("#bUid",window.parent.document).html('<%=(user == null ? "�ο�" : user.getNickname())%>, ���ã�');
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
                <P>��ӭ�㣺<%=user.getUserEmail()%></P>
            </div>
        </div>
        <div id="center">
            <div id="nav" >
                <ul>
                    <li><a href="#" onclick="showTab('c1');">Χ����ҳ</a></li>
                    <li><a href="#" onclick="showTab('c2');">���϶�ս</a></li>
                    <li><a href="#">�ҵĺ���</a></li>
                    <li><a href="#">��ʱ��Ϣ</a></li>
                    <li><a href="#">�˻���ս</a></li>
                    <li><a href="#">���׿�</a></li>
                    <li><a href="#">�����̳�</a></li>
                    <li><a href="#">�˳�</a></li>
                </ul>
            </div>
            <div id="content">
                <iframe id="c1" src="../../shouye/web/index.jsp" frameborder="0" ></iframe>
                <iframe id="c2" src="../../netplay/web/index.jsp" frameborder="0" style="display:none;"></iframe>



            </div>
        </div>
    </body>
</html>