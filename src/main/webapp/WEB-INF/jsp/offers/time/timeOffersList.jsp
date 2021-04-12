<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="ofertas">

	<spring:url value="/offers/foodOfferList/{page}" var="foodOfferListUrl">
		<spring:param name="page" value="0"/>
    </spring:url>
    <button type="button" role="link" onclick="window.location='${fn:escapeXml(foodOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
	<span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
	Ofertas de plato especifico</button>
	
	<spring:url value="/offers/nuOfferList/{page}" var="nuOfferListUrl">
		<spring:param name="page" value="0"/>
    </spring:url>
    <button type="button" role="link" onclick="window.location='${fn:escapeXml(nuOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
	<span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
	Ofertas por número de comensales</button>
	
	<spring:url value="/offers/speedOfferList/{page}" var="speedOfferListUrl">
		<spring:param name="page" value="0"/>
    </spring:url>
    <button type="button" role="link" onclick="window.location='${fn:escapeXml(speedOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
	<span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
	Ofertas de velocidad</button>
	
	<spring:url value="/offers/timeOfferList/{page}" var="timeOfferListUrl">
		<spring:param name="page" value="0"/>
    </spring:url>
    <button type="button" role="link" onclick="window.location='${fn:escapeXml(timeOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
	<span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
	Ofertas de franja horaria</button>

    
    <h2 style="text-align:center;padding:5px"><fmt:message key="timeOffers"/></h2>
	<c:if test="${empty timeOfferLs }">
		<p id="vacio" >No hay ninguna oferta por franja horaria activa.</p>
	</c:if>
	<c:if test="${not empty timeOfferLs }">
    <table id="timeOfferTable" class="table table-striped">
        <thead>
        <tr>
        	<!-- <th style="width: 150px;">Restaurante</th> -->
            <th><fmt:message key="name"/></th>
            <th><fmt:message key="startDate"/></th>
            <th><fmt:message key="endDate"/></th>
            <th><fmt:message key="init"/></th>
            <th><fmt:message key="finishOffer"/></th>
            <th> </th>
        </tr>
        </thead>
        <tbody>
        	<c:forEach items="${timeOfferLs}" var="timeOffer">
            <tr>
                <td>
                    <c:out value="${timeOffer.client.name}"/>
                </td>
                <td>
                    <c:out value="${localDateTimeFormat.format(timeOffer.start)}"/>
                </td>
                <td>
                    <c:out value="${localDateTimeFormat.format(timeOffer.end)}"/>
                </td>
                <td>
                    <c:out value="${timeOffer.init}h"/>
                </td>
                <td>
                    <c:out value="${timeOffer.finish}h"/>
                </td>
                
                <td>
                	<spring:url value="/offers/time/{timeOfferId}" var="timeOfferUrl">
                        <spring:param name="timeOfferId" value="${timeOffer.id}"/>
                    </spring:url>
                    <div class="btn-detalles">
	                    <button type="button" role="link" onclick="window.location='${fn:escapeXml(timeOfferUrl)}'" class="btn-detalles" style="font-family: 'Lobster'; font-size: 20px;">
	                    <span class="glyphicon glyphicon-info-sign" aria-hidden="true" style="padding: 5px"> </span>
	                    <fmt:message key="details"/> </button>
	                </div>
                </td> 
            </tr>
        </c:forEach>
        </tbody>
    </table>
        <c:if test='${page!=0}'>
    	<spring:url value="/offers/timeOfferList/{page}" var="timeOfferListUrl">
    		<spring:param name="page" value="${page-1}"/>
    	</spring:url>
    	<button type="button" role="link" onclick="window.location='${fn:escapeXml(timeOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
		<span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
		Pág. anterior</button>
    </c:if>
    <c:out value='${page}'></c:out>
    <c:if test="${fn:length(timeOfferLs) == 5}">
    	<spring:url value="/offers/foodOfferList/{page}" var="timeOfferListUrl">
    		<spring:param name="page" value="${page+1}"/>
    	</spring:url>
    	<button type="button" role="link" onclick="window.location='${fn:escapeXml(timeOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
		<span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
		Pág. siguiente</button>
	</c:if>
    </c:if>
	
</cheapy:layout>
