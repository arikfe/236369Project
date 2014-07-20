<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.technion.project.model.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.00 Transitional//EN" "http://www.w3.org/TR/html5/loose.dtd">
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>

</head>
<body>

	<nav>
	<ul>
		<%
			User user = (User) request.getAttribute("user");

			if (user != null) {
		%>
		<sec:authorize access="hasRole('ROLE_USER')">
			<c:url value="/logout" var="logoutUrl" />
			<form action="${logoutUrl}" method="post" id="logoutForm">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
			</form>

			<li><img
				src="<%=user.hasContainImage() ? "/236369Project/download/"
							+ user.getImageId() : "/236369Project/IMG/images.jpg"%>"
				align="middle" height="64" width="64"></li>
			<li><a href="#">${user.fname} ${user.lname}</a>
				<ul>
					<li><a href="javascript:formSubmit()"> Logout</a></li>
					<li><a href="/236369Project/accounts/own"> My Account</a></li>
					
				</ul></li>
			<li><a>Reports</a>
				<ul>
					<li><a href="../">All reports</a></li>
					<li><a href="addReport">add report</a></li>
					<li><a href="<%=user.getUsername()%>">My reports</a></li>
				</ul></li>
			<li><a href="/236369Project/accounts/users">Show all Users</a></li>
			<%
				if (user.hasAdminPrevilige()) {
			%>
			<li><a href='/236369Project/admin/addEventView'>Add evacuation</a></li>
			<%
				}
			%>
			<li> <input type="text" id="strField" name="str" onKeyUp="search(this)"/></li>
		</sec:authorize>

		<%
			} else {
		%>



		<li><a href="../login">Login</a></li>
		<li><a href="../register">register</a></li>
		<%
			}
		%>

	</ul>
	</nav>





</body>
</html>