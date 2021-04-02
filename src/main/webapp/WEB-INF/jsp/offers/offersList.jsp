<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="ofertas">
    <h2 style="text-align:center;padding:5px"><fmt:message key="foodOffers"/></h2>
	<c:if test="${empty foodOfferLs }">
		<p id="vacio" >No hay ninguna oferta por plato específico activa.</p>
	</c:if>
	<c:if test="${not empty foodOfferLs }">
    <table id="foodOfferTable" class="table table-striped">
        <thead>
        <tr>
        	<!-- <th style="width: 150px;">Restaurante</th> -->
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
    <h2 style="text-align:center;padding:5px"><fmt:message key="nuOffers"/></h2>
	<c:if test="${empty nuOfferLs }">
		<p id="vacio" >No hay ninguna oferta por plato específico activa.</p>
	</c:if>
	<c:if test="${not empty nuOfferLs }">
    <table id="nuOfferTable" class="table table-striped">
        <thead>
        <tr>
        	<!-- <th style="width: 150px;">Restaurante</th> -->
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
    </c:if>
    
    <h2 style="text-align:center;padding:5px"><fmt:message key="speedOffers"/></h2>
	<c:if test="${empty speedOfferLs }">
		<p id="vacio" >No hay ninguna oferta por plato específico activa.</p>
	</c:if>
	<c:if test="${not empty speedOfferLs }">
    <table id="speedOfferTable" class="table table-striped">
        <thead>
        <tr>
        	<!-- <th style="width: 150px;">Restaurante</th> -->
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
    </c:if>
    
    <h2 style="text-align:center;padding:5px"><fmt:message key="timeOffers"/></h2>
	<c:if test="${empty timeOfferLs }">
		<p id="vacio" >No hay ninguna oferta por plato específico activa.</p>
	</c:if>
	<c:if test="${not empty timeOfferLs }">
    <table id="timeOfferTable" class="table table-striped">
        <thead>
        <tr>
        	<!-- <th style="width: 150px;">Restaurante</th> -->
            <th><fmt:message key="startDate"/></th>
            <th><fmt:message key="endDate"/></th>
            <th><fmt:message key="init"/></th>
            <th><fmt:message key="finish"/></th>
            <th> </th>
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
    </c:if>
    
    <div class="btn-return">
		    <button type="button" role="link" onclick="goBack()" style="font-family: 'Lobster'; font-size: 20px;"> 
		    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true" style="padding: 5px"> </span> 
		    <fmt:message key="return"/> </button>
	</div>
    <script>
		function goBack() {
		  window.history.back()
		}
	</script>
</cheapy:layout>
