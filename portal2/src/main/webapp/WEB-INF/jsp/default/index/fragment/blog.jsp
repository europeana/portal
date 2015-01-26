<%@ include file="/WEB-INF/jsp/default/_common/include-json.jspf" %>
{
   "markup":"<%--
      --%><c:choose><%--
      --%><c:when test='${not empty model.feedEntries}'><%--
         --%><c:forEach items="${model.feedEntries}" var="entry" varStatus="status"><%--
            --%><c:if test="${status.index < 2}"><%--

               --%><div class='six columns'><%--

                  --%><div class='fi-block-spacer'><%--
            
                     --%><h4 class='show-on-phones'><%--
                        --%><a href='${entry.link}'<%--
                           --%>title='${entry.title}'<%--
                           --%>target='_self'<%--
                           --%>class='europeana'><%--
                           --%>${entry.title}<%--
                        --%></a><%--
                     --%></h4><%--
               
                     --%><a href='${entry.link}'<%--
                        --%>title='${entry.title}'<%--
                        --%>target='_self'<%--
                        --%>class='image'><%--
                        --%><img class='responsive_half' src='${homeUrl}${entry.images[0].responsiveFileNames["_1"]}' alt='${entry.title}' title='${entry.title}'/><%--
                     --%></a><%--
      
                     --%><h4 class='hide-on-phones'><%--
                        --%><a href='${entry.link}'<%--
                           --%>title='${entry.title}'<%--
                           --%>target='_self'<%--
                           --%>class='europeana'><%--
                           --%>${entry.title}<%--
                        --%></a><%--
                     --%></h4><%--
                     --%><p class='featured-text'><%--
                     
                        --%>${fn:replace(entry.description, '"', '\\"')}<%--
                        
                     --%></p><%--
                  --%></div><%--
               --%></div><%--
            --%></c:if><%--
         --%></c:forEach><%--
      --%></c:when><%--
      --%><c:otherwise><%--
          fallback content in case the blog feed is not working
          or if the portal server blog feed cache is cleared after a server restart
        --%><h3><a href='<spring:message code="notranslate_blog-item-1_a_url" />' target='<spring:message code="notranslate_blog-item-1_a_target" />'><spring:message code="notranslate_blog-item-1_h3" /></a></h3><%-- 
        --%><p><spring:message code="notranslate_blog-item-1_p" /></p><%--
        --%><h3><a href='<@spring.message "notranslate_blog-item-2_a_url" />' target='<spring:message code="notranslate_blog-item-2_a_target" />'><spring:message code="notranslate_blog-item-2_h3" /></a></h3><%-- 
        --%><p><spring:message code="notranslate_blog-item-2_p" /></p><%--
        --%><h3><a href='<spring:message code="notranslate_blog-item-3_a_url"/>' target='<spring:message code="notranslate_blog-item-3_a_target" />'><spring:message code="notranslate_blog-item-3_h3" /></a></h3><%-- 
        --%><p><spring:message code="notranslate_blog-item-3_p" /></p><%--
   --%></c:otherwise><%--
   --%></c:choose><%--
   --%>"
}
