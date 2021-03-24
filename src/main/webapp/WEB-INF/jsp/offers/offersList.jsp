<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>

<cheapy:layout pageName="owners">
    <h2>Ofertas por plato específico</h2>

    <table id="foodOfferTable" class="table table-striped">
        <thead>
        <tr>
        	<!-- <th style="width: 150px;">Restaurante</th> -->
        	<th style="width: 150px;">Plato</th>
            <th style="width: 150px;">Fecha inicio</th>
            <th style="width: 200px;">Fecha fin</th>
            
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${foodOfferLs}" var="foodOffer">
            <tr>
                <%-- <td>
                    <spring:url value="/offers/food/{offerId}" var="foodOfferUrl">
                        <spring:param name="offerId" value="${foodOffer.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(foodOfferUrl)}"><c:out value="${foodOffer.client.username}"/></a>
                </td> --%>
                <td>
                    <c:out value="${foodOffer.food}"/>
                </td>
                <td>
                    <c:out value="${foodOffer.start}"/>
                </td>
                <td>
                    <c:out value="${foodOffer.end}"/>
                </td>
                  
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <h2>Ofertas por número de comensales</h2>

    <table id="nuOfferTable" class="table table-striped">
        <thead>
        <tr>
        	<!-- <th style="width: 150px;">Restaurante</th> -->
            <th style="width: 150px;">Fecha inicio</th>
            <th style="width: 200px;">Fecha fin</th>
            
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${nuOfferLs}" var="nuOffer">
            <tr>
                <%-- <td>
                    <spring:url value="/offers/nu/{offerId}" var="nuOfferUrl">
                        <spring:param name="offerId" value="${nuOffer.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(nuOfferUrl)}"><c:out value="${nuOffer.client.username}"/></a>
                </td> --%>
                <td>
                    <c:out value="${nuOffer.start}"/>
                </td>
                <td>
                    <c:out value="${nuOffer.end}"/>
                </td>
                  
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <h2>Ofertas por plato específico</h2>

    <table id="speedOfferTable" class="table table-striped">
        <thead>
        <tr>
        	<!-- <th style="width: 150px;">Restaurante</th> -->
            <th style="width: 150px;">Fecha inicio</th>
            <th style="width: 200px;">Fecha fin</th>
            
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${speedOfferLs}" var="speedOffer">
            <tr>
                <%-- <td>
                    <spring:url value="/offers/speed/{offerId}" var="speedOfferUrl">
                        <spring:param name="offerId" value="${speedOffer.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(speedOfferUrl)}"><c:out value="${speedOffer.client.username}"/></a>
                </td> --%>
                <td>
                    <c:out value="${speedOffer.start}"/>
                </td>
                <td>
                    <c:out value="${speedOffer.end}"/>
                </td>
                  
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <h2>Ofertas por plato específico</h2>

    <table id="timeOfferTable" class="table table-striped">
        <thead>
        <tr>
        	<!-- <th style="width: 150px;">Restaurante</th> -->
            <th style="width: 150px;">Fecha inicio</th>
            <th style="width: 200px;">Fecha fin</th>
            
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${timeOfferLs}" var="timeOffer">
            <tr>
                <%-- <td>
                    <spring:url value="/offers/time/{offerId}" var="timeOfferUrl">
                        <spring:param name="offerId" value="${timeOffer.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(timeOfferUrl)}"><c:out value="${timeOffer.client.username}"/></a>
                </td> --%>
                <td>
                    <c:out value="${timeOffer.start}"/>
                </td>
                <td>
                    <c:out value="${timeOffer.end}"/>
                </td>
                  
            </tr>
        </c:forEach>
        </tbody>
    </table>
</cheapy:layout>
