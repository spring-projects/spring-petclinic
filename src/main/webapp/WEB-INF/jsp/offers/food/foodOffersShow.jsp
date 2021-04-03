<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="foodOffer">

    <h2 style="text-align:center;padding:5px"><fmt:message key="foodOffer"/></h2>


	
    <table class="table table-striped" id="foodOfferTable">
    	<thead>
        <tr>
            <th><fmt:message key="offerBeginning"/></th>
            <td><c:out value="${localDateTimeFormat.format(foodOffer.start)}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="offerEnding"/></th>
            <td><c:out value="${localDateTimeFormat.format(foodOffer.end)}"/></td>
        </tr>
		<tr>
            <th><fmt:message key="foodInOffer"/></th>
            <td><c:out value="${foodOffer.food}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="discount"/></th>
            <td><c:out value="${foodOffer.discount}%"/> </td>
        </tr>

        <tr>
            <th><fmt:message key="offerCode"/></th>
            <td><c:out value="${foodOffer.code}"/></td>
        </tr>
        </thead>
    </table>

    <div class="btn-menu">
	    
	<sec:authorize access="hasAnyAuthority('client')">
	<sec:authentication var="principal" property="principal" />
      <div class="btns-edit">
      	<c:if test="${ principal.username eq foodOffer.client.usuar.username}">
      		<c:if test="${foodOffer.status eq 'active' || foodOffer.status eq 'hidden' }">
      		
		        <spring:url value="{foodOfferId}/edit" var="editUrl">
		        <spring:param name="foodOfferId" value="${foodOffer.id}"/>
		        </spring:url>
		        <button type="button" role="link" onclick="window.location='${fn:escapeXml(editUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
		            <span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
		          Editar oferta</button>
	         </c:if>
		
		<c:if test="${foodOffer.status eq 'hidden' }">
	        <spring:url value="{foodOfferId}/activate" var="activateUrl">
	        <spring:param name="foodOfferId" value="${foodOffer.id}"/>
	        </spring:url>
	        <button type="button" role="link" onclick="window.location='${fn:escapeXml(activateUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
	            <span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
	          Activar oferta</button>
		</c:if>
		
		<c:if test="${foodOffer.status eq 'active' }">
	        <spring:url value="{foodOfferId}/disable" var="deactivateUrl">
	        <spring:param name="foodOfferId" value="${foodOffer.id}"/>
	        </spring:url>
	        <button type="button" role="link" onclick="window.location='${fn:escapeXml(deactivateUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
	            <span class="glyphicon glyphicon glyphicon-trash" aria-hidden="true" style="padding: 5px"> </span>
	          Desactivar oferta</button>
         </c:if>
        </c:if>
      </div>
      </sec:authorize>
    </div>
  	

</cheapy:layout>
