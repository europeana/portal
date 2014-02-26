<%@ include file="/WEB-INF/jsp/default/_common/include-json.jspf" %>
     {
       "markup":"<%--
        --%><%--
          --%><%--
            --%><%--
              --%><%--
                --%><%--
                    --%><%--
                    --%><%--
                --%><%--
              --%><%--
            --%><%--
          --%><%--
        --%><%--
      --%>",

      
       "data":[<%--
        --%><c:forEach var="item" items="${model.pinterestItems}" varStatus="status"><%--
          --%>{<%--
            --%>"thumb": "${item.images[0].src}",<%--
            --%>"title": "${fn:escapeXml(item.plainDescription)}",<%--
            --%>"link": "${item.link}",<%--
            --%>"linkTarget": "_new"<%--
          --%>}<%--
          --%><c:if test="${not status.last}">,</c:if><%--
        --%></c:forEach><%--
        --%>]<%--
     --%>}
     