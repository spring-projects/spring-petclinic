<!DOCTYPE html>

<%@ page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<html lang="en">

<jsp:include page="../fragments/htmlHeader.jsp"/>

<body>
<petclinic:bodyHeader menuName="owners"/>
<div class="container-fluid">
    <div class="container xd-container">

        <h2>Owner Information</h2>


        <table class="table table-striped">
            <tr>
                <th>Name</th>
                <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
            </tr>
            <tr>
                <th>Address</th>
                <td><c:out value="${owner.address}"/></td>
            </tr>
            <tr>
                <th>City</th>
                <td><c:out value="${owner.city}"/></td>
            </tr>
            <tr>
                <th>Telephone</th>
                <td><c:out value="${owner.telephone}"/></td>
            </tr>
        </table>

        <spring:url value="{ownerId}/edit.html" var="editUrl">
            <spring:param name="ownerId" value="${owner.id}"/>
        </spring:url>
        <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Owner</a>

        <spring:url value="{ownerId}/pets/new.html" var="addUrl">
            <spring:param name="ownerId" value="${owner.id}"/>
        </spring:url>
        <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Pet</a>

        <br/>
        <br/>
        <br/>
        <h2>Pets and Visits</h2>

        <table class="table table-striped">
            <c:forEach var="pet" items="${owner.pets}">

                <tr>
                    <td valign="top">
                        <dl class="dl-horizontal">
                            <dt>Name</dt>
                            <dd><c:out value="${pet.name}"/></dd>
                            <dt>Birth Date</dt>
                            <dd><joda:format value="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                            <dt>Type</dt>
                            <dd><c:out value="${pet.type.name}"/></dd>
                        </dl>
                    </td>
                    <td valign="top">
                        <table class="table-condensed">
                            <thead>
                            <tr>
                                <th>Visit Date</th>
                                <th>Description</th>
                            </tr>
                            </thead>
                            <c:forEach var="visit" items="${pet.visits}">
                                <tr>
                                    <td><joda:format value="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                    <td><c:out value="${visit.description}"/></td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td>
                                    <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                        <spring:param name="ownerId" value="${owner.id}"/>
                                        <spring:param name="petId" value="${pet.id}"/>
                                    </spring:url>
                                    <a href="${fn:escapeXml(petUrl)}">Edit Pet</a>
                                </td>
                                <td>
                                    <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                        <spring:param name="ownerId" value="${owner.id}"/>
                                        <spring:param name="petId" value="${pet.id}"/>
                                    </spring:url>
                                    <a href="${fn:escapeXml(visitUrl)}">Add Visit</a>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

            </c:forEach>
        </table>

        <petclinic:pivotal/>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>

</body>

</html>
