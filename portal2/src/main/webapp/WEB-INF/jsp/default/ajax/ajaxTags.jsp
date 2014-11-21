<%@ page session="false" language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="objectTags" value=""/>
<c:if test="${!empty model.user && !empty model.user.socialTags}">
	<c:forEach items="${model.user.socialTags}" var="item" varStatus="status">

		<c:if test="${!empty model.user && !empty model.user.socialTags}">
               <c:if test="${model.document == item.europeanaUri}">
               	
               	<c:set var="divide" value=""/>
				<c:if test="${fn:length(objectTags)>0}">
               		<c:set var="divide" value="-"/>
				</c:if>
				<c:set var="objectTags" value="${objectTags}${divide}${item.id}"/>
			</c:if>
		</c:if>
	</c:forEach>

	{"reply":
		{"success":${model.success}

			<c:if test="${fn:length(objectTags)>0}">
				<c:set var="objectTags" value="${fn:split(objectTags, '-')}"/>
	        	, "tags":[
	        		<c:forEach items="${objectTags}" var="item" varStatus="status">${item}<c:if test="${!status.last}">,</c:if></c:forEach>
	        	]
			</c:if>
		}
	}	
</c:if>