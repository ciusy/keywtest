<%@page contentType="text/html" pageEncoding="gbk"%>
<%@page import="com.keyplot.gp.user.domain.*;" %>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%
        User user = (User) session.getAttribute("user");
       
        %>
        <title>keyplot��Ϸƽ̨</title>
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
        <a id="lnkHome" target="_blank" href="#"><img border="0" id="imgLogo" class="gLogo" alt="keyplot��Ϸƽ̨" src="../../resources/img/logo.gif"/></a><!--�ʻ���Ϣ-->
        <div class="gUmsg">
            <div class="fLe">
                <b id="bUid"><%=(user == null ? "�ο�" : user.getNickname())%>, ���ã�</b>[
                <span id="loginLink"><a onclick="showTN('t4');" href="#">��¼</a>��</span>
                <a onclick="fGoto('CC.getServiceLink(\'safeconfig\', \'���뱣��\');')" href="javascript:fGoto()">���뱣��</a>��
                <a  id="lnkexit" href="/keyplot/user.do?method=logout">�˳�</a>]
            </div>
        </div>
        <div class="tLnk">
            <a onclick="fGoto('MM.getModule(\'optionMain\');');return false;" href="javascript:fGoto()">����</a> |
            <a target="_blank" id="lnkhelp" href="#">����</a>
        </div>
       
        <!--������ǩ start-->
        <div id="dvTab" class="gTab">
            <table tabid="welcome" style="width: 90px;" title="�ҵ���ҳ" class="on" id="t1" >
                <tbody>
                    <tr>
                        <td class="tLe"/>
                        <td class="bdy" >
                            <nobr>
                                ������ҳ
                            </nobr>
                        </td>
                        <td class="btn"/><td class="tRi"/>
                    </tr>
                </tbody>
            </table>
            <table tabid="inbox" style="width: 130px;display:none;" title="��Ϸ����" class=""  id="t2" >
                <tbody>
                    <tr>
                        <td class="tLe"/>
                        <td class="bdy">
                            <nobr>
                                ��Ϸ����
                            </nobr>
                        </td>
                        <td class="btn">
                            <a href="#" title="�ر�" class="TabCls" />
                        </td><td class="tRi"/>
                    </tr>
                </tbody>
            </table>
            <table tabid="defaultOutLink_assis" style="width: 130px;display:none;" title="���������Ө�ĶԾ�" class="" id="t3" >
                <tbody>
                    <tr>
                        <td class="tLe"/>
                        <td class="bdy" id="bdy3">
                            <nobr>
                                ���������Ө�ĶԾ�
                            </nobr>
                        </td>
                        <td class="btn">
                            <a href="#" title="�ر�" class="TabCls"/>
                        </td><td class="tRi"/>
                    </tr>
                </tbody>
            </table>
            <table tabid="defaultOutLink_assis" style="width: 130px;display:none;" title="��¼" class="" id="t4">
                <tbody>
                    <tr>
                        <td class="tLe"/>
                        <td class="bdy" id="login_bdy">
                            <nobr>
                                ��¼
                            </nobr>
                        </td>
                        <td class="btn">
                            <a href="#" title="�ر�" class="TabCls" id="login_close"/>
                        </td><td class="tRi"/>
                    </tr>
                </tbody>
            </table>
             <script>
            if(<%=(user!=null)%>){
                $('#t4').hide();
            }
        </script>
        </div><!--������ǩ end-->
        <div id="oMusicBoxFloat">
            <div class="divTmpMus bgF2"/>
            </div>
        </div>
        <table class="gMain">

        </table>
        <div id="tbMsg" style="top: 24px; right: 10px; z-index: 60; left: 1130px; display: none;" class="gSLoading">
            <span id="spnMsg">���ݼ�����,���Ժ�..</span>
        </div>

        <div id="left_box">
            <ul class="nav" id="left_nav">
                <li><a href="#">��ҳ</a></li>
                <li><a href="#" onclick="showTN('t2');">��Ϸ����</a></li>
                <li><a href="#">���׿�</a></li>
                <li><a href="#">������̳</a></li>
                <li><a href="#">�ҵĺ���</a></li>
                <li><a href="#">����Ϣ</a></li>
                <li><a href="#">����</a></li>


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
