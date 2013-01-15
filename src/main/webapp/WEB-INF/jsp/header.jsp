<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!--
	PetClinic :: a Spring Framework demonstration
-->

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>PetClinic :: a Spring Framework demonstration</title>	
  

  	<spring:url value="/webjars/bootstrap/2.2.1/css/bootstrap.min.css" var="bootstrapCss" />
	<link href="${bootstrapCss}" rel="stylesheet"/>
	
	<spring:url value="/resources/css/petclinic.css" var="petclinicCss" />
	<link href="${petclinicCss}" rel="stylesheet"/>
</head>


