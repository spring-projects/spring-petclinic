<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<%@ attribute name="menuName" required="true" rtexprvalue="true"
              description="Name of the active menu: home, owners, vets or error" %>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <spring:url value="/resources/images/banner-graphic.png" var="banner"/>
            <img class="img-responsive" src="${banner}"/>
        </div>
    </div>
</div>

<div class="container">
    <petclinic:menu name="${menuName}"/>
</div>
