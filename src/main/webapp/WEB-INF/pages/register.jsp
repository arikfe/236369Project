<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page session="true"%>
<html>
<head>
<c:set var="baseURL" value="${pageContext.request.contextPath}"/> 
<c:set var="adminURL" value="${pageContext.request.contextPath}/admin"/> 
<c:set var="reportURL" value="${pageContext.request.contextPath}/reports"/> 
<c:set var="accountURL" value="${pageContext.request.contextPath}/accounts"/>
<c:set var="evacuationURL" value="${pageContext.request.contextPath}/evacuation"/>

<title>Register Page</title>

<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.js"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function() {

						var k = document.getElementById("password");
						var j = document.getElementById("cpass");
						var submitElement = document.getElementById("submit1");
						$('#username').keyup(function(){setRegisterState();});
						$('#cpass')
								.keyup(
										function() {
											if (k.value != j.value) {
												document
														.getElementById("pwerror").innerHTML = "<font color='ff0000'>Password does not match</font>";
														
												} 
											else 
											{
												document
														.getElementById("pwerror").innerHTML = "<font color='00aa00'>Password matches</font>";
												setRegisterState();
													}
											
										});
						$('#password')
						.keyup(
								function() {
									if (k.value != j.value) {
										document
												.getElementById("pwerror").innerHTML = "<font color='ff0000'>Password does not match</font>";
												
										} 
									else 
									{
										document
												.getElementById("pwerror").innerHTML = "<font color='00aa00'>Password matches</font>";
										setRegisterState();
													
											}
									
								});
					});

	function setRegisterState()
	{
		var  titleElement = document.getElementById("password").value;
		var k = document.getElementById("username").value;
		if(document.getElementById("username").value=="" ||document.getElementById("lname").value=="" || document.getElementById("fname").value=="")
		{
			$("#submit1").attr("disabled", true);
			return;
		}
		if (titleElement.length > 5 )		
	   		$("#submit1").attr("disabled", false);
		else
			$("#submit1").attr("disabled", true);
	}
	
	function validateForm() {
		var k = document.getElementById("password");
		var j = document.getElementById("cpass");
		var un = setMissing("username");
		var name = setMissing("fname");
		var name = setMissing("name");
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
body 
{
		margin: 100px 40px 10px 70px;
		background-color: #6495ED;
}
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

#submit1{
	height:40px; 
	width:120px;
	font-weight:bold;
	color:white;
	font-family:arial;
	font-size:22px;
	border:5px solid #4472b9;
	border-radius:10px;"
}
#login-box {
	width: 400px;
	height: 400px;
	padding: 20px;
	margin: 100px auto;
	background: #dfe2f4;
	border-color: #4472b9;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 2px solid #4472b9;
}
td 
{
    padding-top: .5em;
    padding-bottom: .3em;
}
input[type=submit]:disabled { background: grey; }
input[type=submit]:enabled { background: #4472b9; }
</style>
</head>
<body onload='document.loginForm.username.focus();'>

	<h1 align="center"  style="font-family:arial;font-size:46px;color:blue;weight:bold; color:white">User registration</h1>

	<div id="login-box">

		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>


		<form:form method="post" action="${accountURL}/?${_csrf.parameterName}=${_csrf.token}"
			enctype="multipart/form-data">
			<table style="font-weight: bold;font-size:18px">
				<tr >
					<td><div id="username_miss"></div>User:</td>
					<td><input type='text' name='username' id='username'></td>
				</tr>
				<tr >
					<td><div id="name_miss"></div>First name:</td>
					<td><input type='text' name='fname' id='fname'></td>
				</tr>
				<tr >
					<td><div id="name_miss"></div>Last name:</td>
					<td><input type='text' name='lname' id='lname'></td>
				</tr>
				<tr >
 					<td><div id="password_miss"></div>Password:</td>
					<td><input type='password' name='password' id='password' /></td>
				</tr>
				<tr >
					<td>Confirm password:</td>
					<td><input type='password' id='cpass' /></td>
				</tr>
				<tr >
					<td>Choose picture:</td>
	 				<td><input type="file" name='file' id='file' /></td>
				</tr>

				<tr >
					<td colspan='2' align="left" height="80px" >
						<input name="submit1" value="Register" type="submit" id="submit1" disabled/>
					</td>
				</tr>
				</table>
		<div id="pwerror"></div>

<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
				
		</form:form>
	</div>

</body>
</html>