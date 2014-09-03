<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">

<c:set var="baseURL" value="${pageContext.request.contextPath}" />
<c:set var="adminURL" value="${pageContext.request.contextPath}/admin" />
<c:set var="reportURL"
	value="${pageContext.request.contextPath}/reports" />

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css"></link>


<script type="text/javascript"
	src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
<script src="${baseURL}/JS/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="<c:url value="/JS/addEvent.js"/>"></script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/containers.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/addReport.css"/>"></link>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css">
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
<c:set var="baseURL" value="${pageContext.request.contextPath}" />
<c:set var="adminURL" value="${pageContext.request.contextPath}/admin" />
<c:set var="reportURL"
	value="${pageContext.request.contextPath}/reports" />
<c:set var="accountURL"
	value="${pageContext.request.contextPath}/accounts" />
<c:set var="evacuationURL"
	value="${pageContext.request.contextPath}/evacuation" />
<script type="text/javascript">
	var ctx = "${pageContext.request.contextPath}";
	var accountCtx = ctx + "/accounts";
	var adminCtx = ctx + "/admin";
	var csrfName = "${_csrf.parameterName}";
	var csrfValue = "${_csrf.token}";
	var currentUser = "${pageContext.request.userPrincipal.name}";
</script>

<script src="${baseURL}/JS/jquery-1.11.1.min.js"></script>
<%-- <script type="text/javascript" src="<c:url value="/JS/addReport.js"/>"></script> --%>

<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<script src="${baseURL}/JS/menu.js"></script>
<script src="${baseURL}/JS/addEvent.js"></script>
<script>
	$.ajax("${baseURL}/menu").done(function(result) {
		$("#menu").html(result);
	}).error(function(res) {
		alert(res);
	});
	google.maps.event.addDomListener(window, 'load', initialize);
</script>
</head>
<body>
	<div id="menu"></div>
	<div class='mainContainer'>
		<div class='formContainer'>
			<form action="${evacuationURL}/?${_csrf.parameterName}=${_csrf.token}"
				method="post">
				<input name='geolng' type='hidden' id='lon'> <input
					name='geolat' type='hidden' id='lat'>
				<table align="left">
					<tr>
						<td>Means of evacuation:</td>
						<td><input class='form' type='text' name='means'></td>
					</tr>
					<tr>
						<td>address:</td>
						<td><input class='form' type='text' name='address' id='address'></td>
					</tr>
					<tr>
						<td>capacity:</td>
						<td><input class='form' type="text" name="capacity"></input></td>
					</tr>
					<tr>
						<td colspan='2'><input name="submit" type="submit"
							value="submit" disabled="disabled" id='submit' /></td>
					</tr>

				</table>
			</form>
		</div>
		<div class='mapContainer'>
			<div id="status">locating Geo location</div>
			<div id="map-canvas"></div>
		</div>
	</div>
</body>
</html>