<html lang="en">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="fragments/headTag.jsp"/>

<body>
  	<div class="container">
		<jsp:include page="fragments/bodyHeader.jsp"/>
		<spring:url value="/resources/images/pets.png" var="petsImage"/>
		<img src="${petsImage}" />
		<h2>Something happened...</h2>
		<p>${exception.message}</p>
		
	
		<jsp:include page="fragments/footer.jsp"/>

  	</div>
</body>

</html>
