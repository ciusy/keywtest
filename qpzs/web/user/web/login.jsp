<%@page contentType="text/html" pageEncoding="gbk"%>
<%@page import="com.keyplot.gp.common.*;" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=gbk" />
        <link rel="shortcut icon" href="../../resources/img/favicon.ico" />
        <title>����Χ����</title>
        <link rel="stylesheet" href="../css/login.css" type="text/css" />
        <style type="text/css">

        </style>



        <script type="text/javascript" src="jquery-1.3.1.js"></script>
        <script type="text/javascript">
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

        <div id="header">
            <div id="content" class="clearfix">
                <div id="main">
                    <div style="background-image: url(../img/login_bg.jpg); height: 400px;">
                        <form style="border-right: 1px solid rgb(204, 204, 204); margin: 30px 0pt 30px 50px; width: 550px; float: left;" method="post" id="login_form" action="/keyplot/user.do?method=login">    <img src="../img/login_title.gif"/>
                            <div style="border: 1px solid rgb(204, 204, 204); padding: 10px; background-color: white; width: 400px;">
                                <span style="color: rgb(66, 116, 161);font-size:1em;">����д���Ļ�Ա��������</span><br/>
                                <table cellpadding="5" width="100%">
                                    <tbody>
                                        <tr>
                                            <td width="110"><span style="color: red;">*</span> ��¼����</td><td><input type="text" name="email" id="email" class="text required"/></td>
                                        </tr>
                                        <tr>
                                            <td><span style="color: red;">*</span> ����</td><td><input type="password" value="" name="password" id="password" class="required text"/></td>
                                        </tr>
                                        <tr>
                                            <td> </td><td><input type="checkbox" value="1" name="remember_me" id="remember_me" checked="checked"/> �Զ���¼ <a href="/signup">ע��</a> <a onclick="$('reset_password_form').show();$('reset_password_name').focus();; return false;" href="#">��������</a></td>
                                        </tr>
                                        <tr>
                                            <td align="right"><input type="hidden" class="submit"/></td>
                                            <td><input type="image" src="../img/login_button.jpg"/></td>
                                        </tr>
                                </tbody></table>
                            </div>

                            Tips: ������κ����⣬����ϵ<a href="mailto:keyplot@yeah.net">keyplot@yeah.net</a>
                        </form>
                        <div style="float: left; width: 400px; height: 200px; margin-top: 35px; margin-left: 30px; line-height: 2.2; color: black;">
                            <p>KeyPlot��Χ�����ֵ���Ƚ�������, ����KeyPlot <span class="highlight">���ܹ���</span></p>
                            <ol>
                                <li>�������������壬���������κοͻ���</li>
                                <li>����ӵ����ӱ�ĸ��˲���</li>
                                <li>�������Ȧ�ӵ��˼ʽ���</li>
                                <li>Χ��ѧϰ�Ĺ���</li>
                            </ol>
                            <p>��ʹ��ȫ������?</p>
                            <a href="/signup"><img src="../img/signup_button.jpg"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>