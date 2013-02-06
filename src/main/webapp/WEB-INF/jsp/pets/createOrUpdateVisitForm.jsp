<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<html lang="en">

<jsp:include page="../fragments/headTag.jsp"/>


<body>
	<script>
		$(function() {
			$("#date").datepicker({ dateFormat: 'yy/mm/dd'});
		});
	</script>
  	<div class="container">
		<jsp:include page="../fragments/bodyHeader.jsp"/>
		<h2><c:if test="${visit['new']}">New </c:if>Visit:</h2>
		
		<form:form modelAttribute="visit">
		  <b>Pet</b>
		  <table  class="table table-striped">
		    <thead>
		    	<tr>
			      <th>Name</th>
			      <th>Birth Date</th>
			      <th>Type</th>
			      <th>Owner</th>
		      	</tr>
		    </thead>
		    <tr>
		      <td><c:out value="${visit.pet.name}" /></td>
		      <td><joda:format value="${visit.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
		      <td><c:out value="${visit.pet.type.name}" /></td>
		      <td><c:out value="${visit.pet.owner.firstName} ${visit.pet.owner.lastName}" /></td>
		    </tr>
		  </table>
		
		  <table class="table">
		    <tr>
		      <th>
		        Date
		        <br/><form:errors path="date" cssClass="errors"/>
		      </th>
		      <td>
		        <form:input path="date" size="10" maxlength="10"/>
		      </td>
		    <tr/>
		    <tr>
		      <th valign="top">
		        Description
		        <br/><form:errors path="description" cssClass="errors"/>
		      </th>
		      <td>
		        <form:textarea path="description" rows="10" cols="25"/>
		      </td>
		    </tr>
		    <tr>
		      <td colspan="2">
		        <input type="hidden" name="petId" value="${visit.pet.id}"/>
		        <p class="submit"><input type="submit" value="Add Visit"/></p>
		      </td>
		    </tr>
		  </table>
		</form:form>
		
		<br/>
		<b>Previous Visits</b>
		<table style="width: 333px;">
		  <tr>
		    <th>Date</th>
		    <th>Description</th>
		  </tr>
		  <c:forEach var="visit" items="${visit.pet.visits}">
		    <c:if test="${!visit['new']}">
		      <tr>
		        <td><joda:format value="${visit.date}" pattern="yyyy/MM/dd"/></td>
		        <td><c:out value="${visit.description}" /></td>
		      </tr>
		    </c:if>
		  </c:forEach>
		</table>

  	</div>
	<jsp:include page="../fragments/footer.jsp"/>
</body>

</html>
