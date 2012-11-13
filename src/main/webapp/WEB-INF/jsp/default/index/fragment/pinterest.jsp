<%@ include file="/WEB-INF/jsp/default/_common/include-json.jspf" %>
     {
       "markup":"<%--
        --%><c:if test="${not empty model.pinterestItems}"><%--
          --%><div id='carousel-3' class='europeana-carousel'><%--
            --%><c:forEach var="item" items="${model.pinterestItems}"><%--
              --%><a href='${item.link}' class='hidden'><%--
                --%><img  src    = '${item.images[0].src}'<%--
                    --%>alt    = '${fn:escapeXml(item.plainDescription)}'<%--
                    --%>title  = '${fn:escapeXml(item.plainDescription)}'<%--
                --%>/><%--
              --%></a><%--
            --%></c:forEach><%--
          --%></div><%--
        --%></c:if><%--
      --%>",
      
       "data":{<%--
        --%>"carousel3Data" : [<%--
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
       --%>},<%--
       --%>"javascripts":[],<%--
       --%>"css":[]<%--
     --%>}
     