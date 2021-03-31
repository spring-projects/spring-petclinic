<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="reviews">
    <h2 style="text-align:center;padding:5px"><fmt:message key="reviews"/></h2>

    <table id="reviewTable" class="table table-striped">
        <thead>
        <tr>
        	<!-- <th style="width: 150px;">Restaurante</th> -->
        	<th><fmt:message key="stars"/></th>
            <th><fmt:message key="opinion"/></th>
            <th> </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${reviewsLs}" var="review">
            <tr>
<!--                 <td> -->
<%--                     <c:out value="nombre por definir"/> <!-- ${review.usuario.nombre},${review.usuario.apellidos}  --> --%>
<!--                 </td> -->
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
        </tbody>
    </table>
    
   
</cheapy:layout>
