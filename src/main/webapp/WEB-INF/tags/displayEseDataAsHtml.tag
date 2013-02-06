<%@ tag trimDirectiveWhitespaces="true" %>

<%-- parameters --%>
<%@ attribute name="listCollection" required="true" type="java.lang.Object" %>
<%@ attribute name="wrapper" required="true" %>
<%@ attribute name="ugc" required="true" %>
<%@ attribute name="ess" required="true" %>

<%-- tag libs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="eu" tagdir="/WEB-INF/tags"%>
<%--
 * display-ese-data-as-fields
 *
 * ese (Europeana Semantic Elements) meta-data can also be accessed directly
 * using a fullbean method, /core/src/main/java/eu/europeana/core/querymodel/beans/FullBean.java,
 * which typically returns a collection that needs to be iterated
 * over e.g., <#list model.document.dcDescription as data>
 *
 * unfortunately this access does not provide ess (extended search service)
 * information such as the object is translatable or viewable in another
 * information service such as wikipedia, so it is preferable to alter the
 * list collections in /portal-full/src/main/java/eu/europeana/web/presentation/model/FullDocPage.java
 *
 * @var data.fieldName string - tag name of the field
 * @var data.fieldUniqueId string - unique id for the field
 * @var data.fieldValues array - collection of values associated with field
 * @var data.seperateLines bool - whether or not to put each value on a new line
 * @var data.showTranslationServices bool - whether or not translation of the field is appropriate
 * @var data.externalServices array - a collection of External Services associated with the field
 * @var data.ESSEnabled bool - whether or not the field has external services associated with it,
 *    e.g., the object can be searched for on wikipedia or imdb
 *
 * @author Dan Entous <contact@pennlinepublishing.com>
 * @modified 2011-06-08 15:25 GMT+1
--%>
<c:forEach items="${listCollection}" var="data" varStatus="fieldStatus">

  <c:set var="item_id" value="" />
  <c:if test='${"dc:description" == data.fieldName}'><c:set var="item_id"> id="item-description" </c:set></c:if>
  <c:if test='${"dc:subject" == data.fieldName}'><c:set var="item_id"> id="item-subject" </c:set></c:if>

  <c:set var="item_class" value=""/>

  <%-- If the content is UGC we skip the dc:source display --%>
  <c:if test="${!('dc:source' == data.fieldName && ugc)}">
    <%-- Semantic attributes --%>
    <c:set var="semanticAttributes">
      <eu:semanticAttributes field="${data.fieldName}" contextualEntity="${data.contextualEntity}" schemaOrgMapping="${model.schemaOrgMapping}" />
    </c:set>
    <c:set var="semanticUrl">
      <eu:semanticUrl field="${data.fieldName}" contextualEntity="${data.contextualEntity}" schemaOrgMapping="${model.schemaOrgMapping}" />
    </c:set>

    <<c:out value="${wrapper}"/>${' '}${item_id} class="item-metadata${item_class}">
      <%-- field's label --%>
      <span class="bold notranslate"><spring:message code="${data.fieldLabel}" />:</span>

      <%-- iterate over possible values for the given label --%>
      <c:forEach items="${data.fieldValues}" var="value" varStatus="valueStatus">

        <c:set var="localSemanticAttributes" value="${semanticAttributes}" />
        <c:set var="localSemanticUrl" value="${semanticUrl}" />
        <c:if test="${value.fieldName != data.fieldName}">
          <c:choose>
            <c:when test="${model.schemaOrgMapping[value.fieldName] != null}">
              <c:set var="localSemanticAttributes">
                <eu:semanticAttributes field="${value.fieldName}" contextualEntity="${value.contextualEntity}" schemaOrgMapping="${model.schemaOrgMapping}" />
              </c:set>
              <c:set var="localSemanticUrl">
                <eu:semanticUrl field="${value.fieldName}" contextualEntity="${value.contextualEntity}" schemaOrgMapping="${model.schemaOrgMapping}" />
              </c:set>
            </c:when>
            <c:otherwise>
              <c:set var="localSemanticAttributes">${"property=\""}${model.edmElements[value.fieldName].fullQualifiedURI}${"\""}</c:set>
            </c:otherwise>
          </c:choose>
        </c:if>

        <%-- determine if value is translatable or not --%>
        <c:set var="translatable" value='class="notranslate"' />
        <c:if test="${!empty data.showTranslationServices && data.showTranslationServices}">
          <c:set var="translatable" value='class="translate"' />
        </c:if>

        <c:set var="separator" value='' />
        <c:if test="${!valueStatus.last}">
          <c:set var="separator" value='; ' />
        </c:if>

        <%-- display the field's value
           value.searchOn = the value has an href value that when clicked on will issue a search in the portal based on other metadata criteria available to the item
           value.url = the value is actually a url that links to further information --%>
        <c:set var="seo_wrapper" value="" />

        <c:if test="${'dcterms:alternative' == data.fieldName}">
          <c:set var="seo_wrapper" value="h2" />
        </c:if>
        <c:if test="${'dc:creator' == data.fieldName}">
          <c:set var="seo_wrapper" value="h2" />
        </c:if>

        <c:if test="${seo_wrapper != ''}"><${seo_wrapper}></c:if>

        <c:choose>
          <c:when test="${value.searchOn}">
            <a href="${value.searchOn}" target="_top" ${translatable} <c:if test="${localSemanticAttributes != ''}">${" "}${localSemanticAttributes}</c:if> rel="nofollow">${value.value}</a>${separator}
          </c:when>
          <c:when test="${value.url}">
            <a href="${value.value}" target="_blank" ${translatable} <c:if test="${localSemanticAttributes != ''}">${" "}${localSemanticAttributes}</c:if> rel="nofollow">${value.value}</a>${separator}
          </c:when>
          <c:otherwise>
            <span ${translatable}<c:if test="${localSemanticAttributes != ''}">${" "}${localSemanticAttributes}</c:if><c:if test="${localSemanticUrl}">${" href=\""}${value.value}${"\""}</c:if>><c:out value="${value.value}" />
              <c:if test="${value.value == '3D PDF'}">
                <img src="/${branding}/images/icons/file-pdf.png" alt="To view this item you need Acrobat Reader 9 or higher">
              </c:if>
            </span><c:out value="${separator}" />
          </c:otherwise>
        </c:choose>
        <c:if test="${seo_wrapper != ''}"></${seo_wrapper}></c:if>

        <%-- link to external services if field has them --%>
        <%--
        <c:if test="${!empty data.ESSEnabled && data.ESSEnabled && ess}">
         | <a href="${model.essUrl}?field=${data.fieldName}&amp;value=${value.valueURL}" 
           title="<spring:message code="essHelpIconAltText_t" />"
           target="_blank" class="external-services toggle-menu-icon">${value.value}</a>
        </c:if>
        --%>

        <%-- handle inline or separate lines for multiple values --%>
        <c:if test="${value_has_next}">
          <c:choose>
            <c:when test="{!empty data.seperateLines && data.seperateLines}">
              <br /><br />
            </c:when>
            <c:otherwise>
              <c:choose>
                <c:when test="{!empty data.ESSEnabled && data.ESSEnabled && ess}">&#160;</c:when>
                <c:otherwise>;</c:otherwise>
              </c:choose>
            </c:otherwise>
          </c:choose>
        </c:if>
      </c:forEach>
    </${wrapper}>
  </c:if>
</c:forEach>