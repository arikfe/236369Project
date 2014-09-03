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
<script type="text/javascript" src="<c:url value="/JS/addReport.js"/>"></script>

<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/addReport.css"/>"></link>
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css">
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
<script type="text/javascript" src="<c:url value="/JS/addReport.js"/>"></script>

<script src="${baseURL}/JS/menu.js"></script>
<script>
	$(function() {
		$("#number").selectmenu().selectmenu("menuWidget").addClass("overflow");
	});
	$.ajax("${baseURL}/menu").done(function(result) {
		$("#menu").html(result);
	}).error(function(res) {
		alert(res);
	});
	google.maps.event.addDomListener(window, 'load', initialize);
</script>
<script>
	if (self != top)
		top.location.replace(self.location.href);
</script>
<title>Add report</title>


</head>
<body>
	<div id="menu"></div>
	<sec:authorize access="hasRole('ROLE_USER')">
		<c:if test="${pageContext.request.userPrincipal.name != null}">
			<div id=container>
				<h1 align="center">Add Report Form</h1>
				<div id="form">
					<form name='add'
						action='${reportURL}/?${_csrf.parameterName}=${_csrf.token}'
						method="POST" enctype="multipart/form-data"
						onsubmit='return sumbitForm()'>
						<input type='hidden' name='username'
							value="${pageContext.request.userPrincipal.name}"> <input
							name='geolng' type='hidden' id='lon'> <input
							name='geolat' type='hidden' id='lat'>

						<table >
							<tr>
								<th >Title:</td>
								<td ><input type='text' name='title' id='title'></td>
							</tr>
							<tr>
								<th >Content:</td>
								<td ><textarea id="content" rows="4" cols="40"
										name="content"></textarea></td>
							</tr>
							<tr>
								<th ">address:</td>
								<td ><input type='text' name='address'
									id='address'></td>
							</tr>
							<tr>
								<th >Expire time:</td>
								<td ><input type="text" name="expiration"
									id="expiration"></td>
							</tr>
							<tr>
								<th >Time:</td>
								<td >
									<fieldset style="width: 200px; align: center;">
										<select name="number" id="number" style="width: 150px;">
											<option>1:00</option>
											<option>5:00</option>
											<option>9:00</option>
											<option selected="selected">13:00</option>
											<option>17:00</option>
											<option>21:00</option>
										</select>
									</fieldset>
								</td>
							</tr>
							<tr>
								<th >Choose picture:</td>
								<td><input type="file" name='file' id='file' /></td>
							</tr>
							<tr>
								<td  align="center"><input name="submit"
									type="submit" value="submit" disabled="disabled" id='submit' /></td>

							</tr>
						</table>
					</form>

				</div>
				<div id="content">
					<div id="map-canvas"></div>
				</div>
			</div>
			<div id="footer">Technion CS 236369</div>

		</c:if>
	</sec:authorize>
</body>
</html>
