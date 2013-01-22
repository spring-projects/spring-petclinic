<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html lang="en">


<body>
	<head>
			<spring:url value="/webjars/jquery/1.8.2/jquery.js" var="jQuery" />
			<script src="${jQuery}"></script>
			
			<spring:url value="/webjars/jquery-ui/1.9.1/js/jquery-ui-1.9.1.custom.js" var="jQueryUi" />
			<script src="${jQueryUi}"></script>
			
			<spring:url value="/webjars/jquery-ui/1.9.1/css/smoothness/jquery-ui-1.9.1.custom.css" var="jQueryUiCss" />
			<link href="${jQueryUiCss}" rel="stylesheet"></link>
				
	
	</head>
  	<div class="container">
			
<script>
	        $(function() {
	            $( "#birthDate" ).datepicker();
	        });
	    </script>
		<form:form modelAttribute="pet" class="form-horizontal">
			<fieldset>			
					<div class="control-group" id="birthDate">
						<label class="control-label">Birth Date</label>
						<div class="controls">
							<form:input path="birthDate"  />
						</div>
					</div>
					
				</fieldset>	
			</form:form>	
  	</div>
</body>

</html>
