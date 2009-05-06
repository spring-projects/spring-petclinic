<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h2>Search Owners</h2>

<form:form id="searchForm" action="owners/search" modelAttribute="ownerSearchForm" method="get">
	<form:label for="lastName" path="lastName">
		Last Name
		<form:input path="lastName" />
	</form:label>
	<input type="submit" value="Search" />	
</form:form>