<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables" %>

<html lang="en">

<jsp:include page="../fragments/headTag.jsp"/>

<body>
<div class="container">
    <jsp:include page="../fragments/bodyHeader.jsp"/>
    <h2>Pets</h2>
    
     <datatables:table id="pets" data="${selections}" cdn="true" row="pet" theme="bootstrap2" 
                      cssClass="table table-striped" paginate="false" info="false" export="pdf">
        
        <datatables:column title="Name" cssStyle="width: 150px;" display="html">
             <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
			                        <spring:param name="ownerId" value="${pet.owner.id}"/>
			                        <spring:param name="petId" value="${pet.id}"/>
			 </spring:url>
            <a href="${fn:escapeXml(petUrl)}"><c:out value="${pet.name}"/></a>
        </datatables:column>
        <datatables:column title="Name" display="pdf">
            <c:out value="${pet.name}"/>
        </datatables:column>
        <datatables:column title="Birth Date">
        	<joda:format value="${pet.birthDate}" pattern="yyyy-MM-dd"/>
        </datatables:column>
        <datatables:column title="type" property="type.name"/>
        <spring:url value="/owners/{ownerId}.html" var="ownerUrl">
                			<spring:param name="ownerId" value="${pet.owner.id}"/>
        </spring:url>
        <datatables:column title="owner" display="html">
        <a href="${fn:escapeXml(ownerUrl)}"><c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/></a>
        </datatables:column>
        <datatables:column title="owner" display="pdf">
        <c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/>
        </datatables:column>
        
        <datatables:export type="pdf" cssClass="btn btn-small" />
    </datatables:table>
    
         
    <jsp:include page="../fragments/footer.jsp"/>

</div>
</body>

</html>