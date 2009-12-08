<%-- 
    Document   : index
    Created on : 2009-2-9, 20:54:09
    Author     : ruojun
--%>

<%@page contentType="text/html" pageEncoding="gbk"%>
<%@page import="com.keyplot.gp.common.*;" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=gbk" />
        <title>若君围棋网</title>
        <script src="../js/jquery-1.3.1.js"></script>
        <style type="text/css">
            root {
                display: block;
            }
            *{
                margin:0;
                padding:0;
            }
            p{
                margin:10px 10px;
            }
            div,code,p,h1,h2,address,dt,dd,li{font: normal 12px/1.5em Tahoma,"Lucida Grande",Helvetica,Verdana,Lucida,Arial,"Arial Unicode",sans-serif,serif}

            .normalbox{
                margin:10px 10px 10px 10px;
                border:#CC0000 1px solid;
            }
            #p_play{
                float:left;
                width:600px;
                height:100%;
                height:650px;
            }
            #p_playerInfo{
                width:250px;
                height:100%;
                height:650px;
            }
            .qipan_smallbox{
                background:url(../img/go_qipan.jpg);
                width:30px;
                height:30px;
                float:left;
            }
            .qipan_smallbox_lu{
                background:url(../img/go_qipan_lu.jpg);
                width:30px;
                height:30px;
                float:left;
            }
            .qipan_smallbox_ld{
                background:url(../img/go_qipan_ld.jpg);
                width:30px;
                height:30px;
                float:left;
            }

            .qipan_smallbox_ru{
                background:url(../img/go_qipan_ru.jpg);
                width:30px;
                height:30px;
                float:left;
            }

            .qipan_smallbox_rd{
                background:url(../img/go_qipan_rd.jpg);
                width:30px;
                height:30px;
                float:left;
            }
            .qipan_smallbox_u{
                background:url(../img/go_qipan_u.jpg);
                width:30px;
                height:30px;
                float:left;
            }
            .qipan_smallbox_d{
                background:url(../img/go_qipan_d.jpg);
                width:30px;
                height:30px;
                float:left;
            }

            .qipan_smallbox_l{
                background:url(../img/go_qipan_l.jpg);
                width:30px;
                height:30px;
                float:left;
            }
            .qipan_smallbox_r{
                background:url(../img/go_qipan_r.jpg);
                width:30px;
                height:30px;
                float:left;
            }


        </style>

        <script>
            $(document).ready(function(){
                $("#gezi_3_3").style="background:url(../img/go_qipan_heidian.jpg)";
            });
            function hide(){
                $("#test").hide();
            }
            function show(){
                $("#test").show();
            }
        </script>
    </head>
    <body>
        <DIV id="p_play" class="normalbox">
            <div id="gezi_0_0" class="qipan_smallbox_lu"></div>
            <%for (int i = 0; i < 17; i++) {%>
            <div id='<%="gezi_0_" + (i + 1)%>' class="qipan_smallbox_u"></div>
            <%}%>
            <div id="gezi_0_18" class="qipan_smallbox_ru"></div>
            <div style="height:30px;"></div>

            <%for (int i = 0; i < 17; i++) {%>
            <div id='<%="gezi_" + (i + 1) + "_0"%>' class="qipan_smallbox_l"></div>
            <%for (int j = 0; j < 17; j++) {%>
            <div id='<%="gezi_" + (i + 1) + "_" + (j + 1)%>' class="qipan_smallbox"></div>
            <%}%>
            <div id='<%="gezi_" + (i + 1) + "_18"%>' class="qipan_smallbox_r"></div>
            <div style="height:30px;"></div>
            <%}%>

            <div class="qipan_smallbox_ld"></div>
            <%for (int i = 0; i < 17; i++) {%>
            <div class="qipan_smallbox_d"></div>
            <%}%>
            <div class="qipan_smallbox_rd"></div>
            <div style="height:30px;"></div>

            <script>
                $("#gezi_3_3").css("background", "url(../img/go_qipan_heidian.jpg)");
                $("#gezi_3_9").css("background", "url(../img/go_qipan_heidian.jpg)");
                $("#gezi_3_15").css("background", "url(../img/go_qipan_heidian.jpg)");
                $("#gezi_9_3").css("background", "url(../img/go_qipan_heidian.jpg)");
                $("#gezi_9_9").css("background", "url(../img/go_qipan_heidian.jpg)");
                $("#gezi_9_15").css("background", "url(../img/go_qipan_heidian.jpg)");
                $("#gezi_15_3").css("background", "url(../img/go_qipan_heidian.jpg)");
                $("#gezi_15_9").css("background", "url(../img/go_qipan_heidian.jpg)");
                $("#gezi_15_15").css("background", "url(../img/go_qipan_heidian.jpg)");
            </script>
        </DIV>

        <div id="p_playerInfo" class="normalbox">

        </div>



    </body>
</html>
