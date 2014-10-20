<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" 
%><%@ taglib prefix="spring" uri="http://www.springframework.org/tags"
%><%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"
%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" 
%><c:catch var="exception"><fmt:parseDate var="parsedDate" value="${param.date}" pattern="EEE MMM dd HH:mm:ss zzz yyyy" type="both"
/><fmt:formatDate var="formattedDate" value="${parsedDate}" type="date" pattern="yyyy-MM-dd"
/>${formattedDate}</c:catch><c:if test="${ exception != null }">${param.date}</c:if>