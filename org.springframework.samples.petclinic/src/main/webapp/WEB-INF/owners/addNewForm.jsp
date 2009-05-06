<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<h2>Add New Owner</h2>

<form:form id="addNewForm" action="${pageContext.request.contextPath}/owners" modelAttribute="owner" method="post">
	<form:label for="firstName" path="firstName">
		First Name
		<form:input path="firstName" />
	</form:label>
	<form:label for="lastName" path="lastName">
		Last Name
		<form:input path="lastName" />
	</form:label>
	<input type="submit" value="Add" />	
</form:form>