<%@page import="java.util.Collection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.technion.project.model.User"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.technion.project.model.EvacuationEvent"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.00 Transitional//EN" "http://www.w3.org/TR/html5/loose.dtd">

<%@page session="true"%>
<html>
<head>
<c:set var="baseURL" value="${pageContext.request.contextPath}" />
<c:set var="adminURL" value="${pageContext.request.contextPath}/admin" />
<c:set var="reportURL"
	value="${pageContext.request.contextPath}/reports" />
<c:set var="accountURL"
	value="${pageContext.request.contextPath}/accounts" />
<c:set var="evacuationURL"
	value="${pageContext.request.contextPath}/evacuation" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<script type="text/javascript">
	var ctx = "${pageContext.request.contextPath}";
	var accountCtx = ctx + "/accounts";
	var reportCtx = "${reportURL}"
	var evacuationURL = "${evacuationURL}"
	var csrfName = "${_csrf.parameterName}";
	var csrfValue = "${_csrf.token}";
	var currentUser = "${pageContext.request.userPrincipal.name}";
</script>
<script src="${baseURL}/JS/jquery-1.11.1.min.js"></script>
<script src="${baseURL}/JS/menu.js"></script>
<script src="${baseURL}/JS/evacuation.js"></script>
<script type="text/javascript">

</script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<script>
	$.ajax("${baseURL}/menu").done(function(result) {
		$("#menu").html(result);
	}).error(function(res) {
		alert(res);
	});
</script>
</head>
<body>


	<div id="menu"></div>
	<%
		Boolean isAdmin = (Boolean) request.getAttribute("adminRight");
		EvacuationEvent event = (EvacuationEvent) request
				.getAttribute("evacuation");
		long id = (Long) request.getAttribute("id");
	%>


	<table id="details" class="zebra" height="200">
		<tr>
			<th>time</th>
			<td><%=new SimpleDateFormat("dd/MMM/yy - hh:mm").format(event
					.getEstimated())%></td>
		</tr>
		<tr>
			<th>total seats</th>
			<td><%=event.getCapacity()%></td>
		</tr>
		<tr>
			<th>seats left</th>
			<td><%=event.getAmountLeft()%></td>
		</tr>
		<tr>
			<th>means of evacuation</th>
			<td><%=event.getMeans()%></td>
		</tr>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<tr>
				<th>Delete this event</th>
				<td><button onclick="deleteEvent(<%=id%>)">Delete</button></td>
			</tr>
		</sec:authorize>
		<%
			for (User user : event.getRegisteredUsers()) {
		%>
		<tr>
			<th>name</th>
			<td id="<%=user.getUsername()%>"><a href='${accountURL}/<%=user.getUsername()%>/reports'><%=user.getFname() + " " + user.getLname()%></a></td>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
				<td><a onClick="leaveUser(<%=id%>,'<%=user.getUsername()%>')"><img
						src="${baseURL}/IMG/X.png" height="64" width="64"></a></td>
			</sec:authorize>
		</tr>
		<%
			}
		%>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<tr>
				<td>add</td>
				<td><input list="searchUser" id="newUser"> <datalist
						id="searchUser"> <%
 	Collection<User> users = (Collection<User>) request
 				.getAttribute("users");
 		users.removeAll(event.getRegisteredUsers());
 		for (User user : users) {
 %>
					<option value="<%=user.getUsername()%>"><%=user.getFname() + " " + user.getLname()%></option>
					<%
						}
					%> </datalist>
					<button onClick="addUser(<%=id%>,newUser.value)">add</button></td>
			</tr>
		</sec:authorize>
		<sec:authorize ifNotGranted="hasRole('ROLE_ADMIN')">
			<%
				String action = (((Boolean) request
							.getAttribute("userRegistered")) ? "un" : "")
							+ "registerToEvent(" + request.getAttribute("id") + ")";
					String actionName = ((Boolean) request
							.getAttribute("userRegistered")) ? "Leave" : "Join";
			%>
			<tr>
				<th><%=actionName%></th>
				<td><input type="button" onclick='<%=action%>'
					value='<%=actionName%>'></td>
			</tr>
		</sec:authorize>
	</table>
</body>
</html>