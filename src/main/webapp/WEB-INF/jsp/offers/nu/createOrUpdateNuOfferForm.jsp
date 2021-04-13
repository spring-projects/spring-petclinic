<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>

<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.css">

     -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.3/moment-with-locales.js"></script>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<script src="http://cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/a549aa8780dbda16f6cff545aeabc3d71073911e/src/js/bootstrap-datetimepicker.js"></script>

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet"/>

<link href="http://cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/a549aa8780dbda16f6cff545aeabc3d71073911e/build/css/bootstrap-datetimepicker.css" rel="stylesheet"/>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.3/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/2.4.4/js/bootstrap-datetimepicker.min.js"></script>

<cheapy:layout pageName="NumOffers">
    <h2 style="text-align:center;padding:5px">
        <c:if test="${nuOffer['new']}"><fmt:message key="new"/> </c:if> <fmt:message key="nuOffer"/>
    </h2>    
    
    <form:form modelAttribute="nuOffer" class="form-horizontal" id="add-nuOffer-form">
        <div class="form-group has-feedback">
            <form:hidden path="id"/>
            <form:hidden path="code"/>
            <form:hidden path="status"/>
            
           <!--  <div class="container">
			   <div class="row">
			      <div class='col-sm-6'>
			         <div class="form-group">
			            <div class='input-group date' id='datetimepicker1'>
		                    <input type='text' class="form-control" />
		                    <span class="input-group-addon">
		                            <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>
					</div>
			      </div>
			      <script type="text/javascript">
			         $(function () {
			             $('#datetimepicker1').datetimepicker();
			         });
			      </script>
			   </div>
			</div>
			 -->
			
			
			<cheapy:inputField label="Fecha de Inicio" placeholder="dd/MM/yyyy HH:mm" name="start"/>
            <cheapy:inputField label="Fecha de Fin"  placeholder="dd/MM/yyyy HH:mm" name="end"/> 
            <cheapy:inputField label="Número de comensales (nivel Oro)" placeholder="XX (Ej. 6)" name="gold"/>
            <cheapy:inputField label="Descuento de nivel oro" placeholder="XX% (Ej. 30)" name="discountGold"/>
            <cheapy:inputField label="Número de comensales (nivel Plata)" placeholder="XX (Ej. 4)" name="silver"/>
            <cheapy:inputField label="Descuento de plata" placeholder="XX% (Ej. 15)" name="discountSilver"/>
            <cheapy:inputField label="Número de comensales (nivel Bronce)" placeholder="XX (Ej. 2)" name="bronze"/>
			<cheapy:inputField label="Descuento de bronce" placeholder="XX% (Ej. 5)" name="discountBronze"/>
 
            </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<div class="btn-mod">
	                <c:choose>
	                    <c:when test="${nuOffer['new']}">
	                        <button class="btn btn-default" type="submit" style="font-family: 'Lobster'; font-size: 20px;">
	                        <span class="glyphicon glyphicon-floppy-save" aria-hidden="true" style="padding: 5px"> </span>
	                        Crear oferta</button>
	                    </c:when>
	                    <c:otherwise>
	                        <button class="btn btn-default" type="submit" style="font-family: 'Lobster'; font-size: 20px;">
	                        <span class="glyphicon glyphicon-floppy-save" aria-hidden="true" style="padding: 5px"> </span>
	                        Modificar</button>
	                    </c:otherwise>
	                </c:choose>
                </div>
            </div>
        </div>
     
    </form:form>
   	
	
</cheapy:layout>
