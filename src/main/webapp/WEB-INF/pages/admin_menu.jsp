<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<head>

<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
<script type="text/javascript" src="<c:url value="/JS/addReport.js"/>"></script>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>

</head>
<body>

	<nav>
		<ul>
			<li><a href="#"><font color="#000000">${fname} </font></a>

				<ul>
					<a href="javascript:formSubmit()"> Logout</a>
				</ul></li>
			<li><a href='admin/users' target='main'>Users management</a></li>
			<li><a href='admin/addEventView' target='main'>Add evacuation</a></li>
		</ul>
	</nav>
	


</body>
</html>