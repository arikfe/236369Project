<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ page import="java.util.Iterator"%>
<%@ page import="com.technion.project.model.User"%>
<%@page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	User user = (User) request.getAttribute("user");
	String result = (String) request.getAttribute("result");
%>
<head>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="/236369Project/JS/menu.js"></script>
<link type="text/css" rel="stylesheet" href="/CSS/table.css"></link>
<link type="text/css" rel="stylesheet" href="/CSS/dropDownMenu.css"></link>
<script type="text/javascript">
	function validateForm() {
		var k = document.getElementById("password");
		var j = document.getElementById("cpass");

		if (k.value.length >= 6 && k.value == j.value)
			$("#resetPassword").prop( "disabled", false );
		else
			$("#resetPassword").prop( "disabled", true );
	}
	function deleteAccount() {
		if (confirm("Are you sure!") == true) {
			$.ajax("/236369Project/accounts/deleteself").always(function() {
				document.getElementById("logoutForm").submit();
			});
		}
	}

	$.ajax("/236369Project/accounts/menu").done(function(result) {
		$("#menu").html(result);
	}).error(function(res) {
		alert(res);
	});
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=user.getFname() + " " + user.getLname()%></title>
</head>
<body>
	<div id="menu"></div>
	<form
		action="/236369Project/accounts/update?${_csrf.parameterName}=${_csrf.token}"
		method="POST">
		<table>
			<tbody>
				<tr>
					<th>user name</th>
					<td><input type="text" name="username"
						value="<%=user.getUsername()%>" disabled>
				</tr>
				<tr>
					<th>First name</th>
					<td><input type="text" name="fname"
						value="<%=user.getFname()%>">
				</tr>
				<tr>
					<th>Last name</th>
					<td><input type="text" name="lname"
						value="<%=user.getLname()%>">
				</tr>



			</tbody>
		</table>
		<input type="submit" value="update" />
	</form>
	<form
		action="/236369Project/accounts/reset?${_csrf.parameterName}=${_csrf.token}"
		method="POST">
		<table>
			<tbody>
				<tr>
					<th>old password</th>
					<td><input type="password" name="oldpass">
				</tr>
				<tr>
					<th>new password</th>
					<td><input type="password" id='password' name="password" onkeyup="validateForm()">
				</tr>
				<tr>
					<th>confirm password</th>
					<td><input id='cpass' type="password" onkeyup="validateForm()">
				</tr>



			</tbody>
		</table>
		<input type="submit" id="resetPassword" value="Change password" disabled />
	</form>
	<div id='result'><%=result %></div>
	<input type="button" value="delete" onclick="deleteAccount()">
</body>
</html>