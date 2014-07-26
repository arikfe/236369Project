<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.technion.project.model.User"%>
<%@ page import="com.technion.project.model.EvacuationEvent"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.00 Transitional//EN" "http://www.w3.org/TR/html5/loose.dtd">

<%@page session="true"%>
<html>
<head>
<c:set var="baseURL" value="${pageContext.request.contextPath}"/> 
<c:set var="adminURL" value="${pageContext.request.contextPath}/admin"/> 
<c:set var="reportURL" value="${pageContext.request.contextPath}/reports"/> 
<c:set var="accountURL" value="${pageContext.request.contextPath}/accounts"/>
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="${baseURL}/JS/menu.js"></script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<script>
	
	$.ajax("${accountURL}/menu").done(function(result) {
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
	<th>Evacuation</th>
	</tr>		
	<%
	
		List<User> users = (List<User>) request.getAttribute("users");

		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = iterator.next();
	%>
		<tr id='<%=user.getUsername() %>'>
		<td><img src="<%=user.hasContainImage() ? request.getContextPath()+"/download/"
							+ user.getImageId() : request.getContextPath()+"/IMG/images.jpg"%>" height="42" width="42"> </td>
		<td><%=user.getFname()%></td>
		<td><%=user.getUsername() %></td>
		<td><a href='${reportURL}/<%=user.getUsername() %>'>reports</a></td>
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