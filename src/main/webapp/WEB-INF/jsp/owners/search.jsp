<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en">

<jsp:include page="../header.jsp"/>

<body>

  <div id="main">

		<h2>Find Owners:</h2>
		
		<spring:url value="/owners" var="formUrl"/>
		<form:form modelAttribute="owner" action="${fn:escapeXml(formUrl)}" method="get">
					<fieldset>
						<label class="control-label">Last name:</label>
						<div class="controls">
							<form:input path="lastName" size="30" maxlength="80"/>
							<form:errors path="*" cssClass="errors"/>
						</div>				
						<div>
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
