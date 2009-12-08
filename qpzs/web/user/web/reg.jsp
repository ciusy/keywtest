<%-- 
    Document   : reg
    Created on : 2009-2-8, 17:25:49
    Author     : ruojun
--%>

<%@page contentType="text/html" pageEncoding="gbk"%>
<%@page import="com.keyplot.gp.common.*;" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=gbk" />
        <title>若君围棋网</title>
        <script type="text/javascript" src="jquery-1.3.1.js"></script>
        <style type="text/css">
            #reg_div{
                border:#CC6600 1px solid;
                margin:10px 10px;
            }
            p{
                margin:10px 10px;
            }
            fieldset{
                margin:10px 10px;


            }
        </style>

        <script type="text/javascript">
        </script>
    </head>
    <body>
        <div id="reg_div">
            <p>简单一步，快速注册</p>
            <form id="reg"  action="/keyplot/user.do?method=reg_sub"  method="post">
                <fieldset id="form_1">
                    <p id="p_email">
                        <label for="email">电子邮箱：</label>
                        <input id="email" name="email" type="text" tabindex="1" value=""/>
                        <span id="emailg" style="display:none;"><img src="../../resources/img/green_right_symbol.gif"/></span>
                    </p>
                    <p id="p_pwd">
                        <label for="passwd">设置密码：</label>
                        <input type="password" name="passwd" maxlength="20" tabindex="2" value=""  id="passwd" />
                        <span id="mmg" style="display:none;"><img src="../../resources/img/green_right_symbol.gif"/></span>
                    </p>
                    <p id="p_pwd2">
                        <label for="passwd2">确认密码：</label>
                        <input type="password" name="passwd2" maxlength="20" tabindex="3" value=""  id="passwd2"  />
                        <span id="mmg2" style="display:none;"><img src="../../resources/img/green_right_symbol.gif"/></span>
                    </p>
                    <p id="p_nickname">
                        <label for="nickname">昵称：&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <input type="text" name="nickname" tabindex="4" value="" id="nickname"  />
                        <span id="xmg" style="display:none;"><img src="../../resources/img/green_right_symbol.gif"/></span>
                    </p>
                    
                    <p></p>
                    <p id="P_submit">
                        <input  type="submit" name="submit" value="免费注册"/>
                    </p>
                </fieldset>
            </form>

        </div>
    </body>
</html>
