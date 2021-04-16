<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="reviews">
    <h2 style="font-family: 'Lobster'; text-align:center; font-size:200%;  color: rgb(0, 64, 128); padding:10px"><fmt:message key="reviews"/></h2>

    <table id="reviewTable" class="table table-striped">
        <thead>
	        <tr>
	        	<!-- <th style="width: 150px;">Restaurante</th> -->
	        	<th><fmt:message key="user"/></th>
	        	<th><fmt:message key="stars"/></th>
	            <th><fmt:message key="opinion"/></th>
	            <th> </th>
	        </tr>
        </thead>
        <tbody>
        <c:choose>
	        <c:when test="${empty reviewsLs}">
	        	<tr><td colspan="4"><em><c:out value="No se ha realizado ninguna valoración por el momento."/></em></td></tr>
	        </c:when>
	        <c:otherwise>
	        <c:forEach items="${reviewsLs}" var="review">
	            <tr>
	<!--                 <td> -->
	<%--                     <c:out value="nombre por definir"/> <!-- ${review.usuario.nombre},${review.usuario.apellidos}  --> --%>
	<!--                 </td> -->
	                <td>
	                    <c:out value="${review.escritor.username}"/>
	                </td>
	                <td>
	                    <c:out value="${review.stars}"/>
	                </td>
	                <td>
	                    <c:out value="${review.opinion}"/>
	                </td>
	                <td>
		                <spring:url value="/reviews/{reviewId}" var="reviewUrl">
		                        <spring:param name="reviewId" value="${review.id}"/>
		                </spring:url>
		                <div class="btn-detalles">
	                		<button type="button" role="link" onclick="window.location='${fn:escapeXml(reviewUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
	                		<span class="glyphicon glyphicon-info-sign" aria-hidden="true" style="padding: 5px"> </span>
		                	<fmt:message key="details"/></button>
	            		</div>
	                </td> 
	                  
	            </tr>
	        </c:forEach>
	        </c:otherwise>
        </c:choose>
        </tbody>
    </table>
    <div class="text-center">
    	<c:out value='Página ${page}'></c:out>
    </div>
    <c:if test='${page!=0}'>
    <div class="text-left">
    	<spring:url value="/reviewsList/{page}" var="reviewsListUrl">
    		<spring:param name="page" value="${page-1}"/>
    	</spring:url>
    	<button type="button" class="btn-pag" role="link" onclick="window.location='${fn:escapeXml(reviewsListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
		<span class="glyphicon 	glyphicon glyphicon-arrow-left" aria-hidden="true" style="padding: 5px"> </span>
		Pág. anterior</button>
 	</div>
    </c:if>
    
    <c:if test="${fn:length(reviewsLs) == 6}">
    <div class="text-right">
    	<spring:url value="/reviewsList/{page}" var="reviewsListUrl">
    		<spring:param name="page" value="${page+1}"/>
    	</spring:url>
    	<button type="button" class="btn-pag" role="link" onclick="window.location='${fn:escapeXml(reviewsListUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
		<span class="glyphicon 	glyphicon glyphicon-arrow-right" aria-hidden="true" style="padding: 5px"> </span>
		Pág. siguiente</button>
	</div>
	</c:if>
</cheapy:layout>
