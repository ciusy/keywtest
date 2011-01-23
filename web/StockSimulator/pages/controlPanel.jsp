<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>股票决策系统控制台</title>
    <!--Include YUI Loader: -->
<script type="text/javascript" src="http://localhost:8080/Jss/StockSimulator/js/yui/buildyuiloader/yuiloader-min.js"></script>
<script type="text/javascript" src="http://localhost:8080/Jss/StockSimulator/js/yui/buildyuiloader/yuiloader-min.js"></script>   
    <!--Use YUI Loader to bring in your other dependencies: -->
<script type="text/javascript">
// Instantiate and configure YUI Loader:
(function() {
    var loader = new YAHOO.util.YUILoader({
        base: "http://localhost:8080/Jss/StockSimulator/js/yui/build",
        require: ["datasource","dom","element","element-delegate","event","event-delegate","get","json","logger","paginator","profiler","selector","tabview","treeview","utilities","yahoo","yuiloader"],
        loadOptional: false,
        combine: false,
        filter: "MIN",
        allowRollup: true,
        onSuccess: function() {
            //you can make use of all requested YUI modules
            //here.
        }
    });

// Load the files using the insert() method.
loader.insert();
})();
</script> 
</head>
<body>

<div id="personList"></div>
<div id="control"></div>
<div id="result"></div>
<div id="chart"></div>

</body>
</html>