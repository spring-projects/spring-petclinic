<!DOCTYPE html> 

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables" %>

<html lang="en">

<jsp:include page="../fragments/staticFiles.jsp"/>

<body>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    <h2><fmt:message key="owners"/></h2>
    
    <spring:message code="name" var="Name" />
    <spring:message code="address" var="Address" />
    <spring:message code="city" var="City" />
    <spring:message code="telephone" var="Telephone" />
    <spring:message code="pets" var="Pets" />
    
    <datatables:table id="owners" data="${selections}" row="owner" theme="bootstrap2" 
                      cssClass="table table-striped" pageable="false" info="false" export="pdf">
        <datatables:column title="${Name}" cssStyle="width: 150px;" display="html">
            <spring:url value="/owners/{ownerId}.html" var="ownerUrl">
                <spring:param name="ownerId" value="${owner.id}"/>
            </spring:url>
            <a href="${fn:escapeXml(ownerUrl)}"><c:out value="${owner.firstName} ${owner.lastName}"/></a>
        </datatables:column>
        <datatables:column title="${Name}" display="pdf">
            <c:out value="${owner.firstName} ${owner.lastName}"/>
        </datatables:column>
        <datatables:column title="${Address}" property="address" cssStyle="width: 200px;"/>
        <datatables:column title="${City}" property="city"/>
        <datatables:column title="${Telephone}" xtitlekey="telephone" property="telephone"/>
        <datatables:column title="${Pets}" xtitlekey="pets" cssStyle="width: 100px;">
            <c:forEach var="pet" items="${owner.pets}">
                <c:out value="${pet.name}"/>
            </c:forEach>
        </datatables:column>
        <datatables:export type="pdf" cssClass="btn" cssStyle="height: 25px;" />
    </datatables:table>
    
    <jsp:include page="../fragments/footer.jsp"/>

</div>
</body>

</html>
