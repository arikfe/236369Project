<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.technion.project.model.User"%>
<%@page session="true"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	User user = (User) request.getAttribute("user");
	String result = (String) request.getAttribute("result");
	if (result == null)
		result =  "";
	
%>
<head>

<link type="text/css" rel="stylesheet" href="/CSS/table.css"></link>
<link type="text/css" rel="stylesheet" href="/CSS/dropDownMenu.css"></link>
<c:set var="baseURL" value="${pageContext.request.contextPath}" />
<c:set var="adminURL" value="${pageContext.request.contextPath}/admin" />
<c:set var="reportURL"
	value="${pageContext.request.contextPath}/reports" />
<c:set var="accountURL"
	value="${pageContext.request.contextPath}/accounts" />
<script src="${baseURL}/JS/menu.js"></script>
<script src="${baseURL}/JS/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
	function validateForm() {
		var k = document.getElementById("password");
		var j = document.getElementById("cpass");

		if (k.value.length >= 6 && k.value == j.value)
			$("#resetPassword").prop("disabled", false);
		else
			$("#resetPassword").prop("disabled", true);
	}
	function deleteAccount(_username) {
		if (confirm("Are you sure!") == true) {
			$.ajax({
				type : "delete",
				url : "${accountURL}/"+_username.value+"?${_csrf.parameterName}=${_csrf.token}"}).always(function() {
				document.getElementById("logoutForm").submit();
			});
		}
	}
	function update(_username, _fname, _lname) {

		$.ajax({
			type : "put",
			url : "${accountURL}/"+_username.value+"?${_csrf.parameterName}=${_csrf.token}",
			data : {
				username :_username.value,
				fname : _fname.value,
				lname : _lname.value
			}
		}).error(function(err){
			alert(err);
		});
	}
	
	$.ajax("${baseURL}/menu").done(function(result) {
		$("#menu").html(result);
	}).error(function(res) {
		alert(res);
	});
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style>
#update 
{
    background-color: blue;
    -moz-border-radius: 5px;
    -webkit-border-radius: 5px;
    border-radius:6px;
    color: white;
    font-family: 'Oswald';
    font-size: 24px;
    text-decoration: none;
    cursor: poiner;
     border:none;
     height: 50px;
     width: 100px;
     text-align: justify;
}
#update:hover {
    border: none;
    background:green;
    box-shadow: 0px 0px 1px #777;
}
</style>
<title><%=user.getFname() + " " + user.getLname()%></title>
</head>
<body>
	<div id="menu"></div>

	<table>
		<tbody>
			<tr>
				<th>user name</th>
				<td><input id="username" type="text" name="username"
					value="<%=user.getUsername()%>" disabled>
			</tr>
			<tr>
				<th>First name</th>
				<td><input id="fname" type="text" name="fname"
					value="<%=user.getFname()%>">
			</tr>
			<tr>
				<th>Last name</th>
				<td><input id="lname" type="text" name="lname"
					value="<%=user.getLname()%>">
			</tr>



		</tbody>
	</table>
	<button id="update"  onclick="update(username,fname,lname)">Update</button>

	<form
		action="${accountURL}/<%=user.getUsername() %>/reset?${_csrf.parameterName}=${_csrf.token}"
		method="POST">
		<table>
			<tbody>
				<tr>
					<th>old password</th>
					<td><input type="password" name="oldpass">
				</tr>
				<tr>
					<th>new password</th>
					<td><input type="password" id='password' name="password"
						onkeyup="validateForm()">
				</tr>
				<tr>
					<th>confirm password</th>
					<td><input id='cpass' type="password" onkeyup="validateForm()">
				</tr>



			</tbody>
		</table>
		<input type="submit" id="resetPassword" value="Change password"
			disabled />
	</form>
	<div id='result'><%=result%></div>
	<input type="button" value="delete" onclick="deleteAccount(username)">
</body>
</html>