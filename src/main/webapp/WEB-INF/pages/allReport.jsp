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
<c:set var="baseURL" value="${pageContext.request.contextPath}"/> 
<c:set var="adminURL" value="${pageContext.request.contextPath}/admin"/> 
<c:set var="reportURL" value="${pageContext.request.contextPath}/reports"/> 
<c:set var="accountURL" value="${pageContext.request.contextPath}/accounts"/>
<c:set var="evacuationURL" value="${pageContext.request.contextPath}/evacuation"/>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="${baseURL}/JS/menu.js"></script>
<script src="${baseURL}/JS/allReports.js"></script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<style>
#map-canvas {
	width: 50%;
	height: 600px;
}

html, body {
	height: 100%;
	margin: 0px;
	padding: 0px
}

#panel {
	position: absolute;
	top: 5px;
	left: 50%;
	margin-left: -180px;
	z-index: 5;
	background-color: #fff;
	padding: 5px;
	border: 1px solid #999;
}
</style>

<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
<script>
var ctx = "${pageContext.request.contextPath}";
var csrfName = "${_csrf.parameterName}";
var csrfValue = "${_csrf.token}";
var currentUser = "${pageContext.request.userPrincipal.name}";
function initialize() {
	
	$.ajax("${baseURL}/menu").done(function(result) {
		$("#menu").html(result);
	}).error(function(res){
		alert(res);
	});

image = {
	url : '${baseURL}/IMG/car.png',
	// This marker is 20 pixels wide by 32 pixels tall.
	size : new google.maps.Size(32, 37),
	// The origin for this image is 0,0.
	origin : new google.maps.Point(0, 0),
	// The anchor for this image is the base of the flagpole at 0,32.
	anchor : new google.maps.Point(18, 32)
};

var haightAshbury = new google.maps.LatLng("${midLat}", "${midLng}");
var mapOptions = {
	zoom : 11,
	center : haightAshbury
};
map = new google.maps.Map(document.getElementById('map-canvas'),
		mapOptions);



var i=0;
var body = $("#table_body");
var msg;

$.ajax({
	type : "GET",
	url : "${reportURL}/",
	contentType : "application/json",
}).done(function(reports) {
	
	for ( var i in reports) {
		handleReportCreation(reports[i],'${pageContext.request.userPrincipal.name}',i);
	}
}).fail(function(err) {
	alert(err);
});


<c:forEach var="e" items="${events}">
	var userRegistered = "";
	var functionName = "registerToEvent";
	var actionName = "register";
	<c:if test="${not empty user.event}">
		userRegistered = "disabled";
	</c:if>
	
	if("${e.id}"=="${user.event.id}")
	{
		functionName = "un"+functionName;
		userRegistered = "";
		actionName = "unregister";
	}
	var totalcapacity = ${e.capacity}
			-${e.registeredUsers.size()};
	var contentStr = '<div id="friend${e.id}"><input type="button" class="styledButton" onclick="'+functionName+'(' + "${e.id}"
			+ ')" ' + userRegistered + 'value="'+actionName+'" />'
			+ '<input type="button" class="styledButton" value="show users" onclick="displayEventUsers(${e.id})"/>'
			+ '<p>capacity left: ' + totalcapacity + '</p>'
			+ '<p>evacuation time: ' + "${e.estimated}" +'<p><a href="${evacuationURL}/id/${e.id}">show Event</a>'+ '</p>';
	setTimeout(updateEvent(contentStr,'${e.geolat}','${e.geolng}','${e.id}'), 500 + (i++ * 200));

</c:forEach>

}

</script>

<title>Reports</title>
</head>
<body>

	<div id="menu"></div>
	<button id="showClose" onclick="bounceClosest()" >show closest evacuation event</button>
	<button onclick="stopEventBounce()">Stop bounce</button>
	<button onclick="bounceMine()">Find my Event</button>
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
	<table id='reportsTbody' class="zebra" align="left">

		<tbody >
			<tr id='table_body'>
				<th>Title</th>
				<th>content</th>
				<th>user name</th>

				<th>expire on</th>
				<th>delete</th>
			</tr>
		</tbody>
	</table>
	<div id="map-canvas" align="right"></div>


</body>
</html>