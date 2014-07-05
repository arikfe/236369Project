<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.js"></script>
<script type="text/javascript">
	function getLocation() {
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(showPosition);
		} else {
			alert("Geolocation is not supported by this browser.");
		}
	}
	function showPosition(position) {
		lat = document.getElementById("lat");
		lon = document.getElementById("lon");
		lat.value = position.coords.latitude;
		lon.value = position.coords.longitude;
		$("#submit").removeAttr("disabled");;
		$("#status").empty();
		
	}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add report</title>
</head>
<body onload="javascript:getLocation()">
	<sec:authorize access="hasRole('ROLE_USER')">
		<c:if test="${pageContext.request.userPrincipal.name != null}">
			<h2>
				User : ${pageContext.request.userPrincipal.name} | <a
					href="javascript:formSubmit()"> Logout</a>
			</h2>
			<form name='add' action="<c:url value='add' />" method="get">
				<input type='hidden' name='username'
					value=${pageContext.request.userPrincipal.name}>
				<input name='geolng' type='hidden' id='lon'> 
				<input name='geolat' type='hidden' id='lat'>
				<table>
					<tr>
						<td>Title:</td>
						<td><input type='text' name='title'></td>
					</tr>
					<tr>
						<td>content:</td>
						<td><textarea rows="4" cols="50" name="content"></textarea></td>
					</tr>
					<tr>
						<td colspan='2'><input name="submit" type="submit" 
							value="submit" disabled="disabled" id='submit'/></td>
					</tr>

				</table>
			</form>
			<div id="status">locating Geo location</div>

		</c:if>
	</sec:authorize>
</body>
</html>