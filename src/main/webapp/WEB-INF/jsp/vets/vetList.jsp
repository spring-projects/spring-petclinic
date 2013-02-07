<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html lang="en">


<jsp:include page="../fragments/headTag.jsp"/>

<body>
  	<div class="container">
		<jsp:include page="../fragments/bodyHeader.jsp"/>

		<h2>Veterinarians</h2>
		
			<table class="table table-stripped" style="width:600px;">
			  <thead>
			  	<tr>
				    <th>Name</th>
				    <th>Specialties</th>
			    </tr>
			  </thead>
			  <tbody>
				  <c:forEach var="vet" items="${vets.vetList}">
				    <tr>
				      <td><c:out value="${vet.firstName} ${vet.lastName}" /></td>
				      <td>
					    <c:forEach var="specialty" items="${vet.specialties}">
				          <c:out value="${specialty.name}" />
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
	
			<jsp:include page="../fragments/footer.jsp"/>
	  	</div>
	</body>

</html>
