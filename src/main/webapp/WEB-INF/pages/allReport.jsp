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
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
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
	var map;
	var markers = [];
	var evacuations = [];
	var image;
	function deleteAccount(){
		$.ajax("../accounts/deleteself").always(function() {
			document.getElementById("logoutForm").submit();
		});
	}
	function deletePost(_id)
	{
		$.ajax({
			type : "GET",
			url : "delete",
			data : {
				id : _id
			}
		}).done(function(msg) {
			$("#row"+msg).remove();
		}).fail(function(err) {
			alert(err);
		});
	}
	function formSubmit() {
		document.getElementById("logoutForm").submit();
	}
	function bounceMarker(marker)
	{
		marker.setAnimation(google.maps.Animation.BOUNCE);
	}
	function stopBounce(marker)
	{
		marker.setAnimation(null);
	}
	function unregisterToEvent(_id) {
		$.ajax({
			type : "GET",
			url : "/project/evacuation/leave",
			data : {
				id : _id
			}
		}).done(function(msg) {
			//alert("Data Saved: " + msg);
		}).fail(function(err) {
			alert(err);
		});
	}
	function registerToEvent(_id) {
		$.ajax({
			type : "GET",
			url : "/project/evacuation/join",
			data : {
				id : _id
			}
		}).done(function(msg) {
			//alert("Data Saved: " + msg);
		}).fail(function(err) {
			alert(err);
		});
	}
	function initialize() {

		image = {
			url : '/project/IMG/car.png',
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
		<c:forEach var="r" items="${reports}">
			 setTimeout(function() {
				  addMarker(new google.maps.LatLng(<c:out value="${r.geolat}"/>,
							<c:out value="${r.geolng}"/>), "${r.title}",'${r.id}');
			    }, 500 + (i++ * 200));
			 msg = "<tr id='row${r.id}'><td>${r.title}</td><td>${r.content}</td><td><a href='${r.username}'>${r.username}</a></td><td>${r.expiration}</td>";
			 if('${pageContext.request.userPrincipal.name}'=='${r.username}')
				 msg += "<td><input type='button' value='Delete' onclick='deletePost(${r.id})'></td>";
			 msg +="</tr>";
			 body.after(msg);
		 </c:forEach>
		
		
		<c:forEach var="e" items="${events}">
			var userRegistered = "";
			var functionName = "registerToEvent";
			var actionName = "register";
			<c:if test="${not empty user.event}">
			userRegistered = "disabled";
			</c:if>
			
			var id = "${e.id}";
			var userEventId = "${user.event.id}";
			if(id==userEventId)
			{
				functionName = "un"+functionName;
				userRegistered = "";
				actionName = "unregister";
			}
			var totalcapacity = ${e.capacity}
					-${e.registeredUsers.size()};
			var contentStr = '<button onclick="'+functionName+'(' + "${e.id}"
					+ ')" ' + userRegistered + '>'+actionName+'</button>'
					+ '<p>capacity left: ' + totalcapacity + '</p>'
					+ '<p>evacuation time: ' + "${e.estimated}" + '</p>';
			setTimeout(updateEvent(contentStr,'${e.geolat}','${e.geolng}'), 500 + (i++ * 200));

		</c:forEach>

	}
	function updateEvent(contentStr,lat,lon)
	{
			addEvacuation(new google.maps.LatLng(lat,
					lon), contentStr);
		
	}
	// Add a marker to the map and push to the array.
	function addMarker(location, title,id) {
		var marker = new google.maps.Marker({
			position : location,
			map : map,
			title : title,
			animation : google.maps.Animation.DROP
		});
		markers.push(marker);
		var row = $("#row"+id);
		row.hover(function(){
			bounceMarker(marker);
			},function(){
				stopBounce(marker);
				});
		row.click(function(){
			panTo(marker);
			});
	}
	function panTo(marker){
		map.panTo(marker.position);
	}
	function addEvacuation(location, contentStr) {

		var infowindow = new google.maps.InfoWindow({
			content : contentStr
		});
		var marker = new google.maps.Marker({
			position : location,
			map : map,
			title : 'event',
			icon : image,
			animation : google.maps.Animation.DROP
		});
		google.maps.event.addListener(marker, 'click', function() {
			infowindow.open(map, marker);
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

	$(document).ready(function(){
		google.maps.event.addDomListener(window, 'load', initialize);
	
	});
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
					
					<li><a href="#">${user.fname} ${user.lname}</a>
						<ul>
						<li>
							<a href="javascript:formSubmit()"> Logout</a>
							</li>
							<li>
							<a href="javascript:deleteAccount()"> Delete Account</a>
							</li>
						</ul>
						
					</li>
					<li>
						<a>Reports</a>
						<ul>
							<li><a href="addReport">add report</a></li>
							<li><a href="${user.username}">My reports</a></li>
						</ul>
					</li>
					<li><a href="../accounts/users">Show all Users</a></li>
				</sec:authorize>

			</c:when>

			<c:otherwise>
				<li><a href="../login">Login</a></li>
				<li><a href="../register">register</a></li>

			</c:otherwise>
		</c:choose>
	</ul>
	</nav>


	<table class="zebra" align="left">

		<tbody >
			<tr id='table_body'>
				<th>Title</th>
				<th>content</th>
				<th>user name</th>

				<th>expire on</th>
			</tr>
		</tbody>
	</table>
	<div id="map-canvas" align="right"></div>


</body>
</html>