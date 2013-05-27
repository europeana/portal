<%@ include file="/WEB-INF/jsp/default/_common/include.jsp"%>

<%@ include file="/WEB-INF/jsp/default/_common/html/doctype.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp" %>

<%@ include file="/WEB-INF/jsp/default/_common/html/open-html.jsp" %>
<style type="text/css">
    ul#stat-selector {
      margin: 0;
      padding: 0;
    }
    ul#stat-selector li {
      display: inline;
      margin-right: 30px;
    }
</style>
<div class="container">
  <%@ include file="/WEB-INF/jsp/default/_common/header.jsp" %>
  <div class="row">
    <c:url var="byDate" value="/admin/statistics.html">
      <c:param name="type">dates</c:param>
    </c:url>
    <c:url var="byMonth" value="/admin/statistics.html">
      <c:param name="type">months</c:param>
    </c:url>
    <c:url var="byUser" value="/admin/statistics.html">
      <c:param name="type">apiKeys</c:param>
    </c:url>
    <c:url var="byRecordType" value="/admin/statistics.html">
      <c:param name="type">recordTypes</c:param>
    </c:url>

    <ul id="stat-selector">
      <li><a href="${byDate}"><spring:message code="apistat_by_date_t" /></a></li>
      <li><a href="${byMonth}"><spring:message code="apistat_by_month_t" /></a></li>
      <li><a href="${byUser}"><spring:message code="apistat_by_user_t" /></a></li>
      <li><a href="${byRecordType}"><spring:message code="apistat_by_type_t" /></a></li>
    </ul>

    <c:choose>
      <c:when test="${empty model.type || model.type == 'dates'}">
        <h3><spring:message code="apistat_by_date_t" /></h3>
        <table>
          <c:forEach items="${model.dateStatistics}" var="stat">
            <tr><td>${stat.key}</td><td>${stat.value}</td></tr>
          </c:forEach>
        </table>
      </c:when>

      <c:when test="${model.type == 'recordTypes'}">
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
            <c:forEach items="${model.typeStatistics}" var="type" varStatus="typeStatus">
              <c:forEach items="${type.value}" var="item" varStatus="status">
                <tr <c:if test="${item.profile == 'total'}">class="total"</c:if>>
                  <td>
                    <c:if test="${status.first}">
                      <c:choose>
                        <c:when test="${!typeStatus.last}"><a href="?type=recordType&recordType=${item.recordType}">${item.recordType}</a></c:when>
                        <c:otherwise>${item.recordType}</c:otherwise>
                      </c:choose>
                    </c:if>
                  </td>
                  <td>${item.profile}</td>
                  <td align="right">${item.count}</td>
                </tr>
              </c:forEach>
            </c:forEach>
          </tbody>
        </table>
      </c:when>

      <c:when test="${model.type == 'apiKeys' || model.type == 'usersByMonth' || model.type == 'usersByRecordType'}">
        <h3><spring:message code="apistat_by_user_t" /></h3>
        <c:if test="${model.type == 'usersByMonth'}">
          <h4>${model.monthLabel}</h4>
        </c:if>
        <c:if test="${model.type == 'usersByRecordType'}">
          <h4>${model.recordType}</h4>
        </c:if>

        <c:set var="defaultParams" value="type=${model.type}" />
        <c:if test="${model.type == 'usersByMonth'}">
          <c:set var="defaultParams" value="${defaultParams}&month=${model.month}" />
        </c:if>
        <c:if test="${model.type == 'usersByRecordType'}">
          <c:set var="defaultParams" value="${defaultParams}&recordType=${model.recordType}" />
        </c:if>

        <c:set var="reverseName" value="false" />
        <c:set var="reverseApi" value="false" />
        <c:set var="reverseCount" value="false" />
        <c:if test="${!model.descending}">
          <c:choose>
            <c:when test="${model.order == 'name'}">
              <c:set var="reverseName" value="true" />
            </c:when>
            <c:when test="${model.order == 'apikey'}">
              <c:set var="reverseApi" value="true" />
            </c:when>
            <c:when test="${model.order == 'count'}">
              <c:set var="reverseCount" value="true" />
            </c:when>
          </c:choose>
        </c:if>

        <table>
          <thead>
            <tr>
              <th><a href="?${defaultParams}&order=name<c:if test="${reverseName}">&dir=desc</c:if>"><spring:message code="apistat_by_user_name_t" /></a></th>
              <th><a href="?${defaultParams}&order=apikey<c:if test="${reverseApi}">&dir=desc</c:if>"><spring:message code="apistat_by_user_apikey_t" /></a></th>
              <th><a href="?${defaultParams}&order=count<c:if test="${reverseCount}">&dir=desc</c:if>"><spring:message code="apistat_by_user_count_t" /></a></th>
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${model.userStatistics}" var="stats">
            <c:forEach items="${stats.value}" var="stat">
              <tr>
                <td>${stat.name}</td>
                <td><a href="?type=apiKey&apiKey=${stat.apiKey}">${stat.apiKey}</a></td>
                <td align="right">${stat.count}</td>
              </tr>
            </c:forEach>
          </c:forEach>
          </tbody>
        </table>
      </c:when>

      <c:when test="${model.type == 'months'}">
        <h3><spring:message code="apistat_by_month_t" /></h3>
        <table>
          <thead>
            <tr>
              <th>Month</th>
              <th>Count</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${model.monthStatistics}" var="stat">
              <tr><td><a href="?type=month&month=${stat.month}">${stat.label}</a></td><td>${stat.count}</td></tr>
            </c:forEach>
          </tbody>
        </table>
      </c:when>
      
      <c:when test="${model.type == 'apiKey'}">
        <h3>Usage info about user <em>${model.userName}</em> (API key: ${model.apiKey})</h3>
        <style>tr.total {font-weight: bold;}</style>

        <h4>By Type</h4>
        <table>
          <thead>
            <tr>
              <th><spring:message code="apistat_record_type_t" /></th>
              <th><spring:message code="apistat_profile_t" /></th>
              <th><spring:message code="apistat_count_t" /></th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${model.typeStatistics}" var="type" varStatus="typeStatus">
              <c:forEach items="${type.value}" var="item" varStatus="status">
                <tr <c:if test="${item.profile == 'total'}">class="total"</c:if>>
                  <td>
                    <c:if test="${status.first}">
                      <c:choose>
                        <c:when test="${!typeStatus.last}"><a href="?type=recordType&recordType=${item.recordType}">${item.recordType}</a></c:when>
                        <c:otherwise>${item.recordType}</c:otherwise>
                      </c:choose>
                    </c:if>
                  </td>
                  <td>${item.profile}</td>
                  <td align="right">${item.count}</td>
                </tr>
              </c:forEach>
            </c:forEach>
          </tbody>
        </table>

        <h4>By month</h4>
        <table>
          <thead>
            <tr>
              <th>Month</th>
              <th>Count</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${model.monthStatistics}" var="stat">
              <tr><td><a href="?type=month&month=${stat.month}">${stat.label}</a></td><td>${stat.count}</td></tr>
            </c:forEach>
          </tbody>
        </table>

      </c:when>

      <c:when test="${model.type == 'recordType'}">
        <h3>Usage info about API call type: <em>${model.recordType}</em></h3>
        <style>tr.total {font-weight: bold;}</style>

        <h4>By API keys</h4>
        <c:set var="defaultParams" value="type=${model.type}&recordType=${model.recordType}" />

        <c:set var="reverseName" value="false" />
        <c:set var="reverseApi" value="false" />
        <c:set var="reverseCount" value="false" />
        <c:if test="${!model.descending}">
          <c:choose>
            <c:when test="${model.order == 'name'}">
              <c:set var="reverseName" value="true" />
            </c:when>
            <c:when test="${model.order == 'apikey'}">
              <c:set var="reverseApi" value="true" />
            </c:when>
            <c:when test="${model.order == 'count'}">
              <c:set var="reverseCount" value="true" />
            </c:when>
          </c:choose>
        </c:if>

        <table>
          <thead>
            <tr>
              <th><a href="?${defaultParams}&order=name<c:if test="${reverseName}">&dir=desc</c:if>"><spring:message code="apistat_by_user_name_t" /></a></th>
              <th><a href="?${defaultParams}&order=apikey<c:if test="${reverseApi}">&dir=desc</c:if>"><spring:message code="apistat_by_user_apikey_t" /></a></th>
              <th><a href="?${defaultParams}&order=count<c:if test="${reverseCount}">&dir=desc</c:if>"><spring:message code="apistat_by_user_count_t" /></a></th>
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${model.userStatistics}" var="stats">
            <c:forEach items="${stats.value}" var="stat">
              <tr>
                <td>${stat.name}</td>
                <td><a href="?type=apiKey&apiKey=${stat.apiKey}">${stat.apiKey}</a></td>
                <td align="right">${stat.count}</td>
              </tr>
            </c:forEach>
          </c:forEach>
          </tbody>
        </table>

        <h4>By month</h4>
        <table>
          <thead>
            <tr>
              <th>Month</th>
              <th>Count</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${model.monthStatistics}" var="stat">
              <tr><td><a href="?type=month&month=${stat.month}">${stat.label}</a></td><td>${stat.count}</td></tr>
            </c:forEach>
          </tbody>
        </table>
      </c:when>

      <c:when test="${model.type == 'month'}">
        <h3>Usage info about date range: <em>${model.monthLabel}</em></h3>

        <c:set var="defaultParams" value="type=${model.type}&month=${model.month}" />

        <c:set var="reverseName" value="false" />
        <c:set var="reverseApi" value="false" />
        <c:set var="reverseCount" value="false" />
        <c:if test="${!model.descending}">
          <c:choose>
            <c:when test="${model.order == 'name'}">
              <c:set var="reverseName" value="true" />
            </c:when>
            <c:when test="${model.order == 'apikey'}">
              <c:set var="reverseApi" value="true" />
            </c:when>
            <c:when test="${model.order == 'count'}">
              <c:set var="reverseCount" value="true" />
            </c:when>
          </c:choose>
        </c:if>

        <h4><spring:message code="apistat_by_user_t" /></h4>
        <table>
          <thead>
            <tr>
              <th><a href="?${defaultParams}&order=name<c:if test="${reverseName}">&dir=desc</c:if>"><spring:message code="apistat_by_user_name_t" /></a></th>
              <th><a href="?${defaultParams}&order=apikey<c:if test="${reverseApi}">&dir=desc</c:if>"><spring:message code="apistat_by_user_apikey_t" /></a></th>
              <th><a href="?${defaultParams}&order=count<c:if test="${reverseCount}">&dir=desc</c:if>"><spring:message code="apistat_by_user_count_t" /></a></th>
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${model.userStatistics}" var="stats">
            <c:forEach items="${stats.value}" var="stat">
              <tr>
                <td>${stat.name}</td>
                <td><a href="?type=apiKey&apiKey=${stat.apiKey}">${stat.apiKey}</a></td>
                <td align="right">${stat.count}</td>
              </tr>
            </c:forEach>
          </c:forEach>
          </tbody>
        </table>

        <h4><spring:message code="apistat_by_type_t" /></h4>
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
            <c:forEach items="${model.typeStatistics}" var="type" varStatus="typeStatus">
              <c:forEach items="${type.value}" var="item" varStatus="status">
                <tr <c:if test="${item.profile == 'total'}">class="total"</c:if>>
                  <td>
                    <c:if test="${status.first}">
                      <c:choose>
                        <c:when test="${!typeStatus.last}"><a href="?type=recordType&recordType=${item.recordType}">${item.recordType}</a></c:when>
                        <c:otherwise>${item.recordType}</c:otherwise>
                      </c:choose>
                    </c:if>
                  </td>
                  <td>${item.profile}</td>
                  <td align="right">${item.count}</td>
                </tr>
              </c:forEach>
            </c:forEach>
          </tbody>
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

