<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="ofertas de plato especifico">
	
	<spring:url value="/offers/nuOfferList" var="nuOfferUrl">
    </spring:url>
    <button type="button" role="link" onclick="window.location='/offers/nuOfferList'" style="font-family: 'Lobster'; font-size: 20px;">
	<span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
	Ofertas por número de comensales</button>
	
	<spring:url value="/offers/speedOfferList" var="speedOfferUrl">
    </spring:url>
    <button type="button" role="link" onclick="window.location='/offers/speedOfferList'" style="font-family: 'Lobster'; font-size: 20px;">
	<span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
	Ofertas de velocidad</button>
	
	<spring:url value="/offers/timeOfferList" var="timeOfferUrl">
    </spring:url>
    <button type="button" role="link" onclick="window.location='/offers/timeOfferList'" style="font-family: 'Lobster'; font-size: 20px;">
	<span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
	Ofertas de franja horaria</button>
	
    <h2 style="text-align:center;padding:5px"><fmt:message key="foodOffers"/></h2>
    
	<c:if test="${empty foodOfferLs }">
		<p id="vacio" >No hay ninguna oferta por plato específico activa.</p>
	</c:if>
	<c:if test="${not empty foodOfferLs }">
    <table id="foodOfferTable" class="table table-striped">
        <thead>
        <tr>
        	<!-- <th style="width: 150px;">Restaurante</th> -->
        	<th><fmt:message key="name"/></th>
        	<th><fmt:message key="food"/></th>
        	<th><fmt:message key="discount"/></th>
            <th><fmt:message key="startDate"/></th>
            <th><fmt:message key="endDate"/></th>
            
            <th> </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${foodOfferLs}" var="foodOffer">
            <tr>
            	<td>
                    <c:out value="${foodOffer.client.name}"/>
                </td>
                <td>
                    <c:out value="${foodOffer.food}"/>
                </td>
                <td>
                    <c:out value="${foodOffer.discount}%"/>
                </td>
                <td>
                    <c:out value="${localDateTimeFormat.format(foodOffer.start)}"/>
                </td>
                <td>
                    <c:out value="${localDateTimeFormat.format(foodOffer.end)}"/>
                </td>
                
                <td>
	                <spring:url value="/offers/food/{foodOfferId}" var="foodOfferUrl">
	                        <spring:param name="foodOfferId" value="${foodOffer.id}"/>
	                </spring:url>
	                <div class="btn-detalles">
                		<button type="button" role="link" onclick="window.location='${fn:escapeXml(foodOfferUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
                		<span class="glyphicon glyphicon-info-sign" aria-hidden="true" style="padding: 5px"> </span>
	                	<fmt:message key="details"/></button>
            		</div>
                </td> 
                  
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </c:if>
	
</cheapy:layout>
