<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html lang="en">

<jsp:include page="header.jsp"/>

<body>
	<div id="header">
		<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
		<img src="${banner}" />
	</div>
	
  	<div id="main">

		<h2>Veterinarians:</h2>
		
			<table>
			  <thead>
			  	<tr>
				    <th>Name</th>
				    <th>Specialties</th>
			    </tr>
			  </thead>
			  <tbody>
				  <c:forEach var="vet" items="${vets.vetList}">
				    <tr>
				      <td>${vet.firstName} ${vet.lastName}</td>
				      <td>
					    <c:forEach var="specialty" items="${vet.specialties}">
				          ${specialty.name}
				        </c:forEach>
				        <c:if test="${vet.nrOfSpecialties == 0}">none</c:if>
				      </td>
				    </tr>
				  </c:forEach>
			  </tbody>
			</table>
			<table class="table-buttons">
			  <tr>
			    <td>
			      <a href="<spring:url value="/vets.xml" htmlEscape="true" />">View as XML</a>
			    </td>
			  </tr>
			</table>
	
	  	</div>
		<jsp:include page="footer.jsp"/>
	</body>

</html>
