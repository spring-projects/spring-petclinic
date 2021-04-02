<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="review">

    <h2 style="text-align:center;padding:5px"><fmt:message key="review"/></h2>


    <table class="table table-striped" id="review-table">
        <tr>
            <th><fmt:message key="stars"/></th>
            <td><c:out value="${review.stars}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="opinion"/></th>
            <td><c:out value="${review.opinion}"/></td>
        </tr>
        
    </table>
    
    <div class="btn-return">
	    <button type="button" role="link" onclick="window.location='/reviews'" style="font-family: 'Lobster'; font-size: 20px;">
	    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true" style="padding: 5px"> </span>
	    <fmt:message key="return"/> </button>
    </div>

    <spring:url value="{reviewId}/edit" var="editUrl">
    <spring:param name="reviewId" value="${review.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar opinión</a>
    
  
</cheapy:layout>
