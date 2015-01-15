<%@ include file="/WEB-INF/jsp/default/_common/include-json.jspf" %>

     {
       "markup":"<%--
         --%><c:if test="${!empty model.featuredItem}"><%--
           --%><div class='fi-block-spacer'><%--
             --%><h4 class='show-on-phones'><%--
               --%><a  href  = '<spring:message code="${model.featuredItem.anchorUrl}" />'<%--
                 --%>title  = '<spring:message code="${model.featuredItem.anchorTitle}" />'<%--
                 --%>target  = '<spring:message code="${model.featuredItem.anchorTarget}" />'<%--
                 --%>class  = 'europeana withRowParam'><%--
                 --%><spring:message code="${model.featuredItem.heading}" /><%--  
               --%></a><%--
             --%></h4><%--
             --%><a  href=  '${homeUrl}<spring:message code="${model.featuredItem.anchorUrl}" />'<%--
             --%>title=  '<spring:message code="${model.featuredItem.anchorTitle}" />'<%--
             --%>target=  '<spring:message code="${model.featuredItem.anchorTarget}" />'<%--
             --%>class=  'image  withRowParam'><%--
             --%><c:set var="altText"><spring:message code="${model.featuredItem.imgAlt}" /></c:set><%-- 
             --%><img class='responsive_half' src='${homeUrl}${model.featuredItem.responsiveImages["_1"]}' alt='${fn:escapeXml(altText)}'/><%--
             --%></a><%--
             --%><h4 class='hide-on-phones'><%--
               --%><a href='${homeUrl}<spring:message code="${model.featuredItem.anchorUrl}" />'<%--
               --%>title  = '<spring:message code="${model.featuredItem.anchorTitle}" />'<%--
               --%>target  = '<spring:message code="${model.featuredItem.anchorTarget}" />'<%--
               --%>class  = 'europeana withRowParam'><%--
               --%><spring:message code="${model.featuredItem.heading}" /><%--  
             --%></a><%--
           --%></h4><%--
           --%><spring:message code="${model.featuredItem.p}" /><%--
         --%></div><%--
       --%></c:if><%--
     --%>",<%--
     --%>"markup2" : "<%--
       --%><div class='six columns' id='section-featured-partner'><%--
       --%><c:if test="${!empty model.featuredPartner}"><%--
            collapsible header
         --%><div class='row'><%--
           --%><div class='twelve columns' id='featured-partner-header-wrapper'><%--
             --%><div id='partner-section-heading' class='fi-block-spacer'><%--
               --%><h3 id='section-header-featured-partner' class='collapse-header-text'><spring:message code="featured-partner-title_t" /></h3><%--
             --%></div><%--
           --%></div><%--
         --%></div><%--
       --%><div class='fi-block-spacer'><%--
         --%><h4 class='show-on-phones'><%--
           --%><a  href  = '<spring:message code="${model.featuredPartner.anchorUrl}" />'<%--
             --%>title  = '<spring:message  code="${model.featuredPartner.anchorTitle}" />'<%--
             --%>target  = '<spring:message  code="${model.featuredPartner.anchorTarget}" />'<%--
             --%>class  = 'europeana'><%--
             --%><spring:message code="${model.featuredPartner.heading}" /><%--  
           --%></a><%--
         --%></h4><%--
         --%><a  href= '${homeUrl}<spring:message code="${model.featuredPartner.anchorUrl}" />'<%--
           --%>title=  '<spring:message code="${model.featuredPartner.anchorTitle}" />'<%--
           --%>target= '<spring:message code="${model.featuredPartner.anchorTarget}" />'<%--
           --%>class=  'image withRowParam'><%--
			--%><c:set var="altText"><spring:message code="${model.featuredPartner.imgAlt}" /></c:set><%--
           --%><img class='responsive_half' src='${homeUrl}${model.featuredPartner.responsiveImages["_1"]}'<%--
           --%>alt='${fn:escapeXml(altText)}'<%--
           --%>/><%--
         --%></a><%--
         --%><h4 class='hide-on-phones'><%--
           --%><a  href  = '${homeUrl}<spring:message  code="${model.featuredPartner.anchorUrl}" />'<%--
             --%>title   = '<spring:message  code="${model.featuredPartner.anchorTitle}" />'<%--
             --%>target  = '<spring:message  code="${model.featuredPartner.anchorTarget}" />'<%--
             --%>class  = 'europeana  withRowParam'><%--
             --%><spring:message code="${model.featuredPartner.heading}" /><%--
           --%></a><%--
         --%></h4><%--
       --%><spring:message code="${model.featuredPartner.p}" /><%--
         --%><ul class='featured-partner-links featured-text'><%--
           --%><li><%--
             --%><a href='<spring:message code="${model.featuredPartner.visitLink}"  />'<%--
               --%>target='<spring:message code="notranslate_featured-partner-visit_target_t" />'<%--
               --%>rel='nofollow'<%--
               --%>class='icon-external-right europeana'><%--
               --%><spring:message code="featured-partner-visit_text_t"/>&nbsp;<%--
               --%><spring:message code="${model.featuredPartner.anchorTitle}"/><%--
               --%><a class='new-content-link europeana' target='<spring:message code="notranslate_featured-partner-view_target_t" />' href='<spring:message code="notranslate_featured-partner-view_link_t" />'><spring:message code="featured-partner-view_text_t"/></a><%--
               --%></a><%--
             --%></li><%--
           --%></ul><%--
         --%></div><%--
       --%></c:if><%--
     --%></div><%--
   --%>"<%--
 --%>}
 