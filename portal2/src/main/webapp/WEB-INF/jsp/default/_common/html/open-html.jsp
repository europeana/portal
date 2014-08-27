<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:set var="xmlns" value='' />
<c:if test="${model.pageName == 'full-doc.html' }">
  <c:set var="xmlns" value='xmlns:cc="http://creativecommons.org/ns#" xmlns:og="http://ogp.me/ns#" xmlns:fb="http://www.facebook.com/2008/fbml" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:xhv="http://www.w3.org/1999/xhtml/vocab#"'/>
</c:if>

<!--[if IE 8]>
<html ${xmlns} class="ie ie8" lang="${lang}">
<![endif]-->

<!--[if IE 9]>
	<html ${xmlns} class="ie ie9" lang="${lang}">
<![endif]-->


<!--[if !IE]>-->
<html ${xmlns} lang="${lang}">
<!--<![endif]-->

<head<c:if test="${model.pageName == 'full-doc.html'}"> about="${model.document.cannonicalUrl}"</c:if>>
  <meta charset="utf-8" />
  <c:set var="title" value="${model.pageTitle}" />

  <c:if test="${fn:indexOf(title, '| http')>-1}">
   	<c:set var="titleRoot">${fn:substringBefore(title, '| http')} | </c:set>
   	<c:set var="titleNew"></c:set>
    <c:forEach items="${model.fields}" var="data">
    	<c:if test="${data.fieldName == 'dc:creator'}">
			<c:forEach items="${data.fieldValues}" var="value" varStatus="valueStatus">
				<c:if test="${!empty value.decorator && !empty value.decorator.prefLabel}">
			    	<c:forEach items="${value.decorator.prefLabel}" var="item" varStatus="t">
			    		<c:if test="${item.key=='en'}">
			    			<c:set var="titleNew">${fn:replace( fn:replace(item.value, '[', '') , ']', '')}</c:set>
			    		</c:if>
			    	</c:forEach>
		        </c:if>
			</c:forEach>
    	</c:if>
    </c:forEach>
   	<c:if test="${fn:length(titleNew) > 0 }">
   		<c:set var="title">${titleRoot}&nbsp;${titleNew}</c:set>
   	</c:if>
  </c:if>
  
  <title>${title}</title>
  
  
  
  <%@ include file="/WEB-INF/jsp/default/_common/html/links.jsp" %>
  <%@ include file="/WEB-INF/jsp/default/_common/html/meta.jsp" %>
</head>
<body class="locale-${model.locale}">

<c:if test="${model.pageName == 'index.html'}">
  <c:if test="${not empty model.announceMsg}">${model.announceMsg}</c:if>
</c:if>