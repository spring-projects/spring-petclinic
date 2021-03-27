<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>

<cheapy:layout pageName="foodOffer">

    <h2>Oferta por plato espec�fico</h2>


    <table class="table table-striped">
        <tr>
            <th>Inicio de la oferta</th>
            <td><b><c:out value="${foodOffer.start}"/></b></td>
        </tr>
        <tr>
            <th>Fin de la oferta</th>
            <td><c:out value="${foodOffer.end}"/></td>
        </tr>
		<tr>
            <th>Plato en oferta</th>
            <td><c:out value="${foodOffer.food}"/></td>
        </tr>
        <tr>
            <th>Descuento</th>
            <td><c:out value="${foodOffer.discount}"/></td>
        </tr>
        <tr>
            <th>Codigo de la oferta</th>
            <td><c:out value="${foodOffer.code}"/></td>
        </tr>
    </table>

    <spring:url value="{foodOfferId}/edit" var="editUrl">
    <spring:param name="foodOfferId" value="${foodOffer.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar oferta</a>
    
    <spring:url value="{foodOfferId}/disable" var="editUrl">
    <spring:param name="foodOfferId" value="${foodOffer.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Desactivar oferta</a>

</cheapy:layout>
