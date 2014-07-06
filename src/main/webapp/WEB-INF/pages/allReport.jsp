<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.00 Transitional//EN" "http://www.w3.org/TR/html5/loose.dtd">
<html>
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">

<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<style>
html, body, #map-canvas {
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
	var map;
	var markers = [];

	function initialize() {

		var haightAshbury = new google.maps.LatLng("${midLat}", "${midLng}");
		var mapOptions = {
			zoom : 12,
			center : haightAshbury
		};
		map = new google.maps.Map(document.getElementById('map-canvas'),
				mapOptions);

		// This event listener will call addMarker() when the map is clicked.
		

		// Adds a marker at the center of the map.
		<c:forEach var="r" items="${reports}">
		addMarker(new google.maps.LatLng(<c:out value="${r.geolat}"/>,
				<c:out value="${r.geolng}"/>),"${r.title}");
		</c:forEach>
	}

	// Add a marker to the map and push to the array.
	function addMarker(location,title) {
		var marker = new google.maps.Marker({
			position : location,
			map : map,
			title : title
		});
		markers.push(marker);
	}

	// Sets the map on all markers in the array.
	function setAllMap(map) {
		for (var i = 0; i < markers.length; i++) {
			markers[i].setMap(map);
		}
	}

	// Removes the markers from the map, but keeps them in the array.
	function clearMarkers() {
		setAllMap(null);
	}

	// Shows any markers currently in the array.
	function showMarkers() {
		setAllMap(map);
	}

	// Deletes all markers in the array by removing references to them.
	function deleteMarkers() {
		clearMarkers();
		markers = [];
	}

	google.maps.event.addDomListener(window, 'load', initialize);
</script>

<title>Reports</title>
</head>
<body>

	<nav>
	<ul>
		<c:choose>
			<c:when test="${pageContext.request.userPrincipal.name!=null}">
				<sec:authorize access="hasRole('ROLE_USER')">
					<c:url value="/logout" var="logoutUrl" />
					<form action="${logoutUrl}" method="post" id="logoutForm">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
					</form>
					<script type="text/javascript">
						function formSubmit() {
							document.getElementById("logoutForm").submit();
						}
					</script>
					<li><a href="#">${fname} ${lname}</a>

						<ul>
							<a href="javascript:formSubmit()"> Logout</a>
						</ul></li>
					<li><a href="addReport">add report</a></li>
				</sec:authorize>

			</c:when>

			<c:otherwise>
				<li><a href="../login">Login</a></li>

			</c:otherwise>
		</c:choose>
	</ul>
	</nav>


	<table class="zebra" align="left">
		<tr>
			<th>Title</th>
			<th>content</th>
			<th>user name</th>
			
			<th>exipre on</th>
		</tr>
		<c:forEach var="r" items="${reports}">
			<tr>
				<td>${r.title}</td>
				<td>${r.content}</td>
				<td>${r.username}</td>
				<td>${r.expiration}</td>
			</tr>
		</c:forEach>
	</table>
	<div id="map-canvas" align="right"></div>

	<!-- <div id="panel">
		<input onclick="clearMarkers();" type=button value="Hide Markers">
		<input onclick="showMarkers();" type=button value="Show All Markers">
		<input onclick="deleteMarkers();" type=button value="Delete Markers">
	</div> -->


</body>
</html>