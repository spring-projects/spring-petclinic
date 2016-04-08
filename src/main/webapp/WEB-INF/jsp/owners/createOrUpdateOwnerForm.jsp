<!DOCTYPE html>

<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<html lang="en">

<jsp:include page="../fragments/htmlHeader.jsp"/>

<body>
<petclinic:bodyHeader menuName="owners"/>
<div class="container-fluid">
    <div class="container xd-container">
        <h2>
            <c:if test="${owner['new']}">New </c:if> Owner
        </h2>
        <form:form modelAttribute="owner" class="form-horizontal" id="add-owner-form">
            <div class="form-group has-feedback">
                <petclinic:inputField label="First Name" name="firstName"/>
                <petclinic:inputField label="Last Name" name="lastName"/>
                <petclinic:inputField label="Address" name="address"/>
                <petclinic:inputField label="City" name="city"/>
                <petclinic:inputField label="Telephone" name="telephone"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${owner['new']}">
                            <button class="btn btn-default" type="submit">Add Owner</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Update Owner</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
        <petclinic:pivotal/>
    </div>
</div>
<jsp:include page="../fragments/footer.jsp"/>
</body>

</html>
