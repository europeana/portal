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


	<%-- #1347 set canned types --%>

	<c:choose>
		<c:when test='${"dc:subject"       == data.fieldName}'>
			<c:set var="cannedUrl" value="/${model.portalName}/search.html?query=what%3a%22CANNED_VALUE%22&rows=${model.rows}"/>
		</c:when>

		<c:when test='${"dc:type"          == data.fieldName}'>
			<c:set var="cannedUrl" value="/${model.portalName}/search.html?query=what%3a%22CANNED_VALUE%22&rows=${model.rows}"/>
		</c:when>

		<c:when test='${"dc:contributor"   == data.fieldName}'>
			<c:set var="cannedUrl" value="/${model.portalName}/search.html?query=who%3a(CANNED_VALUE)&rows=${model.rows}"/>
		</c:when>

		<c:when test='${"dc:creator"       == data.fieldName}'>
			<c:set var="cannedUrl" value="/${model.portalName}/search.html?query=who%3a(CANNED_VALUE)&rows=${model.rows}"/>
		</c:when>

		<c:when test='${"dc:coverage"      == data.fieldName}'>
			<c:set var="cannedUrl" value="/${model.portalName}/search.html?query=where%3a%22CANNED_VALUE%22&rows=${model.rows}"/>
		</c:when>

		<c:when test='${"dc:provider"     == data.fieldName || "edm:provider"     == data.fieldName}'>
			<c:set var="cannedUrl" value="/${model.portalName}/search.html?qf=PROVIDER%3a%22CANNED_VALUE%22&rows=${model.rows}"/>
		</c:when>

		<c:when test='${"dc:dataProvider" == data.fieldName || "edm:dataProvider" == data.fieldName}'>
			<c:set var="cannedUrl" value="/${model.portalName}/search.html?qf=DATA_PROVIDER%3a%22CANNED_VALUE%22&rows=${model.rows}"/>
		</c:when>

		<c:otherwise><c:set var="cannedUrl" value="" /></c:otherwise>
	</c:choose>

	<%-- #1347 --%>

	<c:set var="item_class" value=""/>

	<%-- If the content is UGC we skip the dc:source display --%>
	<c:if test="${!('dc:source' == data.fieldName && ugc)}">
		<%-- Semantic attributes --%>
		<c:set var="semanticAttributes" value="" />
		<c:set var="semanticUrl" value="" />
		<c:if test="${!data.optedOut}">
			<c:set var="semanticAttributes" value="${data.semanticAttributes}" />
			<c:set var="semanticUrl" value="${data.semanticUrl}" />
		</c:if>

		<<c:out value="${wrapper}" />${' '}${item_id} class="item-metadata${item_class}">
			<%-- field's label --%>
			<c:set var="lightboxables" scope="request">Creator_t,europeana_dataProvider_t,Provider_t</c:set>
			<c:set var="lightboxableNameClass" value="" />
			<c:set var="lightboxableValueClass" value="" />

			<c:forEach items="${lightboxables}" var="lightboxable">
				<c:if test="${lightboxable == data.fieldLabel}">
					<c:set var="lightboxableNameClass" value="lbN" />
					<c:set var="lightboxableValueClass" value="lbV" />
				</c:if>
			</c:forEach>


			<span class="bold notranslate ${lightboxableNameClass}"><spring:message code="${data.fieldLabel}" />:</span>

			<%-- iterate over possible values for the given label
				data (FieldValue):
					- value.value
					- value.valueClean
					- value.valueXML
					- value.valueJSON
					- value.valueURL
					- value.fieldName
					- value.optedOut (boolean)
					- value.semanticAttributes (String)
					- value.semanticUrl (boolean)
					- value.contextualEntity (String)
					- value.searchOn (String)
			--%>
			
			<c:forEach items="${data.fieldValues}" var="_value" varStatus="valueStatus">
				<c:set var="value" value="${_value}" scope="request" />
				<c:set var="localSemanticAttributes" value="${semanticAttributes}" />
				<c:set var="localSemanticUrl" value="${semanticUrl}" />
				<c:if test="${value.fieldName != data.fieldName && !value.optedOut}">
					<c:set var="semanticAttributes" value="${value.semanticAttributes}" />
					<c:set var="semanticUrl" value="${value.semanticUrl}" />
				</c:if>

				<%-- determine if value is translatable or not --%>
				<c:set var="classAttr" value='notranslate' />
				<c:if test="${!empty data.showTranslationServices && data.showTranslationServices}">
					<c:set var="classAttr" value='translate' />
				</c:if>
				<c:set var="classAttr" value='${classAttr}${" "}${lightboxableValueClass}' />

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
					<c:when test="${model.showContext && !empty value.decorator}">
						<c:set var="inContext" value="1" scope="request" />
						<c:set var="page" value="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/context/${fn:toLowerCase(value.entityType)}.jsp" />
						<c:set var="contextualItem" value="${value.decorator}" scope="request" />
						<jsp:include page="${page}" flush="true" />
						<c:if test="${!empty value.decorator.allRelatedItems}">
							<c:forEach items="${value.decorator.allRelatedItems}" var="_contextualItem">
								<c:set var="contextualItem" value="${_contextualItem}" scope="request" />
								<jsp:include page="${page}" flush="true" />
							</c:forEach>
						</c:if>
					</c:when>
					<c:when test="${value.searchOn}">
						<a href="${value.searchOn}" target="_top" class="${classAttr}"
							<c:if test="${localSemanticAttributes != ''}">${" "}${localSemanticAttributes}</c:if>
							rel="nofollow">${value.value}</a>${separator}
					</c:when>
					<c:when test="${value.url}">
						<a href="${value.value}" target="_blank" class="${classAttr}"
							<c:if test="${localSemanticAttributes != ''}">${" "}${localSemanticAttributes}</c:if>
							rel="nofollow">${value.value}</a>${separator}
					</c:when>
					<c:otherwise>
						<span class="${classAttr}"
							<c:if test="${localSemanticAttributes != ''}">${" "}${localSemanticAttributes}</c:if>
							<c:if test="${localSemanticUrl}">${" href=\""}${value.value}${"\""}</c:if>><c:choose>

								<%--
								Stop the escaping of non-ascii characters as in:
									Creator: Tyšler, A.,
								on page:
									http://localhost:8081/portal/record/2022005/A8FDF4B2F3ECEBB6B469AE7E1D7AB98CBFF66675.html
								--%>

								<c:when test="${localSemanticUrl}">
									<c:out value="${(value.value)}" />
								</c:when>
								<c:otherwise>
									<c:set var="theVal" value="${value.value}" />

									<%-- respect <br>, <p> and <i> --%>
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;BR&gt;',	'<br/>')}" />
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;br&gt;',	'<br/>')}" />
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;I&gt;',	'<i>')}" />
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;i&gt;',	'<i>')}" />
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;/I&gt;',	'</i>')}" />
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;/i&gt;',	'</i>')}" />
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;P&gt;',	'<p>')}" />
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;p&gt;',	'<p>')}" />
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;/P&gt;',	'</p>')}" />
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;/p&gt;',	'</p>')}" />

									<%-- lose <b> and <li> --%>
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;LI&gt;',	'')}" />
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;li&gt;',	'')}" />
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;/LI&gt;',	'')}" />
									<c:set var="theVal" value="${fn:replace(theVal, '&lt;/li&gt;',	'')}" />

									<%-- escape >> used as arrows --%>
									<c:set var="theVal" value="${fn:replace(theVal, '>>',	'&rarr;')}" />

									<%-- remove double escaped quotes --%>
									<c:set var="theVal" value="${fn:replace(theVal, '&amp;quot;',	'\"')}" />

									<%-- wrap in canned link if available --%>
									<c:choose>
										<c:when test="${fn:length(cannedUrl)>0}">
									      <a class="europeana" href="${fn:replace( cannedUrl, 'CANNED_VALUE', fn:replace(theVal, '&', '%26')  ) }"><c:out value="${theVal}" escapeXml="false" /></a> 
										</c:when>
										<c:otherwise>
											<c:out value="${theVal}" escapeXml="false" />
										</c:otherwise>
									</c:choose>

								</c:otherwise>
							</c:choose> <c:if test="${value.value == '3D PDF'}">
								<img src="/${branding}/images/icons/file-pdf.png" alt="To view this item you need Acrobat Reader 9 or higher">
							</c:if>
						</span>
						<c:out value="${separator}" />
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
							<br />
							<br />
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