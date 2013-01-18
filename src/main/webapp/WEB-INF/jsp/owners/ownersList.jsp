<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html lang="en">

<jsp:include page="../header.jsp"/>

<body>

  	<div class="container">

		<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
		<img src="${banner}" />
		<h2>Owners</h2>
	
		<table class="table table-striped">
		  <thead>
		  	<tr>
			    <th style="width: 150px;">Name</th>
			    <th style="width: 200px;">Address</th>
			    <th>City</th>
			    <th>Telephone</th>
			    <th style="width: 100px;">Pets</th>
		    </tr>
		  </thead>
		  <c:forEach var="owner" items="${selections}">
		    <tr>
		      <td>
		          <spring:url value="owners/{ownerId}.html" var="ownerUrl">
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
		<jsp:include page="../footer.jsp"/>
	
	  	</div>
</body>

</html>
