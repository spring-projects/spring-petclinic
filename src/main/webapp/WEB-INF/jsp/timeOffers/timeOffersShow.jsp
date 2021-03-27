<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>

<cheapy:layout pageName="timeOffer">

    <h2>Oferta por franja horaria</h2>


    <table class="table table-striped">
        <tr>
            <th>Inicio de la oferta</th>
            <td><b><c:out value="${timeOffer.start}"/></b></td>
        </tr>
        <tr>
            <th>Fin de la oferta</th>
            <td><c:out value="${timeOffer.end}"/></td>
        </tr>
        <tr>
            <th>Descuento</th>
            <td><c:out value="${timeOffer.discount}"/></td>
        </tr>
        <tr>
            <th>Codigo de la oferta</th>
            <td><c:out value="${timeOffer.code}"/></td>
        </tr>
    </table>
    
    <spring:url value="{timeOfferId}/edit" var="editUrl">
    <spring:param name="timeOfferId" value="${timeOffer.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar oferta</a>
    
    <spring:url value="{timeOfferId}/disable" var="editUrl">
    <spring:param name="timeOfferId" value="${timeOffer.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Desactivar oferta</a>

</cheapy:layout>
