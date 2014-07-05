<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.00 Transitional//EN" "http://www.w3.org/TR/html5/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Reports</title>
</head>
<body>

	<c:choose>
		<c:when test="${pageContext.request.userPrincipal.name!=null}">
			<h2>
				User : ${pageContext.request.userPrincipal.name} | <a
					href="javascript:formSubmit()"> Logout</a> | <a href="addReport">add
					report</a>
			</h2>

		</c:when>

		<c:otherwise>
			<a href="../login">Login</a>
		</c:otherwise>
	</c:choose>

	<table border=1>
		<tr>
			<th>Title</th>
			<th>content</th>
			<th>user name</th>
			<th>lat</th>
			<th>lon</th>
		</tr>
		<c:forEach var="r" items="${reports}">
			<tr>
				<td>${r.title}</td>
				<td>${r.content}</td>
				<td>${r.username}</td>
				<td>${r.geolat}</td>
				<td>${r.geolng}</td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>