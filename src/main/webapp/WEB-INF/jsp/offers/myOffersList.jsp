<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="ofertasM">

	

    <h2 style="font-family: 'Lobster'; text-align:center; font-size:200%;  color: rgb(0, 64, 128); padding:10px"><fmt:message key="foodOffers"/>:</h2>
	<c:if test="${empty foodOfferLs }">
		<p id="vacio" >No hay ninguna oferta por plato específico creada.</p>
	</c:if>
	<c:if test="${not empty foodOfferLs }">
		<div class="table-responsive">
		    <table id="foodOfferTable" class="table table-striped">
		        <thead>
		        <tr>
		        	<!-- <th style="width: 150px;">Restaurante</th> -->
		        	<th><fmt:message key="food"/></th>
		            <th><fmt:message key="startDate"/></th>
		            <th><fmt:message key="endDate"/></th>
		            <th><fmt:message key="status"/></th>
		            <th><fmt:message key="municipio"/></th>
		            <th> <spring:url value="/offers/food/new" var="newFoodUrl">
		    </spring:url>
		    <!--  <a href="${fn:escapeXml(newFoodUrl)}" class="btn btn-default">Nueva oferta</a></th>-->
		        </tr>
		        </thead>
		        <tbody>
		        <c:forEach items="${foodOfferLs}" var="foodOffer">
		            <tr>
		                <td>
		                    <c:out value="${foodOffer.food}"/>
		                </td>
		                <td>
		                    <c:out value="${localDateTimeFormat.format(foodOffer.start)}"/>
		                </td>
		                <td>
		                    <c:out value="${localDateTimeFormat.format(foodOffer.end)}"/>
		                </td>
		                <td>
		                    <c:out value="${foodOffer.status}"/>
		                </td>
		                <td>
		                    <c:out value="${foodOffer.client.municipio}"/>
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
		</div>
    </c:if>
    <h2 style="font-family: 'Lobster'; text-align:center; font-size:200%;  color: rgb(0, 64, 128); padding:10px"><fmt:message key="nuOffers"/>:</h2>
	<c:if test="${empty nuOfferLs }">
		<p id="vacio" >No hay ninguna oferta por número de comensales creada.</p>
	</c:if>
	<c:if test="${not empty nuOfferLs }">
		<div class="table-responsive">
		    <table id="nuOfferTable" class="table table-striped">
		        <thead>
		        <tr>
		        	<!-- <th style="width: 150px;">Restaurante</th> -->
		            <th><fmt:message key="startDate"/></th>
		            <th><fmt:message key="endDate"/></th>
		            <th><fmt:message key="status"/></th>
		            <th><fmt:message key="municipio"/></th>
		            <th> <spring:url value="/offers/nu/new" var="newNuUrl">
		    </spring:url>
		    <!--  <a href="${fn:escapeXml(newNuUrl)}" class="btn btn-default">Nueva oferta</a></th>-->
		            
		        </tr>
		        </thead>
		        <tbody>
		        <c:forEach items="${nuOfferLs}" var="nuOffer">
		            <tr>
		                
		                <td>
		                    <c:out value="${localDateTimeFormat.format(nuOffer.start)}"/>
		                </td>
		                <td>
		                    <c:out value="${localDateTimeFormat.format(nuOffer.end)}"/>
		                </td>
		                <td>
		                    <c:out value="${nuOffer.status}"/>
		                </td>
		                <td>
		                    <c:out value="${nuOffer.client.municipio}"/>
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
    	</div>
    </c:if>
    
    <h2 style="font-family: 'Lobster'; text-align:center; font-size:200%;  color: rgb(0, 64, 128); padding:10px"><fmt:message key="speedOffers"/>:</h2>
	<c:if test="${empty speedOfferLs }">
		<p id="vacio" >No hay ninguna oferta por tiempo empleado en comer creada.</p>
	</c:if>
	<c:if test="${not empty speedOfferLs }">
		<div class="table-responsive">
		    <table id="speedOfferTable" class="table table-striped">
		        <thead>
		        <tr>
		        	<!-- <th style="width: 150px;">Restaurante</th> -->
		            <th><fmt:message key="startDate"/></th>
		            <th><fmt:message key="endDate"/></th>
		            <th><fmt:message key="status"/></th>
		            <th><fmt:message key="municipio"/></th>
		            <th> <spring:url value="/offers/speed/new" var="newSpeedUrl">
		    </spring:url>
		   <!-- <a href="${fn:escapeXml(newSpeedUrl)}" class="btn btn-default">Nueva oferta</a></th>-->
		            
		        </tr>
		        </thead>
		        <tbody>
		        <c:forEach items="${speedOfferLs}" var="speedOffer">
		            <tr>
		                
		                <td>
		                    <c:out value="${localDateTimeFormat.format(speedOffer.start)}"/>
		                </td>
		                <td>
		                    <c:out value="${localDateTimeFormat.format(speedOffer.end)}"/>
		                </td>
		                <td>
		                    <c:out value="${speedOffer.status}"/>
		                </td>
		                <td>
		                    <c:out value="${speedOffer.client.municipio}"/>
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
    	</div>
    </c:if>
    
    <h2 style="font-family: 'Lobster'; text-align:center; font-size:200%;  color: rgb(0, 64, 128); padding:10px"><fmt:message key="timeOffers"/>:</h2>
	<c:if test="${empty timeOfferLs }">
		<p id="vacio" >No hay ninguna oferta por franja horaria creada.</p>
	</c:if>
	<c:if test="${not empty timeOfferLs }">
		<div class="table-responsive">
		    <table id="timeOfferTable" class="table table-striped">
		        <thead>
		        <tr>
		        	<!-- <th style="width: 150px;">Restaurante</th> -->
		            <th><fmt:message key="startDate"/></th>
		            <th><fmt:message key="endDate"/></th>
		            <th><fmt:message key="status"/></th>
		            <th><fmt:message key="municipio"/></th>
		            <th><spring:url value="/offers/time/new" var="newTimeUrl">
		    </spring:url>
		    <!--<a href="${fn:escapeXml(newTimeUrl)}" class="btn btn-default">Nueva oferta</a> </th>-->
		        </tr>
		        </thead>
		        <tbody>
		        	<c:forEach items="${timeOfferLs}" var="timeOffer">
		            <tr>
		                
		                <td>
		                    <c:out value="${localDateTimeFormat.format(timeOffer.start)}"/>
		                </td>
		                <td>
		                    <c:out value="${localDateTimeFormat.format(timeOffer.end)}"/>
		                </td>
		                <td>
		                    <c:out value="${timeOffer.status}"/>
		                </td>
		                <td>
		                    <c:out value="${timeOffer.client.municipio}"/>
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
    	</div>
    </c:if>
</cheapy:layout>
