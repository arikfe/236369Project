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

<script src="${baseURL}/JS/jquery-1.11.1.min.js"></script>

<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
	<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
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
<div id='menu'></div>
	<table class='zebra'>
		<tr class='zebra'>
			<td class='zebra'>submitters</td>
			<td >Ariel Felzenstien <br>039948765 <br>4ortik@gmail.com
			</td>
			<td >Hagit Cohen <br> <br>hagitc@gmail.com
			</td>
		</tr>
		<tr class='zebra'>
			<td class='zebra'>Advanced features</td>
			<td >keyword search web service</td>
			<td >visible <a href="${baseURL }/search/">here</a></td>
		</tr>
		<tr class='zebra'>
			<td></td>
			<td class='zebra'>Produce XML storage of the reports and evacuation events. <br>Transform
				the messages to KML format ,<br> using XSLT and support the
				presentation of KML on a map
			</td>
			<td >visible using Admin user from the menu</td>
		</tr>
		<tr class='zebra'>
			<td> DB management system</td>
			<td >MySql 6.1</td><td></td>
		</tr>

	</table>
</body>
</html>