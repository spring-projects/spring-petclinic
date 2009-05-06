<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ page session="false" %>
<html>
<head>
	<title><tiles:insertAttribute name="title"/></title>
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles/main.css"/>
</head>
<body id="page-body">
	<div id="page">
		<div id="header">
			<ul id="signin">
				<c:choose>
					<c:when test="${pageContext.request.userPrincipal != null}">
						<p>Welcome ${pageContext.request.userPrincipal.name}</p>
						<li><a href="<c:url value="/account/signout"/>">Sign Out</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="<c:url value="/account/signin"/>">Sign In</a></li>		
					</c:otherwise>
				</c:choose>
			</ul>
			<div id="nav">
				<ul>
					<li><a href="${pageContext.request.contextPath}">Home</a></li>
					<li><a href="${pageContext.request.contextPath}/appointments">Appointments</a></li>
					<li><a href="${pageContext.request.contextPath}/owners">Owners</a></li>
				</ul>
			</div>
		</div>
		<div id="content">
			<tiles:insertAttribute name="content"/>
		</div>
		<div id="footer">
			<ul id="legal">
				<li>Privacy Policy</li>
				<li>Terms of Service</li>
			</ul>	
			<p>(c) 2009 <a href="http://www.springsource.org">springsource.org</a></p>
		</div>
	</div>
</body>
</html>