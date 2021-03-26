<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>

<cheapy:layout pageName="nuOffer">

    <h2>Oferta por n�mero de comensales</h2>


    <table class="table table-striped">
        <tr>
            <th>Inicio de la oferta</th>
            <td><b><c:out value="${nuOffer.start}"/></b></td>
        </tr>
        <tr>
            <th>Fin de la oferta</th>
            <td><c:out value="${nuOffer.end}"/></td>
        </tr>
        <tr>
            <th>Meta oro</th>
            <td><c:out value="${nuOffer.gold}"/></td>
        </tr>
        <tr>
            <th>Descuento oro</th>
            <td><c:out value="${nuOffer.discountGold}"/></td>
        </tr>
        <tr>
            <th>Meta plata</th>
            <td><c:out value="${nuOffer.silver}"/></td>
        </tr>
        <tr>
            <th>Descuento plata</th>
            <td><c:out value="${nuOffer.discountSilver}"/></td>
        </tr>
        <tr>
            <th>Meta bronce</th>
            <td><c:out value="${nuOffer.bronze}"/></td>
        </tr>
        <tr>
            <th>Descuento bronce</th>
            <td><c:out value="${nuOffer.discountBronze}"/></td>
        </tr>
        <tr>
            <th>Codigo de la oferta</th>
            <td><c:out value="${nuOffer.code}"/></td>
        </tr>
    </table>

    <spring:url value="{nuOfferId}/edit" var="editUrl">
    <spring:param name="nuOfferId" value="${nuOffer.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar Oferta</a>
    
    <spring:url value="{nuOfferId}/disable" var="editUrl">
    <spring:param name="nuOfferId" value="${nuOffer.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Eliminar Oferta</a>

</cheapy:layout>
