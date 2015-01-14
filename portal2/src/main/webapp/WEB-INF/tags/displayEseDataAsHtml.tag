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
<%@ taglib prefix="eufn" uri="http://europeana.eu/jsp/tlds/europeanatags"%>
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
 * @var data.translatable bool - whether or not translation of the field is appropriate
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
			<c:set var="cannedUrl" value="../../search.html?query=what%3a%22CANNED_VALUE%22&rows=${model.rows}"/>
		</c:when>

		<c:when test='${"dc:type"          == data.fieldName}'>
			<c:set var="cannedUrl" value="../../search.html?query=what%3a%22CANNED_VALUE%22&rows=${model.rows}"/>
		</c:when>

		<c:when test='${"dc:contributor"   == data.fieldName}'>
			<c:set var="cannedUrl" value="../../search.html?query=who%3a(CANNED_VALUE)&rows=${model.rows}"/>
		</c:when>

		<c:when test='${"dc:creator"       == data.fieldName}'>
			<c:set var="cannedUrl" value="../../search.html?query=who%3a(CANNED_VALUE)&rows=${model.rows}"/>
		</c:when>

		<c:when test='${"dc:coverage"      == data.fieldName}'>
			<c:set var="cannedUrl" value="../../search.html?query=where%3a%22CANNED_VALUE%22&rows=${model.rows}"/>
		</c:when>

		<c:when test='${"dc:provider"     == data.fieldName || "edm:provider"     == data.fieldName}'>
			<c:set var="cannedUrl" value="../../search.html?qf=PROVIDER%3a%22CANNED_VALUE%22&rows=${model.rows}"/>
		</c:when>

		<c:when test='${"dc:dataProvider" == data.fieldName || "edm:dataProvider" == data.fieldName}'>
			<c:set var="cannedUrl" value="../../search.html?qf=DATA_PROVIDER%3a%22CANNED_VALUE%22&rows=${model.rows}"/>
		</c:when>

		<c:otherwise><c:set var="cannedUrl" value="" /></c:otherwise>
	</c:choose>

	<%-- #1347 --%>

	<c:set var="item_class" value=""/>
	<c:if test='${data.fieldLabel=="dcterms_hasPart_t"}'><c:set var="item_class" value=" hasPart"/></c:if>


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

			<span class="bold notranslate br ${lightboxableNameClass}${hasPartClass}"><spring:message code="${data.fieldLabel}" />:</span>
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

			<%-- calculate expected number of spatial results --%>
			
			<c:set var="spatialCount" value="0"/>
			<c:set var="spatialTotal" value="0"/>
			<c:forEach items="${data.fieldValues}" var="_value" varStatus="valueStatus">
				<c:set var="value" value="${_value}" scope="request" />
				<c:if test="${model.showContext && !empty value.decorator}">
				 	<c:if test="${data.fieldName == 'dcterms:spatial'}">
				 		<c:set var="spatialTotal" value="${spatialTotal+1}"/>
					</c:if>
				</c:if>
			</c:forEach>

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
				<c:if test="${!empty data.translatable && data.translatable}">
					<c:if test="${fn:length(cannedUrl) == 0}">
						<c:set var="classAttr" value='translate' />
					</c:if>
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

				<c:choose>
					<c:when test="${'dcterms:alternative' == data.fieldName}">
						<c:set var="seo_wrapper" value="h2" />
					</c:when>
					<c:when test="${'dc:creator' == data.fieldName}">
						<c:set var="seo_wrapper" value="h2" />
					</c:when>
					<c:otherwise>
						<c:set var="seo_wrapper" value="" />
					</c:otherwise>
				</c:choose>

				<c:if test="${fn:length(seo_wrapper)}>0">
					<${seo_wrapper}>
				</c:if>

				<c:choose>
					<%--
						display contextual info
					--%>
					<c:when test="${model.showContext && !empty value.decorator}">
						<c:set var="inContext" value="1" scope="request" />
						<c:set var="page" value="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/context/${fn:toLowerCase(value.entityType)}.jsp" />
						<c:set var="contextualItem" value="${value.decorator}" scope="request" />
                        <c:set var="contextPageMarkup"><jsp:include page="${page}" flush="false" /></c:set>
                        <c:if test="${fn:length( fn:replace(contextPageMarkup, ' ','') ) > 0}">${fn:trim(contextPageMarkup)}${separator}</c:if>
						
 						<%-- lat / long --%>
 						
 					 	<c:if test="${data.fieldName == 'dcterms:spatial'}">
 					 		<c:set var="spatialCount" value="${spatialCount+1}"/>
 					 		<c:set var="openedLi"     value="${false}"/>
 					 		
 					 		<%-- if showing the last dc:spatial append the lat and long values --%>
 					 		
							<c:if test="${spatialCount == spatialTotal}">
								<c:set var="fieldsEnrichment"      value="${model.fieldsEnrichment}" />
								<c:if test="${!empty fieldsEnrichment && fn:length(fieldsEnrichment) > 0}">
									<ul class="geo-coordinates">
										<c:forEach items="${fieldsEnrichment}" var="fieldEnrichment">
											<c:if test="${fieldEnrichment.key == 'enrichment_category_where_t'}">
												<c:if test="${fn:length(fieldEnrichment.value) > 0}">
													<c:forEach items="${fieldEnrichment.value}" var="val" >
														<c:forEach items="${val.fieldValues}" var="fieldVal" varStatus="placeStatus">
															<c:if test="${fieldVal.fieldName == 'enrichment:place_lat_long'}">
																<c:if test="${fieldVal.value != 'null'}">
																	<c:choose>
																		<c:when test="${placeStatus.index==0}">
																			<c:if test="${openedLi}">
																				${"</li>"}	<%-- close anything unclosed --%>
																			</c:if>
																			<c:set var="openedLi"     value="${true}"/>
																			${"<li>"}		<%-- open --%>
																			<span class="translate latLong lat"><spring:message code="edm_place_latitude_t"/></span>:&nbsp;${fieldVal.value};
																		</c:when>
																		<c:when test="${placeStatus.index==1}">
																			<span class="translate latLong"><spring:message code="edm_place_longitude_t"/></span>:&nbsp;${fieldVal.value}
																			${"</li>"}		<%-- close  --%>
																			<c:set var="openedLi"     value="${false}"/>
																		</c:when>
																	</c:choose>
																</c:if>
															</c:if>
														</c:forEach>
													</c:forEach>
												</c:if>
											</c:if>
										</c:forEach>
									</ul>
								</c:if>
							</c:if>
							<c:if test="${openedLi}">
								${"</li>"}	<%-- close anything unclosed --%>
							</c:if>
						</c:if>
 						<%-- end lat / long --%>

						<%--
						<c:if test="${!empty value.decorator.allRelatedItems}">
							<c:forEach items="${value.decorator.allRelatedItems}" var="_contextualItem">
								<c:set var="contextualItem" value="${_contextualItem}" scope="request" />
								<jsp:include page="${page}" flush="true" />
							</c:forEach>
						</c:if>
						 --%>
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
									<c:out value="${(value.value)}" /><c:out value="${separator}" />
								</c:when>
								<c:otherwise>
									<c:set var="theVal" value="${eufn:cleanField(value.value)}" />
									<c:if test="${!empty model.useBackendItemTranslation
												&& model.useBackendItemTranslation == true
												&& !empty model.itemLanguage
												&& !empty data.translatable
												&& data.translatable == true
												&& !empty value.translatedValue}">
										<c:set var="theVal" value="${eufn:cleanField(value.translatedValue)}" />
									</c:if>

									<%-- wrap in canned link if available --%>
									<c:choose>
										<c:when test="${fn:length(cannedUrl) > 0}">
											<c:set var="CLEAN_URL" value="${fn:replace(cannedUrl, 'CANNED_VALUE', fn:replace(theVal, '&', '%26'))}"/>
											
											<c:set var="doSeparator" value="false"/>
											<c:if test="${ valueStatus.index+1 < fn:length(data.fieldValues) }">
												<c:set var="nextVal" value="${data.fieldValues[valueStatus.index+1]}" />
												<c:choose>
													<c:when test="${empty nextVal.decorator}">
														<c:set var="doSeparator" value="true"/>
													</c:when>
													<c:otherwise>
														<c:if test="${!empty nextVal.decorator.labels }">
															<c:set var="doSeparator" value="true"/>
														</c:if>
													</c:otherwise>
												</c:choose>
											</c:if>
											<c:set var="CLEAN_URL" value="${eufn:cleanSquareBrackets(CLEAN_URL)}"/>
											<a class="europeana canned" href="${CLEAN_URL}"><c:out value="${theVal}" escapeXml="false" /></a><c:if test="${doSeparator}">;</c:if>
										</c:when>
										<c:otherwise>
										
											<c:choose>
												<c:when test="${data.fieldLabel == 'languageDropDownList_t'}">
													<c:set var="codeFound" value="false" />
													<c:forEach items="${model.allPortalLanguages}" var="lang">
														<c:if test="${lang.languageCode == theVal}">
															${lang.languageName}
															<c:set var="codeFound" value="true" />
														</c:if>
													</c:forEach>
													<c:if test="${!codeFound}">
														${theVal}
													</c:if>
												</c:when>
												
												<c:when test="${data.fieldLabel == 'Description_t'}">
												    <c:out value="${theVal}" escapeXml="false" /><c:out value="${separator}" />
													<c:if test="${valueStatus.index+1 < fn:length(data.fieldValues)}">
														<br/>
														<br/>
													</c:if>				
												</c:when>

												<c:otherwise>
													<c:out value="${fn:trim(theVal)}" escapeXml="false" /><c:out value="${separator}" />
												</c:otherwise>
											</c:choose>
										
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							<c:if test="${value.value == '3D PDF'}">
								<img src="/${branding}/images/icons/file-pdf.png" alt="To view this item you need Acrobat Reader 9 or higher">
							</c:if>
						</span>
					</c:otherwise>
				</c:choose>
				
				<c:if test="${fn:length(seo_wrapper)}>0">
					</${seo_wrapper}>
				</c:if>
				

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
