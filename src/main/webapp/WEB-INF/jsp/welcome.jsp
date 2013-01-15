<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html lang="en">

<jsp:include page="header.jsp"/>

<body>
  	<div class="container">
		<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
		<img src="${banner}" />
		<img src="<spring:url value="/resources/images/pets.png" htmlEscape="true" />" align="right" style="position:relative;right:30px;"></img>
		<h2><fmt:message key="welcome"/></h2>
		
		<ul class="unstyled">
		  <li><a href="<spring:url value="/owners/find.html" htmlEscape="true" />">Find owner</a></li>
		  <li><a href="<spring:url value="/vets.html" htmlEscape="true" />">Display all veterinarians</a></li>
		  <li><a href="<spring:url value="/resources/html/tutorial.html" htmlEscape="true" />">Tutorial</a></li>
		</ul>
		
	
		<jsp:include page="footer.jsp"/>

  	</div>
</body>

</html>
