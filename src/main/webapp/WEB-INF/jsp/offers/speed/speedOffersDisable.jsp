<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<cheapy:layout pageName="speedOffer">

	<jsp:body>
        <h2 class="text-center" style="font-family: 'Lobster'; font-size: 30px; color: rgb(0, 64, 128); padding:30px"><em>¿Está seguro de que quiere eliminar su oferta?</em></h2>
      		
      		 <form:form modelAttribute="speedOffer" class="form-horizontal">

	            <input type="hidden" name="gold" value="${speedOffer.gold}" />
	            <input type="hidden" name="discountGold" value="${speedOffer.discountGold}" />
	            <input type="hidden" name="silver" value="${speedOffer.silver}" />
	            <input type="hidden" name="discountSilver" value="${speedOffer.discountSilver}" />
	            <input type="hidden" name="bronze" value="${speedOffer.bronze}" />
	            <input type="hidden" name="discountBronze" value="${speedOffer.discountBronze}" />

        		<div class="btns-edit">
        			<button type="submit" style="font-family: 'Lobster'; font-size: 20px;">
        			<span class="glyphicon glyphicon glyphicon-trash" aria-hidden="true" style="padding: 5px"> </span>
        			Dar de baja</button>
        		</div>
        	</form:form>
        
            <div class="btn-return">
			    <button type="button" role="link" onclick="goBack()" style="font-family: 'Lobster'; font-size: 20px;">
			    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true" style="padding: 5px"> </span>
			    <fmt:message key="return"/> </button>
		    </div>
           <script>
				function goBack() {
				  window.history.back()
				}
			</script>
           
    </jsp:body>
</cheapy:layout>
