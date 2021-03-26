<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="foodOffers">
    <h2>
        <c:if test="${foodOffer['new']}">New </c:if> FoodOffer
    </h2>
    <form:form modelAttribute="foodOffer" class="form-horizontal" id="add-foodOffer-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Start Date" name="start"/>
            <petclinic:inputField label="End Date" name="end"/>
            <petclinic:inputField label="Food" name="food"/>
            <petclinic:inputField label="Discount" name="discount"/>
            <petclinic:inputField label="Units" name="units"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${foodOffer['new']}">
                        <button class="btn btn-default" type="submit">Add Food Offer</button>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
