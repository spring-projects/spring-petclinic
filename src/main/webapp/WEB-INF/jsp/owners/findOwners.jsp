<!DOCTYPE html>

<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<html lang="en">

<jsp:include page="../fragments/htmlHeader.jsp"/>

<body>
<petclinic:bodyHeader menuName="owners"/>
<div class="container-fluid">
    <div class="container xd-container">

        <h2>Find Owners</h2>

        <spring:url value="/owners.html" var="formUrl"/>
        <form:form modelAttribute="owner" action="${fn:escapeXml(formUrl)}" method="get" class="form-horizontal"
                   id="search-owner-form">
            <div class="form-group">
                <div class="control-group" id="lastName">
                    <label class="col-sm-2 control-label">Last name </label>
                    <div class="col-sm-10">
                        <form:input class="form-control" path="lastName" size="30" maxlength="80"/>
                        <span class="help-inline"><form:errors path="*"/></span>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">Find Owner</button>
                </div>
            </div>

        </form:form>

        <br/>
        <a class="btn btn-default" href='<spring:url value="/owners/new" htmlEscape="true"/>'>Add Owner</a>

        <petclinic:pivotal/>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>
</body>

</html>
