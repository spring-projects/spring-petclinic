<!DOCTYPE html>

<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<html lang="en">

<jsp:include page="../fragments/staticFiles.jsp"/>
<body>

<script>
    $(function () {
        $("#birthDate").datepicker({dateFormat: 'yy/mm/dd'});
    });
</script>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>

    <h2>
        <c:if test="${pet['new']}">New </c:if>
        Pet
    </h2>

    <form:form modelAttribute="pet"
               class="form-horizontal">
        <input type="hidden" name="id" value="${pet.id}"/>
        <div class="control-group" id="owner">
            <label class="control-label">Owner </label>

            <c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/>
        </div>
        <petclinic:inputField label="Name" name="name"/>
        <petclinic:inputField label="Birth Date" name="birthDate"/>
        <div class="control-group">
            <petclinic:selectField name="type" label="Type " names="${types}" size="5"/>
        </div>
        <div class="form-actions">
            <c:choose>
                <c:when test="${pet['new']}">
                    <button type="submit">Add Pet</button>
                </c:when>
                <c:otherwise>
                    <button type="submit">Update Pet</button>
                </c:otherwise>
            </c:choose>
        </div>
    </form:form>
    <c:if test="${!pet['new']}">
    </c:if>
    <jsp:include page="../fragments/footer.jsp"/>
</div>
</body>

</html>
