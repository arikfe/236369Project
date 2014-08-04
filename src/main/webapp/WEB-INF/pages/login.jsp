<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<head>
<title>Login Page</title>
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
	border-color: #a6aee0;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31418f;
	background-color: #dfe2f4;
	border-color: #a6aee0;
}

#register
{
  	height:40px;
  	background:#4472b9; 
  	width:120px;
  	font-weight:bold;
  	color:white;
  	font-family:arial;
  	font-size:22px;
  	border:5px solid #4472b9;
  	border-radius:10px;
}
#login-box {
	width: 400px;
	padding: 20px;
	margin: 100px auto;
	background: #dfe2f4;
	border-color: #4472b9;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 2px solid #4472b9;
}
</style>
</head>
<body onload='document.loginForm.username.focus();' >

	<h1 align="center"  style="font-family:arial;font-size:46px;color:blue;weight:bold; color:white">Login Form</h1>

	<div id="login-box">

		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>

		<form name='loginForm'
			action="<c:url value='/login' />" method='POST'>
			<table>
				<tr style="font-weight: bold;font-size:18px">
					<td >User:</td>
					<td><input type='text' name='username'></td>
				</tr >
				<tr style="font-weight: bold;font-size:18px">
					<td >Password:</td>
					<td><input type='password' name='password' /></td>
				</tr>
				<tr >
					<td colspan='2' align="left" height="80px" ><input name="submit" type="submit" id="submint"
						value="Login" style="height:40px;background:#4472b9; width:120px;font-weight:bold;color:white;font-family:arial;font-size:22px;border:5px solid #4472b9;border-radius:10px "/></td>
				</tr>
				<tr>
				<td colspan='2'>Not a Member Yet? Registering is free and only takes few seconds.</td>
				</tr>
				<tr >
					<td colspan='2' align="left" height="80px" >
						<a href="${pageContext.request.contextPath}/register">
   							<input type="button" value="Register" id="register"/>
   						</a>
					 </td>
				</tr>
				
			</table>
				
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />

		</form>
	</div>

</body>
</html>