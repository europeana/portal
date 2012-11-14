<%@ include file="/WEB-INF/jsp/default/_common/include.jsp"%>

<%@ include file="/WEB-INF/jsp/default/_common/html/doctype.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp" %>

<%@ include file="/WEB-INF/jsp/default/_common/html/open-html.jsp" %>
<div class="container">
  <%@ include file="/WEB-INF/jsp/default/_common/header.jsp" %>
  <div class="row">
    <c:url var="byDate" value="/admin/statistics.html">
      <c:param name="type">date</c:param>
    </c:url>
    <c:url var="byUser" value="/admin/statistics.html">
      <c:param name="type">user</c:param>
    </c:url>
    <c:url var="byType" value="/admin/statistics.html">
      <c:param name="type">type</c:param>
    </c:url>

    <p>
      <a href="${byDate}"><spring:message code="apistat_by_date_t" /></a>
       - <a href="${byUser}"><spring:message code="apistat_by_user_t" /></a>
       - <a href="${byType}"><spring:message code="apistat_by_type_t" /></a>
    </p>
    <c:choose>
      <c:when test="${empty model.type || model.type == 'date'}">
        <h3><spring:message code="apistat_by_date_t" /></h3>
        <table>
          <c:forEach items="${model.dateStatistics}" var="stat">
            <tr><td>${stat.key}</td><td>${stat.value}</td></tr>
          </c:forEach>
        </table>
      </c:when>

      <c:when test="${model.type == 'type'}">
        <h3><spring:message code="apistat_by_type_t" /></h3>
        <style>tr.total {font-weight: bold;}</style>
        <table>
          <thead>
            <tr>
              <th><spring:message code="apistat_record_type_t" /></th>
              <th><spring:message code="apistat_profile_t" /></th>
              <th><spring:message code="apistat_count_t" /></th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${model.typeStatistics}" var="stat">
              <c:forEach items="${stat.value}" var="item" varStatus="status">
                <tr <c:if test="${item.key == 'total'}">class="total"</c:if>>
                <td><c:if test="${status.first}">${stat.key}</c:if></td>
                <td>${item.key}</td>
                <td>${item.value}</td></tr>
              </c:forEach>
            </c:forEach>
          </tbody>
        </table>
      </c:when>

      <c:when test="${model.type == 'user'}">
        <h3><spring:message code="apistat_by_user_t" /></h3>
        <table>
          <c:forEach items="${model.userStatistics}" var="stat">
            <tr><td>${stat.key}</td><td>${stat.value}</td></tr>
          </c:forEach>
        </table>
      </c:when>
    </c:choose>

    <p>
      <c:url var="adminPage" value="/admin.html" />
      <a href="${adminPage}"><spring:message code="myeuropeana_AdminSection_t" /></a>
    </p>

  </div>
  <%@ include file="/WEB-INF/jsp/default/_common/footer.jsp" %>
</div>
<%@ include file="/WEB-INF/jsp/default/_common/html/javascripts.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/html/close-html.jsp" %>

