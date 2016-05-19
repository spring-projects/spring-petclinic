<!DOCTYPE html>

<%@ page session="false" %>
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
        $("#date").datepicker({dateFormat: 'yy/mm/dd'});
    });
</script>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    <h2><c:if test="${visit['new']}">New </c:if>Visit</h2>

    <b>Pet</b>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Birth Date</th>
            <th>Type</th>
            <th>Owner</th>
        </tr>
        </thead>
        <tr>
            <td><c:out value="${visit.pet.name}"/></td>
            <td><joda:format value="${visit.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
            <td><c:out value="${visit.pet.type.name}"/></td>
            <td><c:out value="${visit.pet.owner.firstName} ${visit.pet.owner.lastName}"/></td>
        </tr>
    </table>

    <form:form modelAttribute="visit">

        <petclinic:inputField label="date" name="date"/>
        <petclinic:inputField label="description" name="description"/>

        <div class="form-actions">
            <input type="hidden" name="petId" value="${visit.pet.id}"/>
            <button type="submit">Add Visit</button>
        </div>
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
                    <td><c:out value="${visit.description}"/></td>
                </tr>
            </c:if>
        </c:forEach>
    </table>

</div>
<jsp:include page="../fragments/footer.jsp"/>
</body>

</html>
