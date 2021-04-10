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


    <h2 style="text-align:center;padding:5px"><fmt:message key="nuOffers"/></h2>
	<c:if test="${empty nuOfferLs }">
		<p id="vacio" >No hay ninguna oferta por número de comensales activa.</p>
	</c:if>
	<c:if test="${not empty nuOfferLs }">
    <table id="nuOfferTable" class="table table-striped">
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
        <c:forEach items="${nuOfferLs}" var="nuOffer">
            <tr>
                <td>
                    <c:out value="${nuOffer.client.name}"/>
                </td>
                <td>
                    <c:out value="${localDateTimeFormat.format(nuOffer.start)}"/>
                </td>
                <td>
                    <c:out value="${localDateTimeFormat.format(nuOffer.end)}"/>
                </td>
                <td>
                    <c:out value="${nuOffer.gold} comensales"/>
                </td>
                <td>
                    <c:out value="${nuOffer.discountGold}%"/>
                </td>
                <td>
	                <spring:url value="/offers/nu/{nuOfferId}" var="nuOfferUrl">
	                        <spring:param name="nuOfferId" value="${nuOffer.id}"/>
	                </spring:url>
	                <div class="btn-detalles">
		                <button type="button" role="link" onclick="window.location='${fn:escapeXml(nuOfferUrl)}'" class="btn-detalles" style="font-family: 'Lobster'; font-size: 20px;">
		                <span class="glyphicon glyphicon-info-sign" aria-hidden="true" style="padding: 5px"> </span>
		                <fmt:message key="details"/> </button>
		            </div>
                </td>  
            </tr>
        </c:forEach>
        </tbody>
    </table>
        <c:if test='${page!=0}'>
    	<spring:url value="/offers/nuOfferList/{page}" var="nuOfferListUrl">
    		<spring:param name="page" value="${page-1}"/>
    	</spring:url>
    	<button type="button" role="link" onclick="window.location='${fn:escapeXml(nuOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
		<span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
		Pág. anterior</button>
    </c:if>
    <c:out value='${page}'></c:out>
    <c:if test="${fn:length(nuOfferLs) == 5}">
    	<spring:url value="/offers/nuOfferList/{page}" var="nuOfferListUrl">
    		<spring:param name="page" value="${page+1}"/>
    	</spring:url>
    	<button type="button" role="link" onclick="window.location='${fn:escapeXml(nuOfferListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
		<span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
		Pág. siguiente</button>
	</c:if>
    </c:if>
	
</cheapy:layout>
