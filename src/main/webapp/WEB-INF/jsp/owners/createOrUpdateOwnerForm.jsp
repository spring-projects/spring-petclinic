<!DOCTYPE html> 

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<html lang="en">

<jsp:include page="../fragments/staticFiles.jsp"/>

<body>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    <c:choose>
        <c:when test="${owner['new']}"><c:set var="method" value="post"/></c:when>
        <c:otherwise><c:set var="method" value="put"/></c:otherwise>
    </c:choose>

    <h2>
        <c:if test="${owner['new']}"><fmt:message key="new"/> </c:if> <fmt:message key="owner"/>
    </h2>
    
	<spring:message code="firstName" var="firstName" />
	<spring:message code="lastName" var="lastName" />
	<spring:message code="address" var="Address" />
	<spring:message code="city" var="City" />
	<spring:message code="telephone" var="Telephone" />

    <form:form modelAttribute="owner" method="${method}" class="form-horizontal" id="add-owner-form">
        <petclinic:inputField label="${firstName}" name="firstName"/>
        <petclinic:inputField label="${lastName}" name="lastName"/>
        <petclinic:inputField label="${Address}" name="address"/>
        <petclinic:inputField label="${City}" name="city"/>
        <petclinic:inputField label="${Telephone}" name="telephone"/>

        <div class="form-actions">
            <c:choose>
                <c:when test="${owner['new']}">
                    <button type="submit"><fmt:message key="addOwner"/></button>
                </c:when>
                <c:otherwise>
                    <button type="submit"><fmt:message key="updateOwner"/></button>
                </c:otherwise>
            </c:choose>
        </div>
    </form:form>
</div>
<jsp:include page="../fragments/footer.jsp"/>
</body>

</html>
