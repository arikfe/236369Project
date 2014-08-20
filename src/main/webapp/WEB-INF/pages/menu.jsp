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
<style>

</style>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<c:set var="baseURL" value="${pageContext.request.contextPath}"/> 
<c:set var="adminURL" value="${pageContext.request.contextPath}/admin"/> 
<c:set var="reportURL" value="${pageContext.request.contextPath}/reports"/> 
<c:set var="accountURL" value="${pageContext.request.contextPath}/accounts"/>
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
				src="<%=user.hasContainImage() ? request.getContextPath()+"/download/"
							+ user.getImageId() : request.getContextPath()+"/IMG/images.jpg"%>"
				 height="64" width="64" >
			</li>
			<li><a href="#">${user.fname} ${user.lname}</a>
				<ul>
					<li><a href="javascript:formSubmit()"> Logout</a></li>
					<li><a href="${accountURL}/own"> My Account</a></li>
					
				</ul></li>
			<li><a>Reports</a>
				<ul>
					<li><a href="${reportURL}/">All reports</a></li>
					<li><a href="${baseURL}/addReport">Add report</a></li>
					<li><a href="${accountURL}/<%=user.getUsername()%>/reports">My reports</a></li>
				</ul>
				</li>
				<li><a>Save to file</a>
				<ul>
					<li><a href="${reportURL}/exportXml">Save XML</a></li>
					<li><a href="${reportURL}/exportKml">Save KML</a></li>
				</ul>
				</li>
			<li><a href="${accountURL}/">Show all Users</a></li>
			<%
				if (user.hasAdminPrevilige()) {
			%>
			<li><a href='${adminURL}/addEventView'>Add evacuation</a></li>
			<%
				}
			%>
			<li  style="padding-top: 5px"> <input type="text" id="strField" name="str" onKeyUp="search(this)"></li>
		</sec:authorize>

		<%
			} else {
		%>
		<li><a href="${baseURL}/login">Login</a></li>
		<li><a href="${baseURL}/register">register</a></li>
		<%
			}
		%>

	</ul>
	</nav>





</body>
</html>