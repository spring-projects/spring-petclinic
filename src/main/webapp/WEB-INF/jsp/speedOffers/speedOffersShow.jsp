<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>

<cheapy:layout pageName="speedOffer">

    <h2>Oferta por comer veloz</h2>


    <table class="table table-striped">
        <tr>
            <th>Inicio de la oferta</th>
            <td><b><c:out value="${speedOffer.start}"/></b></td>
        </tr>
        <tr>
            <th>Fin de la oferta</th>
            <td><c:out value="${speedOffer.end}"/></td>
        </tr>
        <tr>
            <th>Meta oro</th>
            <td><c:out value="${speedOffer.gold}"/></td>
        </tr>
        <tr>
            <th>Descuento oro</th>
            <td><c:out value="${speedOffer.discountGold}"/></td>
        </tr>
        <tr>
            <th>Meta plata</th>
            <td><c:out value="${speedOffer.silver}"/></td>
        </tr>
        <tr>
            <th>Descuento plata</th>
            <td><c:out value="${speedOffer.discountSilver}"/></td>
        </tr>
        <tr>
            <th>Meta bronce</th>
            <td><c:out value="${speedOffer.bronze}"/></td>
        </tr>
        <tr>
            <th>Descuento bronce</th>
            <td><c:out value="${speedOffer.discountBronze}"/></td>
        </tr>
        <tr>
            <th>Codigo de la oferta</th>
            <td><c:out value="${speedOffer.code}"/></td>
        </tr>
    </table>

    <spring:url value="{speedOfferId}/edit" var="editUrl">
        <spring:param name="speedOfferId" value="${speedOffer.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar oferta</a>

</cheapy:layout>
