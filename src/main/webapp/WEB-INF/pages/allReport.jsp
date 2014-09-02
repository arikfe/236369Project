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
	var midLat = '${midLat}';
	var midLng = '${midLng}';
	var adminCtx = ctx + "/admin";
	var csrfName = "${_csrf.parameterName}";
	var csrfValue = "${_csrf.token}";
	var currentUser = "${pageContext.request.userPrincipal.name}";
</script>
<script src="${baseURL}/JS/jquery-1.11.1.min.js"></script>
<script src="${baseURL}/JS/lightbox.min.js"></script>
<script src="${baseURL}/JS/menu.js"></script>
<script src="${baseURL}/JS/allReports.js"></script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/allReports.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/addReport.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/expendableList.css"/>"></link>
<link href="<c:url value="/CSS/lightbox.css"/>" rel="stylesheet" />

<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
<script>
	
</script>

<title>Reports</title>
</head>
<body>

	<div id="menu"></div>
	<c:if test="${not empty desiredUser}">

		<table>
			<tr>
				<th>Profile Pic</th>
				<td><img src="${baseURL}/download/${desiredUser.imageId}"
					align="middle" height="64" width="64"></td>
			</tr>
			<tr>
				<th>User name</th>
				<td>${desiredUser.username}</td>
			</tr>
			<tr>
				<th>First name</th>
				<td>${desiredUser.fname}</td>
			</tr>
			<tr>
				<th>Last name</th>
				<td>${desiredUser.lname}</td>
			</tr>
		</table>
		<br />
	</c:if>
	<button id="showClose" onclick="bounceClosest()">Closest
		evacuation event</button>
	<button onclick="stopEventBounce()">Stop bounce</button>
	<button onclick="bounceMine()">Find my Event</button>
	<div id='container'>
		<div id='linesContainer'>
			<h4>Reports</h4>
			<nav id="reports" class="list"> </nav>
			<h4>Evacuation events</h4>
			<nav id="events" class="list"> </nav>
		</div>
		<div id='mapContainer'>
			<div id="map-canvas" ></div>
		</div>
	</div>

</body>
</html>
