<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html lang="en">

<jsp:include page="../header.jsp"/>

<body>
	<div id="header well">
		<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
		<img src="${banner}" />
	</div>
  	<div class="container" style="padding-top: 50px;">
		<c:choose>
			<c:when test="${owner['new']}"><c:set var="method" value="post"/></c:when>
			<c:otherwise><c:set var="method" value="put"/></c:otherwise>
		</c:choose>
		
		<h2><c:if test="${owner['new']}">New </c:if>Owner</h2>
		<form:form modelAttribute="owner" method="${method}" class="form-horizontal" id="add-owner-form">
			<fieldset>
					<div class="control-group" id="firstName">
						<label class="control-label">First Name </label>
						<div class="controls">
							<form:input path="firstName" />
							<span class="help-inline"><form:errors path="firstName" /></span>
						</div>
					</div>
					<div class="control-group" id="lastName">
						<label class="control-label">Last Name </label>
						<div class="controls">
							<form:input path="lastName" />
							<span class="help-inline"><form:errors path="lastName" /></span>
						</div>
					</div>
					<div class="control-group" id="address">
						<label class="control-label">Address </label>
						<div class="controls">
							<form:input path="address" />
							<span class="help-inline"><form:errors path="address" /></span>
						</div>
					</div>
					<div class="control-group" id="city">
						<label class="control-label">City </label>
						<div class="controls">
							<form:input path="city" />
							<span class="help-inline"><form:errors path="city" /></span>
						</div>
					</div>
					<div class="control-group" id="telephone">
						<label class="control-label">Telephone </label>
						<div class="controls">
							<form:input path="telephone" />
							<span class="help-inline"><form:errors path="telephone" /></span>
						</div>
					</div>
					<div class="form-actions">
						<c:choose>
				          <c:when test="${owner['new']}">
				            <button type="submit">Add Owner</button>
				          </c:when>
				          <c:otherwise>
				            <button type="submit">Update Owner</button>
				          </c:otherwise>
				        </c:choose>
					</div>
				</fieldset>
		</form:form>
  	</div>
	<jsp:include page="../footer.jsp"/>
</body>

</html>
