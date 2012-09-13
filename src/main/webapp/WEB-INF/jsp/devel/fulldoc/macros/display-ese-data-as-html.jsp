<!-- display-ese-data-as-html -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
 * display-ese-data-as-fields
 *
 * ese ( Europeana Semantic Elements ) meta-data can also be accessed directly
 * using a fullbean method, /core/src/main/java/eu/europeana/core/querymodel/beans/FullBean.java,
 * which typically returns a collection that needs to be iterated
 * over e.g., <#list model.document.dcDescription as data>
 *
 * unfortunately this access does not provide ess ( extended search service )
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
 *		e.g., the object can be searched for on wikipedia or imdb
 *
 *
 * @author unknown
 * @author Dan Entous <contact@pennlinepublishing.com>
 * @modified 2011-06-08 15:25 GMT+1
 
 model.fields 'div' false true
--%>
<%-- macro displayEseDataAsHtml listCollection wrapper ugc ess --%>
<!-- @displayEseDataAsHtml model.fields 'div' false true / -->
<%-- #list listCollection as data --%>
<c:set var="wrapper" value="div" />
<c:set var="ugc" value="false" />
<c:set var="ess" value="true" />
<c:forEach items="${model.fields}" var="data">
	<c:set var="item_id" value="" />
	<c:if test='${"dc:description" == data.fieldName}'><c:set var="item_id" value=' id="item-description" ' /></c:if>
	<c:if test='${"dc:subject"     == data.fieldName}'><c:set var="item_id" value=' id="item-subject" ' /></c:if>

	<c:set var="item_class" value="" />
	<c:if test='${"dc:rights"      == data.fieldName}'><c:set var="item_class" value=' item-moreless' /></c:if>

	<%-- If the content is UGC we skip the dc:source display --%>
	<c:if test="${!('dc:source' == data.fieldName && ugc)}">
		<<c:out value="${wrapper}"/> <c:out value="${item_id}" /> class="item-metadata${item_class}">
			<%-- field's label --%>
			<span class="bold notranslate"><spring:message code="${data.fieldLabel}" />:</span>

			<%-- iterate over possible values for the given label --%>
			<c:forEach items="${data.fieldValues}" var="value">
				<%-- determine if value is translatable or not --%>
				<c:set var="translatable" value=' class="notranslate"' />
				<c:if test="${!empty data.showTranslationServices && data.showTranslationServices}">
					<c:set var="translatable" value=' class="translate"' />
				</c:if>
				<%-- display the field's value
				     value.searchOn = the value has an href value that when clicked on will issue a search in the portal based on other metadata criteria available to the item
				     value.url = the value is actually a url that links to further information --%>
				<c:set var="seo_wrapper" value="" />
				<c:if test="${'dcterms:alternative' == data.fieldName}"><c:set var="seo_wrapper" value="h2" /></c:if>
				<c:if test="${'dc:creator' == data.fieldName}"><c:set var="seo_wrapper" value="h2" /></c:if>
				<c:if test="${seo_wrapper != ''}"><${seo_wrapper}></c:if>
				<c:choose>
					<c:when test="${value.searchOn}">
						<a href="${value.searchOn}" target="_top"${translatable} rel="nofollow">${value.value}</a>
					</c:when>
					<c:when test="${value.url}">
						<a href="${value.value}" target="_blank"${translatable} rel="nofollow">${value.value}</a>
					</c:when>
					<c:otherwise>
						<span${translatable}>${value.value}
							<c:if test="${value.value == '3D PDF'}">
								<img src="/${branding}/images/icons/file-pdf.png" alt="To view this item you need Acrobat Reader 9 or higher">
							</c:if>
						</span>
					</c:otherwise>
				</c:choose>
				<c:if test="${seo_wrapper != ''}"></${seo_wrapper}></c:if>
					<%-- link to external services if field has them --%>
				<c:if test="${!empty data.ESSEnabled && data.ESSEnabled && ess}">
				 | <a href="${model.essUrl}?field=${data.fieldName}&amp;value=${value.valueURL}" 
					 title="<spring:message code="essHelpIconAltText_t" />"
					 target="_blank"
					 class="external-services toggle-menu-icon">${value.value}</a>
				</c:if>
				<%-- handle inline or separate lines for multiple values --%>
				<c:if test="${value_has_next}">
					<c:choose>
						<c:when test="${!empty data.seperateLines && data.seperateLines}">
							<br /><br />
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${!empty data.ESSEnabled && data.ESSEnabled && ess}">&#160;</c:when>
								<c:otherwise>;</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</c:if>
			</c:forEach>
		</${wrapper}>
	</c:if>
</c:forEach>
<!-- /display-ese-data-as-html -->