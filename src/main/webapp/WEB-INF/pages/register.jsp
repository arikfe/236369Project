<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page session="true"%>
<html>
<head>


<title>Register Page</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.js"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {

						var t = document.getElementById("username");
						var n = document.getElementById("name");
						var k = document.getElementById("password");
						var j = document.getElementById("cpass");

						$('#cpass')
								.keyup(
										function() {
											if (k.value != j.value) {
												document
														.getElementById("pwerror").innerHTML = "<font color='ff0000'>Password does not match</font>";
											} else {
												document
														.getElementById("pwerror").innerHTML = "<font color='00aa00'>Password matches</font>";
											}
										});
					});

	function validateForm() {
		var k = document.getElementById("password");
		var j = document.getElementById("cpass");
		var un = setMissing("username");
		var name = setMissing("fname");
		var pass = setMissing("password");
		return k.value == j.value && un && name && pass;

	}
	function setMissing(id) {
		var field = $("#" + id)[0];
		if (field.value == "")
			$("#" + id + "_miss")[0].innerHTML = "<font color='ff0000'>*</font>";
		else
			$("#" + id + "_miss")[0].innerHTML = "";
		return field.value != "";
	}
</script>
<style>
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}

#login-box {
	width: 300px;
	padding: 20px;
	margin: 100px auto;
	background: #fff;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 1px solid #000;
}
</style>
</head>
<body onload='document.loginForm.username.focus();'>

	<h1>Register new user</h1>

	<div id="login-box">

		<h3>Register new user</h3>

		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>


		<form:form method="post" action="accounts/add?${_csrf.parameterName}=${_csrf.token}"
			enctype="multipart/form-data">
			<table>
				<tr>
					<td><div id="username_miss"></div>User:</td>
					<td><input type='text' name='username' id='username'>
					</td>
				</tr>
				<tr>
					<td><div id="name_miss"></div>First name:</td>
					<td><input type='text' name='fname' id='fname'></td>
				</tr>
				<tr>
					<td><div id="name_miss"></div>Last name:</td>
					<td><input type='text' name='lname' id='lname'></td>
				</tr>
				<tr>
 
					<td><div id="password_miss"></div>Password:</td>
					<td><input type='password' name='password' id='password' /></td>
				</tr>
				<tr>
					<td>Confirm password:</td>
					<td><input type='password' id='cpass' /></td>
				</tr>
				<tr>
					<td>choose picture:</td>
					<td><input type="file" name='file' id='file' /></td>
				</tr>
				<tr>
					<td colspan='2'><input name="submit" type="submit" /></td>
				</tr>
			</table>
		<div id="pwerror"></div>

		</form:form>
	</div>

</body>
</html>