<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html lang="en">

<jsp:include page="../header.jsp"/>

<body>

  	<div class="container">
		<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
		<img src="${banner}" />
		
		<c:choose>
			<c:when test="${pet['new']}"><c:set var="method" value="post"/></c:when>
			<c:otherwise><c:set var="method" value="put"/></c:otherwise>
		</c:choose>
		
		<h2><c:if test="${pet['new']}">New </c:if>Pet</h2>
		
		<b>Owner:</b> ${pet.owner.firstName} ${pet.owner.lastName}
		<br/>
		<form:form modelAttribute="pet" method="${method}" class="form-horizontal">
			<fieldset>
					<div class="control-group" id="name">
						<label class="control-label">Name </label>
						<div class="controls">
							<form:input path="name" />
							<span class="help-inline"><form:errors path="name" /></span>
						</div>
					</div>
					<div class="control-group" id="birthDate">
						<label class="control-label">Birth Date (yyyy-MM-dd)</label>
						<div class="controls">
							<form:input path="birthDate" />
							<span class="help-inline"><form:errors path="birthDate" /></span>
						</div>
					</div>
					<div class="control-group" id="type">
						<label class="control-label">Type </label>
						<form:select path="type" items="${types}"/>
					</div>
					<div class="form-actions">
						<c:choose>
				          <c:when test="${owner['new']}">
				            <button type="submit">Add Pet</button>
				          </c:when>
				          <c:otherwise>
				            <button type="submit">Update Pet</button>
				          </c:otherwise>
				        </c:choose>
					</div>
				</fieldset>	
			</form:form>	
			<c:if test="${!pet['new']}">
			</c:if>    
			<jsp:include page="../footer.jsp"/>
  	</div>
</body>

</html>
