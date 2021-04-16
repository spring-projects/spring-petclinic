<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="ofertasM">
    <h2 style="font-family: 'Lobster'; text-align:center; font-size:200%;  color: rgb(0, 64, 128); padding:10px">Historial de Ofertas</h2>
	<c:if test="${empty datos }">
		<p id="vacio" >No hay ninguna oferta creada.</p>
	</c:if>
	<c:if test="${not empty datos }">
		<div class="table-responsive">
		    <table id="offerTable" class="table table-striped">
		        <thead>
		        <tr>
		        	<th>Restaurante</th>
		        	<th>Tipo de oferta</th>
		            <th><fmt:message key="startDate"/></th>
		            <th><fmt:message key="endDate"/></th>
		            <th><fmt:message key="status"/></th>
		            <th></th>
		        </tr>
		        </thead>
		        <tbody>
		        <c:forEach items="${datos}" var="datos">
		            <tr>
		                <td>
		                    <c:out value="${datos.key.client.name}"/>
		                </td>
		                <td>
		                	<c:if test="${datos.value == 'time'}">
		                    	<c:out value="Por franja horaria"/>
		                    </c:if>
		                    <c:if test="${datos.value == 'nu'}">
		                    	<c:out value="Por numero de comensales"/>
		                    </c:if>
		                    <c:if test="${datos.value == 'speed'}">
		                    	<c:out value="Por rapidez"/>
		                    </c:if>
		                    <c:if test="${datos.value == 'food'}">
		                    	<c:out value="Por plato especifico"/>
		                    </c:if>
		                </td>
		                <td>
		                    <c:out value="${localDateTimeFormat.format(datos.key.start)}"/>
		                </td>
		                <td>
		                    <c:out value="${localDateTimeFormat.format(datos.key.end)}"/>
		                </td>
		                <td>
		                	<c:if test="${datos.key.status == 'active'}">
		                    	<c:out value="Activa"/>
		                    </c:if>	
		                    <c:if test="${datos.key.status == 'hidden'}">
		                    	<c:out value="Oculta"/>
		                    </c:if>	
		                    <c:if test="${datos.key.status == 'inactive'}">
		                    	<c:out value="Inactiva"/>
		                    </c:if>		                 
		                </td>
		                
		                <td>
	                	<spring:url value="/offers/${datos.value}/${datos.key.id}" var="offerUrl">
	                        <spring:param name="foodOfferId" value="${foodOffer.id}"/>
	                	</spring:url>
	               		<div class="btn-detalles">
                			<button type="button" role="link" onclick="window.location='${fn:escapeXml(offerUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
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
</cheapy:layout>
