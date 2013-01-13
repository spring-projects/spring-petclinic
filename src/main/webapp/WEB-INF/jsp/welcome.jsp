<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html lang="en">

<jsp:include page="header.jsp"/>

<body>
	<div id="header">
		<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
		<img src="${banner}" />
	</div>

  	<div id="main">
		<img src="<spring:url value="/resources/images/pets.png" htmlEscape="true" />" align="right" style="position:relative;right:30px;"></img>
		<h2><fmt:message key="welcome"/></h2>
		
		<ul>
		  <li><a href="<spring:url value="/owners/search" htmlEscape="true" />">Find owner</a></li>
		  <li><a href="<spring:url value="/vets" htmlEscape="true" />">Display all veterinarians</a></li>
		  <li><a href="<spring:url value="/resources/html/tutorial.html" htmlEscape="true" />">Tutorial</a></li>
		</ul>
		
		<p>&nbsp;</p>
		<p>&nbsp;</p>
	

  	</div>
	<jsp:include page="footer.jsp"/>
</body>

</html>
