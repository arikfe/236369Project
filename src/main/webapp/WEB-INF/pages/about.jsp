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
<body style="background: linear-gradient(#000055, #0000aa)">

	<p align="center"
		style="font-family: Times, serif; color: black; font-size: 35px; font-weight: 900;">
		Crowdsourcing Application for Evacuation from</br> a Disaster Area
	</p>
	<p style="font-family: arial; color: #cecece; font-size: 20px;"
		align="center">
		</br> <strong>Usage:</strong> a web application for managing location-based
		reports related to a disaster area.</br> This system support leveraging the
		crowd for collecting information about the disaster area.</br> Also,
		facilitate publishing information about evacuation events, where and
		when people</br> can be evacuated from the area. </br>
		</br> Users all over the world provide information about hazards they
		encounter in specific locations, </br> by sending geo-tagged reports –
		reports that a location is attached to them.</br> Rescue team can insert
		evacuation events. The evacuation events associated with a specific
		location,</br> planned evacuation time and the maximal number of people
		that can be evacuated at this time. </br>
		</br> <strong>Advanced features:</strong> All reports can be export to file
		in XML & KML format.</br> Also, user can search events with Keyword-search.</br>
		</br> <strong>Technology:</strong> Login to system secured. Your password
		is stored in MySQL database after encryption.</br> Nobody else except the
		you can access or modify it. </br>
		</br>
	<p style="font-family: arial; color: green; font-size: 20px;"
		align="center">
		<strong>For any suggestion or issue, please contact us.</strong>
	</p>
	<p style="font-family: arial; color: gray; font-size: 18px;"
		align="center">
		<em>
			<p style="font-family: arial; color: black; font-size: 20px;"
				align="center">
				<strong>Arik Felznshtien: </strong><a
					href="mailto:4ortik@gmail.com?subject=Mail%20from%20user%20">4ortik@gmail.com</a>
			</p>
			<p style="font-family: arial; color: black; font-size: 20px;"
				align="center">
				<strong>Hagit Cohen: </strong><a
					href="mailto:hagitc999@gmail.com?subject=Mail%20from%20user%20">hagitc999@gmail.com</a>
			</p>
	</p>
	</p>
</html>