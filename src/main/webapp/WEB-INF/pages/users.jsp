<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.technion.project.model.User"%>
<%@page session="true"%>
<html>
<head>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="/236369Project/JS/menu.js"></script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>


<script>
	function toggleEnabled(username){
		$.ajax("disable/"+username).done(function(data) {
		});
		}
	function deleteUser(username){
		$.ajax("delete/"+username).always(function(data) {
			$("#"+username).remove();
		});
		}
	function formSubmit() {
		document.getElementById("logoutForm").submit();
	}
	function showEvac() {
		$.ajax("admin/addEventView").done(function(data) {
			$("#result").html(data);
		}).fail(function(data) {
			$("#result").html(data.responseText);
		}).always(function() {
			//alert("complete");
		});
	}
	$.ajax("/236369Project/accounts/menu").done(function(result) {
		$("#menu").html(result);
	}).error(function(res){
		alert(res);
	});
</script>
</head>
<body>

	
	<div id="menu"></div>
	<%	Boolean isAdmin =  (Boolean)request.getAttribute("adminRight"); %>
	<table class="zebra">
	<tr>
	<th>image</th>
	<th>name</th>
	<th>user name</th>
	<th>reports</th>
	<% if(isAdmin)
		{
	%>
			<th>enabled</th>
			<th>delete</th>
	<%
		}
	%>
	</tr>		
	<%
	
		List<User> users = (List<User>) request.getAttribute("users");

		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = iterator.next();
	%>
		<tr id='<%=user.getUsername() %>'>
		<td><img src="<%= user.hasContainImage()? "/236369Project/download/" + user.getImageId():"/236369Project/IMG/images.jpg"%>" height="42" width="42"> </td>
		<td><%=user.getFname()%></td>
		<td><%=user.getUsername() %></td>
		<td><a href='/236369Project/reports/<%=user.getUsername() %>'>reports</a></td>
		<% if(isAdmin)
		{
			%>
				<td><input type="checkbox" <%=user.isEnabled()?"checked":"" %> onchange="toggleEnabled('<%=user.getUsername() %>')"></td>
				<td><input type="button" class="styledButton" value="delete" onclick="deleteUser('<%=user.getUsername() %>')"></td>
			<%
		}
			%>
		</tr>
	<%
		}
	%>
	</table>
</body>
</html>