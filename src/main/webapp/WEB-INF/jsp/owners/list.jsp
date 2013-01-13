<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html lang="en">

<jsp:include page="../header.jsp"/>

<body>
	<div id="header">
		<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
		<img src="${banner}" />
	</div>
  	<div id="main">

	<h2>Owners:</h2>
	
		<table>
		  <thead>
		  	<tr>
			    <th>Name</th>
			    <th>Address</th>
			    <th>City</th>
			    <th>Telephone</th>
			    <th>Pets</th>
		    </tr>
		  </thead>
		  <c:forEach var="owner" items="${selections}">
		    <tr>
		      <td>
		          <spring:url value="owners/{ownerId}" var="ownerUrl">
		              <spring:param name="ownerId" value="${owner.id}"/>
		          </spring:url>
		          <a href="${fn:escapeXml(ownerUrl)}">${owner.firstName} ${owner.lastName}</a>
		      </td>
		      <td>${owner.address}</td>
		      <td>${owner.city}</td>
		      <td>${owner.telephone}</td>
		      <td>
		        <c:forEach var="pet" items="${owner.pets}">
		          ${pet.name} &nbsp;
		        </c:forEach>
		      </td>
		    </tr>
		  </c:forEach>
		</table>
	
	  	</div>
		<jsp:include page="../footer.jsp"/>
</body>

</html>
