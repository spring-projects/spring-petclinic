<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="ofertas por rapidez">
	<div class="text-center">
		<div class="btn-filter-max">
			<spring:url value="/offers/foodOfferList/{page}" var="foodOfferListUrl">
				<spring:param name="page" value="0"/>
		    </spring:url>
		    <button type="button" role="link" class="btn-filter" onclick="window.location='${fn:escapeXml(foodOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
			<span class="glyphicon 	glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
			Ofertas de plato específico</button>
			
			<spring:url value="/offers/nuOfferList/{page}" var="nuOfferListUrl">
				<spring:param name="page" value="0"/>
		    </spring:url>
		    <button type="button" role="link" class="btn-filter" onclick="window.location='${fn:escapeXml(nuOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
			<span class="glyphicon 	glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
			Ofertas por número de comensales</button>
			
			<spring:url value="/offers/speedOfferList/{page}" var="speedOfferListUrl">
				<spring:param name="page" value="0"/>
		    </spring:url>
		    <button type="button" role="link" class="btn-filter-active" onclick="window.location='${fn:escapeXml(speedOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
			<span class="glyphicon 	glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
			Ofertas de velocidad</button>
			
			<spring:url value="/offers/timeOfferList/{page}" var="timeOfferListUrl">
				<spring:param name="page" value="0"/>
		    </spring:url>
		    <button type="button" role="link" class="btn-filter" onclick="window.location='${fn:escapeXml(timeOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
			<span class="glyphicon 	glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
			Ofertas de franja horaria</button>
		</div>
	</div>
    <h2 style="font-family: 'Lobster'; text-align:center; font-size:200%;  color: rgb(0, 64, 128); padding:10px"><fmt:message key="speedOffers"/>:</h2>
	<c:if test="${empty speedOfferLs }">
		<p id="vacio" >No hay ninguna oferta por tiempo empleado en comer activa.</p>
	</c:if>
	<c:if test="${not empty speedOfferLs }">
    <table id="speedOfferTable" class="table table-striped">
        <thead>
        <tr>
        	<!-- <th style="width: 150px;">Restaurante</th> -->
            <th><fmt:message key="name"/></th>
            <th><fmt:message key="startDate"/></th>
            <th><fmt:message key="endDate"/></th>
            <th><fmt:message key="goldGoal"/></th>
            <th><fmt:message key="goldDiscount"/></th>
            <th> </th>
            
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${speedOfferLs}" var="speedOffer">
            <tr>
                <td>
                    <c:out value="${speedOffer.client.name}"/>
                </td>
                <td>
                    <c:out value="${localDateTimeFormat.format(speedOffer.start)}"/>
                </td>
                <td>
                    <c:out value="${localDateTimeFormat.format(speedOffer.end)}"/>
                </td>
                <td>
                    <c:out value="${speedOffer.gold} minutos"/>
                </td>
                <td>
                    <c:out value="${speedOffer.discountGold}%"/>
                </td>
                <td>
                    <spring:url value="/offers/speed/{speedOfferId}" var="speedOfferUrl">
                        <spring:param name="speedOfferId" value="${speedOffer.id}"/>
                    </spring:url>
                    <div class="btn-detalles">
	                    <button type="button" role="link" onclick="window.location='${fn:escapeXml(speedOfferUrl)}'" class="btn-detalles" style="font-family: 'Lobster'; font-size: 20px;">
	                    <span class="glyphicon glyphicon-info-sign" aria-hidden="true" style="padding: 5px"> </span>
	                    <fmt:message key="details"/> </button>
	                </div>
                </td>
                  
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="text-center">
    	<c:out value='Página ${page}'></c:out>
    </div>
    <c:if test='${page!=0}'>
    <div class="text-right">
    	<spring:url value="/offers/speedOfferList/{page}" var="speedOfferListUrl">
    		<spring:param name="page" value="${page-1}"/>
    	</spring:url>
    	<button type="button" class="btn-pag" role="link" onclick="window.location='${fn:escapeXml(speedOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
		<span class="glyphicon 	glyphicon glyphicon-arrow-left" aria-hidden="true" style="padding: 5px"> </span>
		Pág. anterior</button>
	</div>
    </c:if>
    
    <c:if test="${fn:length(speedOfferLs) == 5}">
    <div class="text-right">
    	<spring:url value="/offers/speedOfferList/{page}" var="speedOfferListUrl">
    		<spring:param name="page" value="${page+1}"/>
    	</spring:url>
    	<button type="button" class="btn-pag" role="link" onclick="window.location='${fn:escapeXml(speedOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
		<span class="glyphicon 	glyphicon glyphicon-arrow-right" aria-hidden="true" style="padding: 5px"> </span>
		Pág. siguiente</button>
	</div>
	</c:if>
    </c:if>
</cheapy:layout>
