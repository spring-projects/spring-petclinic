<html lang="en">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="header.jsp"/>

<body>
  	<div class="container">
		<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
		<img src="${banner}" />
		<spring:url value="/resources/images/pets.png" var="petsImage"/>
		<img src="${petsImage}" />
		<h2>Something happened...</h2>
		<p>${exception.message}</p>
		
	
		<jsp:include page="footer.jsp"/>

  	</div>
</body>

</html>
