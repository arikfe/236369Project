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
	padding: 10px
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
<c:set var="baseURL" value="${pageContext.request.contextPath}"/> 
<c:set var="adminURL" value="${pageContext.request.contextPath}/admin"/> 
<c:set var="reportURL" value="${pageContext.request.contextPath}/reports"/> 
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>

<script src="${baseURL}/JS/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="<c:url value="/JS/addReport.js"/>"></script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<script src="${pageContext.request.contextPath}/JS/menu.js"></script>
<script>
	$.ajax("${baseURL}/menu").done(function(result) {
		$("#menu").html(result);
	}).error(function(res) {
		alert(res);
	});
</script>
<title>Add report</title>
<script type="text/javascript"
     src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.8.3/jquery.min.js">
    </script> 
    <script type="text/javascript"
     src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.2.2/js/bootstrap.min.js">
    </script>
    <script type="text/javascript"
     src="http://tarruda.github.com/bootstrap-datetimepicker/assets/js/bootstrap-datetimepicker.min.js">
    </script>
    <script type="text/javascript"
     src="http://tarruda.github.com/bootstrap-datetimepicker/assets/js/bootstrap-datetimepicker.pt-BR.js">
    </script>
    <script type="text/javascript">
      $('#datetimepicker').datetimepicker({
        format: 'dd/MM/yyyy hh:mm:ss',
        language: 'pt-BR'
      });
    </script>
</head>
<body style="background:"white">
	<sec:authorize access="hasRole('ROLE_USER')">
		<c:if test="${pageContext.request.userPrincipal.name != null}">
			<div id="menu"></div>

			<form name='add' action="${reportURL}/?${_csrf.parameterName}=${_csrf.token}"
				method="POST" enctype="multipart/form-data">
				<input type='hidden' name='username'
					value=${pageContext.request.userPrincipal.name}> <input
					name='geolng' type='hidden' id='lon'> <input name='geolat'
					type='hidden' id='lat'>
					<table width="900px" CELLPADDING="10">
					<caption height="100px"  style="font-family:Times New Roman;color:blue;font-size:40px;align=center" >Add Report</caption>
					<tr height="500px" bordercolor="black">
					<td width="300px">
					<div style="font-family:Times New Roman;color:blue;font-size:20px;">
							Name:<br>
							<textarea name='title' rows="2" cols="40"  style="color:black; font-size:16px; background-color: #D1D0CE;resize:none;">
							</textarea><br>
							Details:<br> 
							<textarea name="content" rows="8" cols="40"  style="color: black; font-size:16px; background-color: #D1D0CE ;resize:none;">
							</textarea><br>
							expiration time<br>
						
							<div id="datetimepicker" class="input-append date">
								<input type="text" name="expiration"></input> <span class="add-on"> <i
									data-time-icon="icon-time" data-date-icon="icon-calendar"></i>
								</span>
							</div>
								<br>
	
							Choose picture:<br><br>
							<input type="file" name='file' id='file' style="color: red; background-color: lightyellow"/>
						
					</div>
			</td>
			<td width="800px" paddind="5px">
				<div>
					<div id="map-canvas" style="height: 500px ;width:800px"></div>
				</div>
			</td>
			</tr>
			</table>
					<input name="submit" type="submit"
							value="Submit" disabled="disabled" id='submit' />
					<input name="cancel" type="button" style="background-color:#736F6E;"
							value="Cancel" id='cancel' />
		</form>
		</c:if>
	</sec:authorize>
</body>
</html>