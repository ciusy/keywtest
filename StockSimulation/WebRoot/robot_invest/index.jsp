<%@ page language="java" import="java.util.*,com.mrj.person.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="css/robot_invest.css">
		<link rel="stylesheet" type="text/css" href="../css/global.css">
		<script type="text/javascript" src="../js/jquery-1.3.1.js"></script>
		<script type="text/javascript">
		
		</script>
		<%
			ArrayList<Person> top100 = new ArrayList<Person>();
		%>

	</head>

	<body>
		<div id="robot_info_list" title="机器人投资排名列表">
			<div align="center">
				机器人投资排名列表
			</div>
			<table class="myTable2" width="100%">
				<tr>
					<td class="whiteBg" width="20%">
						排名
					</td>
					<td class="whiteBg" width="79%">
						UUID
					</td>
				</tr>

				<%
					for (int i = 0; i < top100.size(); i++) {
						Person p = top100.get(i);
				%>
				<tr>
					<td><%=i + 1%></td>
					<td><%=p.getUserUuid()%></td>
				</tr>
				<%
					}
				%>
			</table>

		</div>
		<div id="robot_info_detail" title="机器人投资详情">


			<div class="menu">
				<div class="tabs" id="Main_mainwindow_menu_tabs">
					<div href="#" style="display: block;" tab="msg_box" id="Main_mainwindow_msg_box_tab_head">
						<span>投资概况</span>
					</div>
					<div href="#" style="display: block;" tab="buddys_feeds" id="Main_mainwindow_buddys_feeds_tab_head" class="current">
						<span>资产曲线</span>
					</div>
					<div href="#" style="display: block;" tab="buddys_impressions" id="Main_mainwindow_buddys_impressions_tab_head">
						<span>资金流水</span>
					</div>

					<div href="#" style="display: block;" tab="music_box" id="Main_mainwindow_music_box_tab_head">
						<span>明日推荐</span>
					</div>

				</div>
			</div>
		</div>
	</body>
</html>
