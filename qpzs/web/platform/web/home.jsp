<%@page contentType="text/html" pageEncoding="gbk"%>
<%@page import="com.keyplot.gp.user.domain.*;" %>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%
        User user = (User) session.getAttribute("user");
       
        %>
        <title>keyplot游戏平台</title>
        <link id="cssGlobe" type="text/css" rel="stylesheet" href="../css/globle.css"/>
        <link id="cssStyle" type="text/css" rel="stylesheet" href="../css/share_blue_s.css"/>

        <link id="cssStyle" type="text/css" rel="stylesheet" href="../css/home.css"/>
      
        </style>
        <script src="../../jq_study/js/jquery-1.3.1.js"></script>
        <script src="../js/home.js"></script>
       

    </head>
    <body lang="zh" class="gPage" >
        <input type="hidden" value="false" id="isLogin">
        <div id="dvTop" class="gTop">
        <a id="lnkHome" target="_blank" href="#"><img border="0" id="imgLogo" class="gLogo" alt="keyplot游戏平台" src="../../resources/img/logo.gif"/></a><!--帐户信息-->
        <div class="gUmsg">
            <div class="fLe">
                <b id="bUid"><%=(user == null ? "游客" : user.getNickname())%>, 您好！</b>[
                <span id="loginLink"><a onclick="showTN('t4');" href="#">登录</a>，</span>
                <a onclick="fGoto('CC.getServiceLink(\'safeconfig\', \'密码保护\');')" href="javascript:fGoto()">密码保护</a>，
                <a  id="lnkexit" href="/keyplot/user.do?method=logout">退出</a>]
            </div>
        </div>
        <div class="tLnk">
            <a onclick="fGoto('MM.getModule(\'optionMain\');');return false;" href="javascript:fGoto()">设置</a> |
            <a target="_blank" id="lnkhelp" href="#">帮助</a>
        </div>
       
        <!--顶部标签 start-->
        <div id="dvTab" class="gTab">
            <table tabid="welcome" style="width: 90px;" title="我的首页" class="on" id="t1" >
                <tbody>
                    <tr>
                        <td class="tLe"/>
                        <td class="bdy" >
                            <nobr>
                                金珀首页
                            </nobr>
                        </td>
                        <td class="btn"/><td class="tRi"/>
                    </tr>
                </tbody>
            </table>
            <table tabid="inbox" style="width: 130px;display:none;" title="游戏大厅" class=""  id="t2" >
                <tbody>
                    <tr>
                        <td class="tLe"/>
                        <td class="bdy">
                            <nobr>
                                游戏大厅
                            </nobr>
                        </td>
                        <td class="btn">
                            <a href="#" title="关闭" class="TabCls" />
                        </td><td class="tRi"/>
                    </tr>
                </tbody>
            </table>
            <table tabid="defaultOutLink_assis" style="width: 130px;display:none;" title="孟若君与黄莹的对局" class="" id="t3" >
                <tbody>
                    <tr>
                        <td class="tLe"/>
                        <td class="bdy" id="bdy3">
                            <nobr>
                                孟若君与黄莹的对局
                            </nobr>
                        </td>
                        <td class="btn">
                            <a href="#" title="关闭" class="TabCls"/>
                        </td><td class="tRi"/>
                    </tr>
                </tbody>
            </table>
            <table tabid="defaultOutLink_assis" style="width: 130px;display:none;" title="登录" class="" id="t4">
                <tbody>
                    <tr>
                        <td class="tLe"/>
                        <td class="bdy" id="login_bdy">
                            <nobr>
                                登录
                            </nobr>
                        </td>
                        <td class="btn">
                            <a href="#" title="关闭" class="TabCls" id="login_close"/>
                        </td><td class="tRi"/>
                    </tr>
                </tbody>
            </table>
             <script>
            if(<%=(user!=null)%>){
                $('#t4').hide();
            }
        </script>
        </div><!--顶部标签 end-->
        <div id="oMusicBoxFloat">
            <div class="divTmpMus bgF2"/>
            </div>
        </div>
        <table class="gMain">

        </table>
        <div id="tbMsg" style="top: 24px; right: 10px; z-index: 60; left: 1130px; display: none;" class="gSLoading">
            <span id="spnMsg">数据加载中,请稍候..</span>
        </div>

        <div id="left_box">
            <ul class="nav" id="left_nav">
                <li><a href="#">首页</a></li>
                <li><a href="#" onclick="showTN('t2');">游戏大厅</a></li>
                <li><a href="#">棋谱库</a></li>
                <li><a href="#">棋友论坛</a></li>
                <li><a href="#">我的好友</a></li>
                <li><a href="#">短消息</a></li>
                <li><a href="#">关于</a></li>


            </ul>
        </div>

        <div id="content_box" >
            <!--<iframe src="http://weiqi.sports.tom.com" style="width:100%;height:100%;" frameborder="0" border="0" id="if1" ></iframe>-->
            <iframe src="../../shouye/web" style="width:100%;height:100%;" frameborder="0" border="0" id="if1" ></iframe>
            <iframe src="../../gogame/web/room.jsp" style="width:100%;height:100%;display:none;" frameborder="0" border="0" id="if2"></iframe>
            <iframe src="../../gogame/web/index.jsp" style="width:100%;height:100%;display:none;" frameborder="0" border="0" id="if3" ></iframe>
            <iframe src="../../user/web/login.jsp" style="width:100%;height:100%;display:none;" frameborder="0" border="0" id="if4" ></iframe>
        </div>
        <ie:clientcaps id="oClientCaps">
            <span style="display: none;" class="userData" id="oUserData"/>

        </ie:clientcaps>
        <div style="display: none;">
        </div>
        <div style="display: none;">
        </div>
    </body>
</html>
