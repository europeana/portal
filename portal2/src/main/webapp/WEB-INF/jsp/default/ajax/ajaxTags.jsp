<%@ page session="false" language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	{"reply":
		{"success":${model.success}
			<c:if test="${!empty model.user && !empty model.user.socialTags}">,"tags":[<c:forEach items="${model.user.socialTags}" var="item" varStatus="status">${item.id}<c:if test="${!status.last}">,</c:if></c:forEach>]
			</c:if>
			<c:if test="${model[debug] && !empty model.debug && model.exception}">,"exception":"${model.exception}"</c:if>
		}
	}
