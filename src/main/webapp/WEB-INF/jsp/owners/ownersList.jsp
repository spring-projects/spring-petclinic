<%@page session="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html lang="en">

<jsp:include page="../fragments/headTag.jsp"/>
<style type="text/css" title="currentStyle">
    @import "/petclinic/resources/css/jquery.dataTables.css";
</style>
<script type="text/javascript" language="javascript" src="/petclinic/resources/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" charset="utf-8">
    $(document).ready(function() {
        $('#ownersTable').dataTable();
    } );
</script>

<body>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    <h2>Owners</h2>

    <table id="ownersTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Name</th>
            <th style="width: 200px;">Address</th>
            <th>City</th>
            <th>Telephone</th>
            <th style="width: 100px;">Pets</th>
        </tr>
        </thead>
        <c:forEach var="owner" items="${selections}">
            <tr>
                <td>
                    <spring:url value="owners/{ownerId}.html" var="ownerUrl">
                        <spring:param name="ownerId" value="${owner.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(ownerUrl)}"><c:out value="${owner.firstName} ${owner.lastName}"/></a>
                </td>
                <td><c:out value="${owner.address}"/></td>
                <td><c:out value="${owner.city}"/></td>
                <td><c:out value="${owner.telephone}"/></td>
                <td>
                    <c:forEach var="pet" items="${owner.pets}">
                        <c:out value="${pet.name}"/>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
    </table>
    <br/>
    <jsp:include page="../fragments/footer.jsp"/>

</div>
</body>

</html>
