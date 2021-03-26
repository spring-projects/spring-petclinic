<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="speedOffers">
    <h2>
        <c:if test="${speedOffer['new']}">New </c:if> SpeedOffer
    </h2>
    <form:form modelAttribute="speedOffer" class="form-horizontal" id="add-speedOffer-form">
        <div class="form-group has-feedback">
            <form:hidden path="id"/>
            <form:hidden path="code"/>
            <form:hidden path="type"/>
            <petclinic:inputField label="Start Date" name="start"/>
            <petclinic:inputField label="End Date" name="end"/>
            <petclinic:inputField label="Gold" name="gold"/>
            <petclinic:inputField label="Gold Discount" name="discountGold"/>
            <petclinic:inputField label="Silver" name="silver"/>
            <petclinic:inputField label="Silver Discount" name="discountSilver"/>
            <petclinic:inputField label="Bronze" name="bronze"/>
            <petclinic:inputField label="Bronze Discount" name="discountBronze"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${speedOffer['new']}">
                        <button class="btn btn-default" type="submit">Crear oferta</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Modificar</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
