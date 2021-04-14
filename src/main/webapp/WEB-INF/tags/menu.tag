<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, ofertas, contactanos, login"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<cheapy:menuItem active="${name eq 'home'}" url="/"
					title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Inicio</span>
				</cheapy:menuItem>
				
				<cheapy:menuItem active="${name eq 'ofertas'}" url="/offers" title="ofertas">
					<span class="glyphicon glyphicon-cutlery" aria-hidden="true"></span>
					<span>Ver ofertas</span>
				</cheapy:menuItem>
				
				<sec:authorize access="hasAnyAuthority('client')">
				<cheapy:menuItem active="${name eq 'ofertasM'}" url="/myOffers" title="misOfertas">
					<span class="glyphicon glyphicon-cutlery" aria-hidden="true"></span>
					<span>Mis ofertas</span>
				</cheapy:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAnyAuthority('admin')">
				<cheapy:menuItem active="${name eq 'clientes'}" url="/administrators/clients" title="clients">
					<span class="glyphicon " aria-hidden="true"></span>
					<span>Clientes</span>
				</cheapy:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAnyAuthority('admin')">
				<cheapy:menuItem active="${name eq 'usuarios'}" url="/administrators/usuarios" title="usuarios">
					<span class="glyphicon " aria-hidden="true"></span>
					<span>Usuarios</span>
				</cheapy:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAnyAuthority('admin')">
				<cheapy:menuItem active="${name eq 'registro'}" url="/administrators/offersRecord" title="offersRecord">
					<span class="glyphicon " aria-hidden="true"></span>
					<span>Registro de ofertas</span>
				</cheapy:menuItem>
				</sec:authorize>
				<!--  
				<cheapy:menuItem active="${name eq 'contactanos'}" url="/contactanos"
					title="contactanos">
					<span class="glyphicon glyphicon-earphone" aria-hidden="true"></span>
					<span>Contï¿½ctanos</span>
				</cheapy:menuItem>
				-->
				<sec:authorize access="isAuthenticated()">
					<cheapy:menuItem active="${name eq 'reviews'}" url="/reviewsList/0" title="opiniones">
						<span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>
						<span>Reseñas</span>
					</cheapy:menuItem>
					<cheapy:menuItem active="${name eq 'reviewsN'}" url="/reviews/new" title="valóranos">
						<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
						<span>Valóranos</span>
					</cheapy:menuItem>
				</sec:authorize>
			</ul>
			
			<ul class="nav navbar-nav navbar-right">
	   
                <sec:authorize access="hasAnyAuthority('client')">
					<cheapy:menuItem active="${name eq 'miPerfil'}" url="/clients/show" title="miPerfil">
						<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
						<span>Mi perfil</span>
					</cheapy:menuItem> 		           		            
		        </sec:authorize>
				<sec:authorize access="hasAnyAuthority('usuario')">
					<cheapy:menuItem active="${name eq 'miPerfil'}" url="/usuarios/show" title="miPerfil">
						<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
						<span>Mi perfil</span>
					</cheapy:menuItem> 		           		            
		        </sec:authorize>
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Iniciar sesión</a></li>
					<!--<li><a href="<c:url value="/users/new" />">Register</a></li>-->
				</sec:authorize>
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/users/new" />">Registrarse Usuario</a></li>
					<!--<li><a href="<c:url value="/users/new" />">Register</a></li>-->
				</sec:authorize>
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/clients/new" />">Registrarse Cliente</a></li>
					<!--<li><a href="<c:url value="/users/new" />">Register</a></li>-->
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span>
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row" >
										<div class="col-lg-4" style="">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size" ></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<form action="/logout" method=post>
												<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
												<input type="submit" value="logout" style="align-content:center;color:white;background-color:#004080;padding:10px; border:none; text-align:center">
											</form>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>
<!-- 							
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="#" class="btn btn-primary btn-block">My Profile</a>
												<a href="#" class="btn btn-danger btn-block">Change
													Password</a>
											</p>
										</div>
									</div>
								</div>
							</li>
-->
						</ul></li>
				</sec:authorize>
			</ul>
		</div>
	</div>
</nav>
