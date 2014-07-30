<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<style>
html, body, #map-canvas {
	height: 100%;
	margin: 0px;
	padding: 0px
}
</style>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
<c:set var="baseURL" value="${pageContext.request.contextPath}"/> 
<c:set var="adminURL" value="${pageContext.request.contextPath}/admin"/> 
<c:set var="reportURL" value="${pageContext.request.contextPath}/reports"/> 
<c:set var="accountURL" value="${pageContext.request.contextPath}/accounts"/>
<c:set var="eventURL" value="${pageContext.request.contextPath}/evacuation"/>  

<script src="${baseURL}/JS/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="<c:url value="/JS/addReport.js"/>"></script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<script src="${baseURL}/JS/menu.js"></script>
<script>
$.ajax("${baseURL}/menu").done(function(result) {
	$("#menu").html(result);
}).error(function(res){
	alert(res);
});
</script>
</head>
<body>
<div id="menu"></div>
	<form action="${eventURL}/?${_csrf.parameterName}=${_csrf.token}" method="post">
		<input name='geolng' type='hidden' id='lon'> <input
			name='geolat' type='hidden' id='lat'>
		<table align="left">
			<tr>
				<td>Means of evacuation:</td>
				<td><input type='text' name='means'></td>
			</tr>
			<tr>
				<td>capacity:</td>
				<td><input type="text" name="capacity"></input></td>
			</tr>
			<tr>
				<td colspan='2'><input name="submit" type="submit"
					value="submit" disabled="disabled" id='submit' /></td>
			</tr>

		</table>
	</form>
	<div id="status">locating Geo location</div>
	<div id="map-canvas"></div>
</body>
</html>