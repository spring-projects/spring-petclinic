<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="reviewsN">
    <h2 style="font-family: 'Lobster'; text-align:center; font-size:200%;  color: rgb(0, 64, 128); padding:10px">
    	<c:if test="${review['new']}">Nueva </c:if> ReseÃ±a
    </h2>
    <form:form modelAttribute="review" class="form-horizontal" id="add-review-form">
        <div class="form-group has-feedback">
        	<form:hidden path="id"/>
            <cheapy:textAreaField label="Opinión" name="opinion"/>
           <!-- <cheapy:inputField label="Estrellas" name="stars"/>  -->
            <cheapy:ratingStar label="Valoración" name="stars" disabled="false"></cheapy:ratingStar>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<div class="btn-mod">
                <c:choose>
                    <c:when test="${review['new']}">
                        <button class="btn btn-default" type="submit" style="font-family: 'Lobster'; font-size: 20px;">
	                        <span class="glyphicon glyphicon-floppy-save" aria-hidden="true" style="padding: 5px"> </span>
	                        Crear reseÃ±a</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Modificar ReseÃ±a</button>
                    </c:otherwise>
                </c:choose>
                </div>
            </div>
        </div>
        
        
	
    </form:form>
</cheapy:layout>
