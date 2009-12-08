<%-- 
    Document   : index
    Created on : 2009-2-7, 21:44:25
    Author     : ruojun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="../js/jquery-1.3.1.js"></script>
        <script>
            $(document).ready(function(){
                //alert("hi");
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
        <h1>Hello World!</h1>
        <div id="test" >
            adfasdf
        </div>
        <a href="#" onclick="hide();">hide</a>
        <a href="#" onclick="show();">show</a>
    </body>
</html>
