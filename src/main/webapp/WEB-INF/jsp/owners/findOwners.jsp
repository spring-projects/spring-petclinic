<!DOCTYPE html>

<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en">

<jsp:include page="../fragments/staticFiles.jsp"/>

<body>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>

    <h2>Find Owners</h2>

    <spring:url value="/owners.html" var="formUrl"/>
    <form:form modelAttribute="owner" action="${fn:escapeXml(formUrl)}" method="get" class="form-horizontal"
               id="search-owner-form">
        <fieldset>
            <div class="control-group" id="lastName">
                <label class="control-label">Last name </label>
                <form:input path="lastName" size="30" maxlength="80"/>
                <span class="help-inline"><form:errors path="*"/></span>
            </div>
            <div class="form-actions">
                <button type="submit">Find Owner</button>
            </div>
        </fieldset>
    </form:form>

    <br/>
    <a href='<spring:url value="/owners/new" htmlEscape="true"/>'>Add Owner</a>

    <jsp:include page="../fragments/footer.jsp"/>

</div>
</body>

</html>
