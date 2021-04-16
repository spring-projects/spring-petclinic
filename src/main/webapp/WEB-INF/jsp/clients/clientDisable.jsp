<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="client">

	<jsp:body>
        <h2 class="text-center" style="font-family: 'Lobster'; font-size: 30px; color: rgb(0, 64, 128); padding:30px"><em>¿Está seguro de que quiere eliminar su cuenta?</em></h2>
      		
      		<form:form modelAttribute="client" class="form-horizontal">
            
				      <div class="btns-edit2">
        			<button type="submit" style="font-family: 'Lobster'; font-size: 20px;">
        			<span class="glyphicon glyphicon glyphicon-trash" aria-hidden="true" style="padding: 5px"> </span>
        			Dar de baja</button>
        		</div>
        	</form:form>
        
    </jsp:body>
    
</cheapy:layout>
