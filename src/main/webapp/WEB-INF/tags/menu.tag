<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of the active menu: home, owners, vets or error" %>

<nav class="navbar navbar-default" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#main-navbar">
                <span class="sr-only"><os-p>Toggle navigation</os-p></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <div class="navbar-collapse collapse" id="main-navbar">
            <ul class="nav navbar-nav navbar-right">

                <petclinic:menuItem active="${name eq 'home'}" url="/" title="home page">
                    <span class="glyphicon glyphicon-home" aria-hidden="true"></span>
                    <span>Home</span>
                </petclinic:menuItem>

                <petclinic:menuItem active="${name eq 'owners'}" url="/owners/find.html" title="find owners">
                    <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                    <span>Find owners</span>
                </petclinic:menuItem>

                <petclinic:menuItem active="${name eq 'vets'}" url="/vets.html" title="veterinarians">
                    <span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
                    <span>Veterinarians</span>
                </petclinic:menuItem>

                <petclinic:menuItem active="${name eq 'error'}" url="/oups.html"
                            title="trigger a RuntimeException to see how it is handled">
                    <span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span>
                    <span>Error</span>
                </petclinic:menuItem>

            </ul>
        </div>
    </div>
</nav>
