<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@page import="com.mrj.person.*,java.util.*,com.mrj.policy.*,java.math.*,com.mrj.operate.policy.*,com.mrj.sto.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Insert title here</title>
</head>
<body>
<%
	Person person1 = new Person(new FittingPolicy(), new LastDayFinalPricePolicy(), new CapitalSituation(
			new ArrayList<ShareHolding>(), new BigDecimal(20000f)));
	Main.testPersonInvest(person1, "03/01/2009", "08/01/2009");
%>

</body>
</html>