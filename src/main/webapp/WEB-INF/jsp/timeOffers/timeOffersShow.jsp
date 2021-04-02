<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="timeOffer">

    <h2 style="text-align:center;padding:5px"><fmt:message key="timeOffer"/></h2>


    <table id="timeOfferTable" class="table table-striped">
        <thead>
        <tr>
            <th><fmt:message key="offerBeginning"/></th>
            <td><c:out value="${localDateTimeFormat.format(timeOffer.start)}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="offerEnding"/></th>
            <td><c:out value="${localDateTimeFormat.format(timeOffer.end)}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="init"/></th>
            <td><c:out value="${timeOffer.init}h"/></td>
        </tr>
        <tr>
            <th><fmt:message key="finish"/></th>
            <td><c:out value="${timeOffer.finish}h"/></td>
        </tr>
        <tr>
            <th><fmt:message key="discount"/></th>
            <td><c:out value="${timeOffer.discount}%"/></td>
        </tr>
        <tr>
            <th><fmt:message key="offerCode"/></th>
            <td><c:out value="${timeOffer.code}"/></td>
        </tr>
        </thead>
    </table>

	<div class="btn-menu">
	    <div class="btn-return">
		    <button type="button" role="link" onclick="goBack()" style="font-family: 'Lobster'; font-size: 20px;"> 
		    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true" style="padding: 5px"> </span> 
		    <fmt:message key="return"/> </button>
	    </div>

		<div class="btns-edit">
		    <spring:url value="{timeOfferId}/edit" var="editUrl">
		    <spring:param name="timeOfferId" value="${timeOffer.id}"/>
		    </spring:url>
		    <button type="button" role="link" onclick="window.location='${fn:escapeXml(editUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
            <span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
	        Editar oferta</button>
	    
		   	<spring:url value="{timeOfferId}/disable" var="editUrl">
		    <spring:param name="timeOfferId" value="${timeOffer.id}"/>
		    </spring:url>
		    <button type="button" role="link" onclick="window.location='${fn:escapeXml(editUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
            <span class="glyphicon glyphicon glyphicon-trash" aria-hidden="true" style="padding: 5px"> </span>
	        Desactivar oferta</button>
	    </div>
    </div>
    
    <script>
		function goBack() {
		  window.history.back()
		}
	</script>

  
</cheapy:layout>
