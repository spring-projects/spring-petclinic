<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en">

<jsp:include page="../header.jsp"/>

<body>

	<div class="container">
		<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
		<img src="${banner}" />

		<h2>Find Owners</h2>
		
		<spring:url value="/owners" var="formUrl"/>
		<form:form modelAttribute="owner" action="${fn:escapeXml(formUrl)}" method="get" class="form-horizontal">
					<fieldset>
						<div class="controls">
							<label class="control-label">Last name </label>
							<form:input path="lastName" size="30" maxlength="80"/>
							<span class="help-inline"><form:errors path="*" /></span>
						</div>				
						<div class="form-actions">
							<button type="submit">Find Owner</button>
						</div>
					</fieldset>
		</form:form>
		
		<br/>
		<a href='<spring:url value="/owners/new" htmlEscape="true"/>'>Add Owner</a>
		
		<jsp:include page="../footer.jsp"/>

  	</div>
</body>

</html>
