<%-- 
    Document   : room
    Created on : 2009-4-18, 22:22:36
    Author     : ruojun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.keyplot.gp.user.domain.*;" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <%
        User user = (User) session.getAttribute("user");
        // out.print(user==null);
%>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link id="cssStyle" type="text/css" rel="stylesheet" href="../css/room.css"/>
        <script src="../../jq_study/js/jquery-1.3.1.js"></script>
        <!--<script src="../js/room.js"></script>-->
        <script>

            var suc_sitDownSeatId="";
            function loadHallState(){//定期载入大厅状态列表，以便更新座位状态
                $.ajax({
                    url: "/keyplot/gogame/web/ajax/hallstate.jsp",
                    type: 'GET',
                    data: "",
                    dataType: 'xml',
                    error: function(){
                        alert('Error loading XML document');
                    },
                    success: function(xml){
                        jQuery(xml).find('seat').each(function(){
                            var seat_id = $(this).find("id").text();
                            var left= $(this).find("left");
                            var left_isSeatdown=left.find("isSeatdown").text();
                            var left_userId=left.find("userId").text();
                            var left_img=left.find("img").text();
                            if(left_isSeatdown=="true"){
                                $('#leftSeat_'+seat_id).css('background-image', 'url(../img/yiyi.gif)');
                            }else{
                                $('#leftSeat_'+seat_id).css('background-image', 'url(../img/blank_seat.gif)');
                            }


                            var right= $(this).find("right");
                            var right_isSeatdown=right.find("isSeatdown").text();
                            var right_userId=right.find("userId").text();
                            var right_img=right.find("img").text();
                            if(right_isSeatdown=="true"){
                                $('#rightSeat_'+seat_id).css('background-image', 'url(../img/clyde.gif)');
                            }else{
                                $('#rightSeat_'+seat_id).css('background-image', 'url(../img/blank_seat.gif)');
                            }
                        });


                    }
                }
            );

            }
            $(document).ready(
            function(){
                setInterval(loadHallState, 2000);


                $('.leftSide').mouseover(function(){
                    var url=$(this).css("background-image");
                    if(url.indexOf('blank_')!=-1)
                        $(this).css('background-image', 'url(../img/blank_over.gif)');
                }).mouseout(function(){
                    var url=$(this).css("background-image");
                    if(url.indexOf('blank_')!=-1)
                        $(this).css('background-image', 'url(../img/blank_seat.gif)');
                });
                $('.rightSide').mouseover(function(){
                    var url=$(this).css("background-image");
                    if(url.indexOf('blank_')!=-1)
                        $(this).css('background-image', 'url(../img/blank_over.gif)');
                }).mouseout(function(){
                    var url=$(this).css("background-image");
                    if(url.indexOf('blank_')!=-1)
                        $(this).css('background-image', 'url(../img/blank_seat.gif)');
                });

                $('.leftSide').click(sidtown);
                $('.rightSide').click(sidtown);
            }
        );


            function sidtown(){
                var id=$(this).attr("id");
                $.ajax({
                    url: "/keyplot/gogame/web/ajax/match.jsp",
                    type: 'GET',
                    data: "method=sitdown&seatId="+$(this).attr("id"),
                    dataType: 'xml',
                    error: function(){
                        alert('Error loading XML document');
                    },
                    success: function(xml){
                        $(xml).find('value').each(function(){
                            var item_text = $(this).text();
                            if(item_text=="successSitdown"){
                                if(suc_sitDownSeatId!=""){//如果已经坐下了，当前的座位就要空出来。
                                    $('#'+suc_sitDownSeatId).css('background-image', 'url(../img/blank_seat.gif)');
                                }
                                suc_sitDownSeatId=id;//更新图片以表示坐下
                                $('#'+id).css('background-image', 'url(../img/yiyi.gif)');

                            }
                            $('<li></li>')
                            .html(item_text)
                            .appendTo('ol');
                        });
                    }
                });
            }




        </script>
        <%
        int I = 5, J = 5;//从横都是5个位置;
%>


    </head>
    <body>
        <ul >
            <li><a href="#">加入</a></li>
            <li><a href="#">找人</a></li>
            <li><a href="#">设置</a></li>
            <li><a href="#">棋友论坛</a></li>
        </ul>
        <p style="clear:both"></p>
        <table id="hall">
            <%for (int j = 0; j < I; j++) {%>
            <tr>
                <%for (int i = 0; i < J; i++) {%>
                <td id="<%="desk_" + (j * I + (i + 1))%>">
                    <div class="leftSide" id="<%="leftSeat_" + (j * I + (i + 1))%>"></div>
                    <div class="middleDesk" id="<%="chessborad_" + (j * I + (i + 1))%>"></div>
                    <div class="rightSide" id="<%="rightSeat_" + (j * I + (i + 1))%>"></div>
                </td>
                <%}%>
            </tr>
            <%}%>
        </table>
        <ol><li></li></ol>


    </body>
</html>
