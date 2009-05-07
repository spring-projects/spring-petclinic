<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<h2>Add New Appointment</h2>

<form:form id="addNewForm" action="${pageContext.request.contextPath}/appointments" modelAttribute="appointment" method="post">
	<form:label for="doctor" path="doctor">
		Doctor
		<form:input path="doctor" />
	</form:label>
	<form:label for="owner" path="owner">
		Owner
		<form:input path="owner" />
	</form:label>
	<form:label for="pet" path="pet">
		Pet
		<form:input path="pet" />
	</form:label>
	<form:label for="date" path="date">
		Date
		<form:input path="date" />
	</form:label>
	<form:label for="time" path="time">
		Time
		<form:input path="time" />
	</form:label>
	<form:label for="notes" path="notes">
		Notes
		<form:input path="notes" />
	</form:label>
	<input type="submit" value="Add" />	
</form:form>