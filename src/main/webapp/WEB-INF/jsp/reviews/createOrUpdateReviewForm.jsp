<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>

<cheapy:layout pageName="reviewsN">
    <h2>
        <c:if test="${review['new']}">Nueva </c:if> Reseña
    </h2>
    <form:form modelAttribute="review" class="form-horizontal" id="add-review-form">
        <div class="form-group has-feedback">
        	<form:hidden path="id"/>
            <cheapy:textAreaField label="Opinión" name="opinion"/>
            <cheapy:inputField label="Estrellas" name="stars"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${review['new']}">
                        <button class="btn btn-default" type="submit">Crear Reseña</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Modificar Reseña</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</cheapy:layout>
