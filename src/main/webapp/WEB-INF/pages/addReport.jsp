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
html,body,#map-canvas {
	height: 100%;
	margin: 0px;
	padding: 10px
	background-color: #6495ED;
}

textarea#content {
	width: 250px;
	height: 50px;
	border: 3px solid #cccccc;
	padding: 5px;
	font-family: Tahoma, sans-serif;
	background-position: bottom right;
	background-repeat: no-repeat;
	resize: none;
}
input#title ,#file, #expiration, #address{
	width: 250px;
	height: 30px;
	border: 3px solid #cccccc;
	padding: 5px;
	font-family: Tahoma, sans-serif;
	background-position: bottom right;
	background-repeat: no-repeat;
	background: white;
}

#submit,#cancel {
    background-color: blue;
    -moz-border-radius: 5px;
    -webkit-border-radius: 5px;
    border-radius:6px;
    color: white;
    font-family: 'Oswald';
    font-size: 24px;
    text-decoration: none;
    cursor: poiner;
     border:none;
     height: 50px;
     width: 100px;
     text-align: justify;
}
td 
{
    padding-top: .5em;
    padding-bottom: .3em;
}


#submit:hover {
    border: none;
    background:green;
    box-shadow: 0px 0px 1px #777;
}

#cancel:hover {
    border: none;
    background:red;
    paddig-left:10px;
    box-shadow: 0px 0px 1px #777;
}
#map-canvas {
        width: 750px;
        height: 600px;
      }

</style>

<c:set var="baseURL" value="${pageContext.request.contextPath}" />
<c:set var="adminURL" value="${pageContext.request.contextPath}/admin" />
<c:set var="reportURL"
	value="${pageContext.request.contextPath}/reports" />

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css"></link>


<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>



<script src="${baseURL}/JS/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="<c:url value="/JS/addReport.js"/>"></script>

<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<script src="${baseURL}/JS/menu.js"></script>
<script>
	$.ajax("${baseURL}/menu").done(function(result) {
		$("#menu").html(result);
	}).error(function(res) {
		alert(res);
	});
	google.maps.event.addDomListener(window, 'load', initialize);
	
</script>
<script>
	if (self != top) top.location.replace(self.location.href);
</script>
<title>Add report</title>


</head>
<body >
	<sec:authorize access="hasRole('ROLE_USER')">
		<c:if test="${pageContext.request.userPrincipal.name != null}">
			<div id="menu"></div>
			<form name='add'
				action="${reportURL}/?${_csrf.parameterName}=${_csrf.token}"
				method="POST" enctype="multipart/form-data">
				<input type='hidden' name='username'
					value="${pageContext.request.userPrincipal.name}"> <input
					name='geolng' type='hidden' id='lon'> <input name='geolat'
					type='hidden' id='lat'>
					
				<table align="left" style="width:450px;">
					<tr >
						<td width="150px">Title:</td>
						<td width= "250px"><input type='text' name='title' id='title'></td>
					</tr>
					<tr>
						<td width="150px">Content:</td>
						<td><textarea id="content" rows="4" cols="50" name="content"></textarea></td>
					</tr>
					<tr>
						<td width="150px">address:</td>
						<td width= "250px"><input type='text' name='address' id='address'></td>
					</tr>
					<tr>
					<td width="150px">Expire time:</td>
						<td >
							<div id="datetimepicker" >
								<input type="datetime-local" name="expiration">
							</div>

						</td>
					</tr>
					<tr>
						<td width="150px">Choose picture:</td>
						<td width= "250px"><input type="file" name='file' id='file' /></td>
					</tr>
					<tr>
						<td><input name="submit" type="submit"
							value="submit" disabled="disabled" id='submit' /></td>
						<td><input name="cancel" type="submit"
							value="cancel" id='cancel' /></td>
					</tr>
				</table>
			</form >
			
				<div id="map-canvas"></div>
			
			
		</c:if>
	</sec:authorize>
</body>
</html>