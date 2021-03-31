<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="foodOffer">


    <h2 style="text-align:center;padding:5px"><fmt:message key="foodOffer"/></h2>



    <table class="table table-striped" id="foodOfferTable">
    	<thead>
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
            <td><c:out value="${foodOffer.discount} %"/></td>
        </tr>

        <tr>
            <th><fmt:message key="offerCode"/></th>
            <td><c:out value="${foodOffer.code}"/></td>
        </tr>
        </thead>
    </table>
    
    <div class="btn-return">
	    <button type="button" role="link" onclick="window.location='/offers'" style="font-family: 'Lobster'; font-size: 20px;"> 
	    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true" style="padding: 5px"> </span> 
	    <fmt:message key="return"/> </button>
    </div>
    
	<sec:authorize access="hasAnyAuthority('client')">
    <spring:url value="{foodOfferId}/edit" var="editUrl">
    <spring:param name="foodOfferId" value="${foodOffer.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar oferta</a>
    </sec:authorize>
    
    <sec:authorize access="hasAnyAuthority('client')">
    <spring:url value="{foodOfferId}/disable" var="editUrl">
    <spring:param name="foodOfferId" value="${foodOffer.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Desactivar oferta</a>
    </sec:authorize>
	
</cheapy:layout>
