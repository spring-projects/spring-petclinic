<!DOCTYPE html> 

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<html lang="en">

<jsp:include page="../fragments/staticFiles.jsp"/>


<body>
<script>
    $(function () {
        $("#date").datepicker({ dateFormat: 'yy/mm/dd'});
    });
</script>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    <h2><c:if test="${visit['new']}"><fmt:message key="new"/> </c:if><fmt:message key="visit"/></h2>

    <b><fmt:message key="pet"/></b>
    <table class="table table-striped">
        <thead>
        <tr>
            <th><fmt:message key="name"/></th>
            <th><fmt:message key="birthDate"/></th>
            <th><fmt:message key="type"/></th>
            <th><fmt:message key="owner"/></th>
        </tr>
        </thead>
        <tr>
            <td><c:out value="${visit.pet.name}"/></td>
            <td><joda:format value="${visit.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
            <td><c:out value="${visit.pet.type.name}"/></td>
            <td><c:out value="${visit.pet.owner.firstName} ${visit.pet.owner.lastName}"/></td>
        </tr>
    </table>
    
    <spring:message code="date" var="date" />
    <spring:message code="description" var="description" />

    <form:form modelAttribute="visit">
    
        <petclinic:inputField label="${date}" name="date" />
        <petclinic:inputField label="${description}" name="description" />
        
        <div class="form-actions">
            <input type="hidden" name="petId" value="${visit.pet.id}"/>
            <button type="submit"><fmt:message key="addVisit"/></button>
        </div>
    </form:form>

    <br/>
    <b><fmt:message key="previousVisits"/></b>
    <table style="width: 333px;">
        <tr>
            <th><fmt:message key="visit"/><fmt:message key="date"/></th>
            <th><fmt:message key="visit"/><fmt:message key="description"/></th>
        </tr>
        <c:forEach var="visit" items="${visit.pet.visits}">
            <c:if test="${!visit['new']}">
                <tr>
                    <td><joda:format value="${visit.date}" pattern="yyyy/MM/dd"/></td>
                    <td><c:out value="${visit.description}"/></td>
                </tr>
            </c:if>
        </c:forEach>
    </table>

</div>
<jsp:include page="../fragments/footer.jsp"/>
</body>

</html>
