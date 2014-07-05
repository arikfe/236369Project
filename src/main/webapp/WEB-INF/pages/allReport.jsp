<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.00 Transitional//EN" "http://www.w3.org/TR/html5/loose.dtd">
<html>
<head>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/table.css"/>"></link>
<link type="text/css" rel="stylesheet"
	href="<c:url value="/CSS/dropDownMenu.css"/>"></link>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
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
					<script>
						function formSubmit() {
							document.getElementById("logoutForm").submit();
						}
					</script>
					<li><a href="#">${pageContext.request.userPrincipal.name}</a>

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


	<table class="zebra">
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