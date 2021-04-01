<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>

<cheapy:layout pageName="foodOffers">
    <h2>
        <c:if test="${foodOffer['new']}">Nueva </c:if> Oferta de plato específico
    </h2>
    
    <form:form modelAttribute="foodOffer" class="form-horizontal" id="add-foodOffer-form">
        <div class="form-group has-feedback">
        	<form:hidden path="id"/>
            <form:hidden path="code"/>
            <form:hidden path="status"/>
            <cheapy:inputField label="Fecha de inicio" placeholder="15/06/2021 14:00" name="start"/>
            <cheapy:inputField label="Fecha de fin" placeholder="15/06/2021 16:00" name="end"/>
            <cheapy:inputField label="Comida" name="food"/>
            <cheapy:inputField label="Descuento" name="discount"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${foodOffer['new']}">
                        <button class="btn btn-default" type="submit">Crear oferta</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Modificar</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</cheapy:layout>
