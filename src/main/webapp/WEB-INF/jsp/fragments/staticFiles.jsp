<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!--
PetClinic :: a Spring Framework demonstration
-->

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>PetClinic :: a Spring Framework demonstration</title>


    <spring:url value="/vendors/bootstrap/docs/assets/css/bootstrap.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

    <spring:url value="/resources/css/petclinic.css" var="petclinicCss"/>
    <link href="${petclinicCss}" rel="stylesheet"/>

    <spring:url value="/vendors/jquery/jquery.js" var="jQuery"/>
    <script src="${jQuery}"></script>

    <!-- jquery-ui.js file is really big so we only load what we need instead of loading everything -->
    <spring:url value="/vendors/jquery-ui/ui/jquery.ui.core.js" var="jQueryUiCore"/>
    <script src="${jQueryUiCore}"></script>

    <spring:url value="/vendors/jquery-ui/ui/jquery.ui.datepicker.js" var="jQueryUiDatePicker"/>
    <script src="${jQueryUiDatePicker}"></script>

    <!-- jquery-ui.css file is not that big so we can afford to load it -->
    <spring:url value="/vendors/jquery-ui/themes/base/jquery.ui.base.css" var="jQueryUiCss"/>
    <link href="${jQueryUiCss}" rel="stylesheet"/>
</head>
