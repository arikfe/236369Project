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

html,body {	
	height: 100%;
	margin: 0px;
	padding: 10px;
	background-color: #E5E5E5;
}

#map-canvas {
        width: 1000px;
        height: 700px;
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
tr{
	font-weight: bold;
	font-size:18px;
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
</style>

<c:set var="baseURL" value="${pageContext.request.contextPath}" />
<c:set var="adminURL" value="${pageContext.request.contextPath}/admin" />
<c:set var="reportURL"
	value="${pageContext.request.contextPath}/reports" />

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css"></link>


	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>




<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css">
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
	 <script>
	 $(function() {
		 $( "#expiration" ).datepicker();
		 });
</script>

<script>
  $(function() {
    $( "#number" )
      .selectmenu()
      .selectmenu( "menuWidget" )
        .addClass( "overflow" );
  });
  </script>
  
  
	<script type="text/javascript" src="<c:url value="/JS/addReport.js"/>"></script>

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
			<form name='add'
				action="${reportURL}/?${_csrf.parameterName}=${_csrf.token}"
				method="POST" enctype="multipart/form-data">
				<input type='hidden' name='username'
					value="${pageContext.request.userPrincipal.name}"> <input
					name='geolng' type='hidden' id='lon'> <input name='geolat'
					type='hidden' id='lat'>
					
	<div id="container" style="width:100%; height:610px;" >
		<h1 align="center"  style="font-family:arial;font-size:46px;color:blue;weight:bold; color:blue">Add Report Form</h1>
			<div id="menuTable" style="height:100%; width:30%;float:left;">
				<table >
					<tr >
						<td width="30%">Title:</td>
						<td width= "70%"><input type='text' name='title' id='title'></td>
					</tr>
					<tr>
						<td width="30%">Content:</td>
						<td width="70%"><textarea  id="content" rows="4" cols="40" name="content"></textarea></td>
					</tr>
					<tr>
						<td width="30%">address:</td>
						<td width="70%"><input type='text' name='address' id='address'></td>
					</tr>
					<tr>
					<td width="30%">Expire time:</td>
						<td  width="70%">
								<input type="text" name="expiration" id="expiration">
							
						</td>
					</tr>
					<tr>
						<td width="30%">Time:</td>
						<td width="70%">
							<fieldset style="width:200px ; align:center;">
						  	  <select name="number"  id="number" style="width:150px;">
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
						<td width="30%">Choose picture:</td>
						<td width="70%"><input type="file" name='file' id='file' /></td>
					</tr>
					<tr>
						<td width="30%" align="center"><input name="submit" type="submit"
							value="submit" disabled="disabled" id='submit'/></td>
						<td width="70%" align="left"><input name="cancel" type="submit"
							value="cancel" id='cancel' /></td>
					</tr>
				</table>
				</div>
			
			<div id="content" style="background-color:#EEEEEE;float:left;width:700px; height:700px;">
				<div id="map-canvas"></div>
			</div>

	</div>
			<div id="footer" style="background-color:#FFA500;clear:both;text-align:center;">
				Techion C236369</div>
</form >
		</c:if>
	</sec:authorize>
</body>
</html>
