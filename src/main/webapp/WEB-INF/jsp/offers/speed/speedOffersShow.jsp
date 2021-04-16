<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="speedOffer">

	<h2 style="font-family: 'Lobster'; text-align:center; font-size:200%;  color: rgb(0, 64, 128); padding:10px">
		<fmt:message key="speedOffer" />
	</h2>

    <table class="table table-striped" id="speedOffer-table">
    	<tr>
            <th><fmt:message key="client"/></th>
            <td><c:out value="${speedOffer.client.name}"/> </td>
        </tr>
        <tr>
            <th><fmt:message key="offerBeginning"/></th>
            <td><c:out value="${localDateTimeFormat.format(speedOffer.start)}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="offerEnding"/></th>
            <td><c:out value="${localDateTimeFormat.format(speedOffer.end)}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="goldGoal"/></th>
            <td><c:out value="${speedOffer.gold} minutos"/></td>
        </tr>
        <tr>
            <th><fmt:message key="goldDiscount"/></th>
            <td><c:out value="${speedOffer.discountGold}%"/></td>
        </tr>
        <tr>
            <th><fmt:message key="silverGoal"/></th>
            <td><c:out value="${speedOffer.silver} minutos"/></td>
        </tr>
        <tr>
            <th><fmt:message key="silverDiscount"/></th>
            <td><c:out value="${speedOffer.discountSilver}%"/></td>
        </tr>
        <tr>
            <th><fmt:message key="bronzeGoal"/></th>
            <td><c:out value="${speedOffer.bronze} minutos"/></td>
        </tr>
        <tr>
            <th><fmt:message key="bronzeDiscount"/></th>
            <td><c:out value="${speedOffer.discountBronze}%"/></td>
        </tr>
        <tr>
            <th><fmt:message key="offerCode"/></th>
            <td><c:out value="${speedOffer.code}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="municipio"/></th>
            <td><c:out value="${speedOffer.client.municipio}"/></td>
        </tr>
    </table>

    <div class="btn-menu">
	    
	<sec:authorize access="hasAnyAuthority('client')">
	<sec:authentication var="principal" property="principal" />
		<div class="btns-edit">
		<c:if test="${ principal.username eq speedOffer.client.usuar.username}">
			<c:if test="${speedOffer.status eq 'active' || speedOffer.status eq 'hidden' }">
			    <spring:url value="{speedOfferId}/edit" var="editUrl">
			    <spring:param name="speedOfferId" value="${speedOffer.id}"/>
			    </spring:url>
			    <button type="button" role="link" onclick="window.location='${fn:escapeXml(editUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
	            <span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
		        Editar oferta</button>
			</c:if>

			<c:if test="${speedOffer.status eq 'hidden' }">
		        <spring:url value="{speedOfferId}/activate" var="activateUrl">
		        <spring:param name="speedOfferId" value="${speedOffer.id}"/>
		        </spring:url>
		        <button type="button" role="link" onclick="window.location='${fn:escapeXml(activateUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
		            <span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
			        Activar oferta</button>
			</c:if>
			
			<c:if test="${speedOffer.status eq 'active' }">
			    <spring:url value="{speedOfferId}/disable" var="deactivateUrl">
			    <spring:param name="speedOfferId" value="${speedOffer.id}"/>
			    </spring:url>
			    <button type="button" role="link" onclick="window.location='${fn:escapeXml(deactivateUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
	            <span class="glyphicon glyphicon glyphicon-trash" aria-hidden="true" style="padding: 5px"> </span>
		        Desactivar oferta</button>
	        </c:if>
	    </c:if>
	    </div>
	    </sec:authorize>
	
    </div>
	
	

</cheapy:layout>
