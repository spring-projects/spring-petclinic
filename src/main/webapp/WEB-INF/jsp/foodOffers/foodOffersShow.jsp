<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="foodOffer">

    <h2><fmt:message key="foodOffer"/></h2>


    <table class="table table-striped">
        <tr>
            <th><fmt:message key="offerBeginning"/></th>
            <td><c:out value="${localDateTimeFormat.format(foodOffer.start)}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="offerEnding"/></th>
            <td><c:out value="${localDateTimeFormat.format(foodOffer.end)}"/></td>
        </tr>
		<tr>
            <th><fmt:message key="foodInOffer"/></th>
            <td><c:out value="${foodOffer.food}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="discount"/></th>
            <td><c:out value="${foodOffer.discount}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="cuantity"/></th>
            <td><c:out value="${foodOffer.units}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="offerCode"/></th>
            <td><c:out value="${foodOffer.code}"/></td>
        </tr>
    </table>
    
    <div class="btn-return">
	    <button type="button" role="link" onclick="window.location='/offers'" style="font-family: 'Lobster'; font-size: 20px;"> 
	    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true" style="padding: 5px"> </span> 
	    <fmt:message key="return"/> </button>
    </div>

    <%-- <spring:url value="{ownerId}/edit" var="editUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Owner</a> --%>

</cheapy:layout>
