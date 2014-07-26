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
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="${baseURL}/JS/menu.js"></script>
<script type="text/javascript">
function addUser(eid,name){
	$.ajax({
		type : "GET",
		url : "${evacuationURL}/joinUser",
		data : {
			id: eid,
			username : name
		}
	}).done(function(data) {
		
	}).fail(function(err) {
		alert(err);
	});
}
function leaveUser(_id,name){
	$.ajax({
		type : "GET",
		url : "${evacuationURL}/leaveUser",
		data : {
			id: _id,
			username : name
		}
	}).done(function(data) {
		$("#"+name).remove();
	}).fail(function(err) {
		alert(err);
	});
}
</script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<script>
	$.ajax("${accountURL}/menu").done(function(result) {
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
		long id =  (Long)request.getAttribute("id");
	%>


	<table id="details" class="zebra">
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
		<%
		for(User user : event.getRegisteredUsers())
		{		
			%>
			<tr>
			<th>name</th>
			<td id="<%=user.getUsername() %>"><%=user.getFname() + " " + user.getLname()%></td>
			
			<td><a onClick="leaveUser(<%=id%>,'<%=user.getUsername()%>')"><img src="${baseURL}/IMG/X.png"  height="64" width="64"></a></td>
		</tr>
			<%
		}
			%>
		<tr>
		<td>add</td>
		<td><input list="searchUser" id="newUser">
		<datalist id="searchUser">
		<%
		Collection<User> users =(Collection<User>)request.getAttribute("users");
		users.removeAll(event.getRegisteredUsers());
		for(User user : users)
		{
			%>
			 <option value="<%=user.getUsername() %>"><%=user.getFname()+" "+user.getLname() %></option>
			<%
		}
		%>
		</datalist>
		<button onClick="addUser(<%=id%>,newUser.value)">add</button>
		</td>
		</tr>
	</table>
</body>
</html>