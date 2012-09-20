<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:if test="${!empty model.moreLikeThis}">
  <h3>
    <a href="#similar-content"><spring:message code="SimilarContent_t" /></a>
  </h3>
  <div id="similar-content" class="carousel">
    <div class="carousel-container" about="${model.document.id}">
      <ul>
        <c:forEach items="${model.moreLikeThis}" var="doc"><%-- doc is BriefBean --%>
          <li>
            <c:set var="relItemQuery" value='europeana_uri:"${model.document.id}"' />
            <a href="/${model.portalName}/${doc.fullDocUrl}" rel="rdfs:seeAlso" resource="${doc.id}" title="${doc.title[0]}">
              <img src="${imgBaseUrl}${doc.thumbnail}" alt="${doc.title[0]}" width="70" data-type="${fn:toLowerCase(doc.type)}" />
            </a>
          </li>
        </c:forEach>
      </ul>
    </div>
  </div>
</c:if>
