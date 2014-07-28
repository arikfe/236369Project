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
<style>
html, body, #map-canvas {
	height: 100%;
	margin: 0px;
	padding: 0px
}
</style>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="<c:url value="/JS/addReport.js"/>"></script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<script src="${pageContext.request.contextPath}/JS/menu.js"></script>
<c:set var="baseURL" value="${pageContext.request.contextPath}" />
<script>
	$.ajax("${baseURL}/menu").done(function(result) {
		$("#menu").html(result);
	}).error(function(res) {
		alert(res);
	});
</script>
<title>Add report</title>
</head>
<body>
	<sec:authorize access="hasRole('ROLE_USER')">
		<c:if test="${pageContext.request.userPrincipal.name != null}">
			<div id="menu"></div>

			<form name='add' action="?${_csrf.parameterName}=${_csrf.token}"
				method="POST" enctype="multipart/form-data">
				<input type='hidden' name='username'
					value=${pageContext.request.userPrincipal.name}> <input
					name='geolng' type='hidden' id='lon'> <input name='geolat'
					type='hidden' id='lat'>
				<table align="left">
					<tr>
						<td>Title:</td>
						<td><input type='text' name='title'></td>
					</tr>
					<tr>
						<td>content:</td>
						<td><textarea rows="4" cols="50" name="content"></textarea></td>
					</tr>
					<tr>
						<td>choose picture:</td>
						<td><input type="file" name='file' id='file' /></td>
					</tr>
					<tr>
						<td colspan='2'><input name="submit" type="submit"
							value="submit" disabled="disabled" id='submit' /></td>
					</tr>
				</table>
			</form>
			<div id="status">locating Geo location</div>
			<div id="map-canvas"></div>

		</c:if>
	</sec:authorize>
</body>
</html>