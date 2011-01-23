<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>股票决策系统控制台</title>
    <link type="text/css" rel="stylesheet" href="http://localhost:8080/Jss/StockSimulator/css/pages/controlPanel.css">
    <!--Include YUI Loader: -->
    <script type="text/javascript"
            src="http://localhost:8080/Jss/StockSimulator/js/yui/build/yuiloader/yuiloader-debug.js"></script>
    <script type="text/javascript">
        // Instantiate and configure YUI Loader:
        (function() {
            var loader = new YAHOO.util.YUILoader({
                base: "http://localhost:8080/Jss/StockSimulator/js/yui/build/",
                require: ["animation","calendar","datasource","datatable","datemath","dom","dragdrop","element","event","event-delegate","get","json","paginator","selector","tabview","treeview","yahoo","yuiloader"],
                loadOptional: false,
                combine: false,
                filter: "DEBUG",
                allowRollup: false,
                onSuccess: function() {
                    //you can make use of all requested YUI modules
                    //here.
                }
            });

// Load the files using the insert() method.
            loader.insert();
        })();
    </script>
    <script type="text/javascript" src="http://localhost:8080/Jss/StockSimulator/js/pageBus/subjects.js"></script>
    <script type="text/javascript" src="http://localhost:8080/Jss/StockSimulator/js/pageBus/pagebus.js"></script>
    <script type="text/javascript" src="http://localhost:8080/Jss/StockSimulator/js/pageBus/messages.js"></script>
    <script type="text/javascript" src="http://localhost:8080/Jss/StockSimulator/js/pages/controlPanel.js"></script>

</head>
<body>
<div id="left_nav">
    <ul>
        <li>菜单一</li>
        <li>菜单二</li>
        <li>菜单三</li>
        <li>菜单四</li>

    </ul>
</div>
<div id="center">

    <div id="main_panel">
        <div id="personList">
            <div>
                <button class="savePersonList">保存当前PersonList</button>
                |
                <button class="loadPersonList">载入PersonList</button>
            </div>
            <div><input type="radio" name="IsSameDate" value="true">所有Person选择相同日期<br>

                <input type="radio" name="IsSameDate" value="false" checked>分别为每个Person指定日期<br>

                <div class="personTable">
                    <a href="#" class="addPerson">添加Person</a>
                </div>

            </div>

            <div class="clear"></div>

        </div>
        <div id="main_main">
            <div class="control">控制按钮组</div>
            <div class="result">结果报表</div>
            <div class="chart">结果Chart</div>
        </div>
        <div id="status_panel">状态显示:</div>
    </div>
</div>

</body>
</html>