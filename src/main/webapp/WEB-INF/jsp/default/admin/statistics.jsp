<%@ include file="/WEB-INF/jsp/default/_common/include.jsp"%>

<%@ include file="/WEB-INF/jsp/default/_common/html/doctype.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp" %>

<%@ include file="/WEB-INF/jsp/default/_common/html/open-html.jsp" %>
<style type="text/css">
tr.total {
  font-weight: bold;
}
ul#stat-selector {
  margin: 0;
  padding: 0;
}
ul#stat-selector li {
  display: inline;
  margin-right: 30px;
}
table {
  *border-collapse: collapse; /* IE7 and lower */
  border-spacing: 0;
  width: 100%;
}

.bordered a {
  display: block;
  cursor: pointer;
}

.bordered {
  border: solid #ccc 1px;
  -moz-border-radius: 6px;
  -webkit-border-radius: 6px;
  border-radius: 6px;
  -webkit-box-shadow: 3px 3px 13px #ccc;
  -moz-box-shadow: 3px 3px 13px #ccc;
  box-shadow: 3px 3px 13px #ccc;
}

.bordered tr:hover {
  background: #e3fffb; /* Hover color could be related to site specific (lightest colour) */
  -o-transition: all 0.1s ease-in-out;
  -webkit-transition: all 0.1s ease-in-out;
  -moz-transition: all 0.1s ease-in-out;
  -ms-transition: all 0.1s ease-in-out;
  transition: all 0.1s ease-in-out;
}

.bordered td, .bordered th {
  border-left: 1px solid #ccc;
  border-top: 1px solid #ccc;
  padding: 0.6em;
  text-align: left;
  white-space: normal;
}

.bordered th {
  background-color: #dce9f9;
  background-image: -webkit-gradient(linear, left top, left bottom, from(#ebf3fc), to(#dce9f9));
  background-image: -webkit-linear-gradient(top, #ebf3fc, #dce9f9);
  background-image:    -moz-linear-gradient(top, #ebf3fc, #dce9f9);
  background-image:     -ms-linear-gradient(top, #ebf3fc, #dce9f9);
  background-image:      -o-linear-gradient(top, #ebf3fc, #dce9f9);
  background-image:         linear-gradient(top, #ebf3fc, #dce9f9);
  -webkit-box-shadow: 0 1px 0 rgba(255,255,255,.8) inset; 
  -moz-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;  
  box-shadow: 0 1px 0 rgba(255,255,255,.8) inset;        
  border-top: none;
  text-shadow: 0 1px 0 rgba(255,255,255,.5); 
  font-weight: 600;
  padding-top: 0.8em;
}

.bordered td:first-child, .bordered th:first-child {
  border-left: none;
}

.bordered th:first-child {
  -moz-border-radius: 6px 0 0 0;
  -webkit-border-radius: 6px 0 0 0;
  border-radius: 6px 0 0 0;
}

.bordered th:last-child {
  -moz-border-radius: 0 6px 0 0;
  -webkit-border-radius: 0 6px 0 0;
  border-radius: 0 6px 0 0;
}

.bordered th:only-child{
  -moz-border-radius: 6px 6px 0 0;
  -webkit-border-radius: 6px 6px 0 0;
  border-radius: 6px 6px 0 0;
}

.bordered tr:last-child td:first-child {
  -moz-border-radius: 0 0 0 6px;
  -webkit-border-radius: 0 0 0 6px;
  border-radius: 0 0 0 6px;
}

.bordered tr:last-child td:last-child {
  -moz-border-radius: 0 0 6px 0;
  -webkit-border-radius: 0 0 6px 0;
  border-radius: 0 0 6px 0;
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
        <table class="bordered">
          <c:forEach items="${model.dateStatistics}" var="stat">
            <tr>
              <td>${stat.key}</td>
              <td>${stat.value}</td>
            </tr>
          </c:forEach>
        </table>

        <p><a href="statistics.csv?type=dates">Export to CVS</a></p>
      </c:when>

      <c:when test="${model.type == 'recordTypes'}">
        <h3><spring:message code="apistat_by_type_t" /></h3>
        <table class="bordered">
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

        <p><a href="statistics.csv?type=${model.type}">Export to CVS</a></p>
        <%@ include file="/WEB-INF/jsp/default/admin/content/record-type-description.jspf" %>

      </c:when>

      <c:when test="${model.type == 'apiKeys'}">
        <h3><spring:message code="apistat_by_user_t" /></h3>

        <c:set var="defaultParams" value="type=${model.type}" />

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

        <table class="bordered">
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
                  <td>
                    <c:choose>
                      <c:when test="${stat.apiKey != '-'}"><a href="?type=apiKey&apiKey=${stat.apiKey}">${stat.apiKey}</a></c:when>
                      <c:otherwise>-</c:otherwise>
                    </c:choose>
                  </td>
                  <td align="right">${stat.count}</td>
                </tr>
              </c:forEach>
            </c:forEach>
            <tr class="total">
              <td>Total</td>
              <td>&mdash;</td>
              <td align="right">${model.userStatisticsTotal}</td>
            </tr>
          </tbody>
        </table>

        <p><a href="statistics.csv?type=${model.type}">Export to CVS</a></p>
      </c:when>

      <c:when test="${model.type == 'months'}">
        <h3><spring:message code="apistat_by_month_t" /></h3>
        <table class="bordered">
          <thead>
            <tr>
              <th>Month</th>
              <th>Count</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${model.monthStatistics}" var="stat">
              <tr>
                <td><a href="?type=month&month=${stat.month}">${stat.label}</a></td>
                <td>${stat.count}</td>
              </tr>
            </c:forEach>
            <tr class="total">
              <td>Total</td>
              <td>${model.monthStatisticsTotal}</td>
            </tr>
          </tbody>
        </table>

        <p><a href="statistics.csv?type=${model.type}">Export to CVS</a></p>
      </c:when>

      <c:when test="${model.type == 'apiKey'}">
        <h3>Usage info about user <em>${model.userName}</em> (API key: ${model.apiKey})</h3>

        <h4>By Type</h4>
        <table class="bordered">
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

        <p><a href="statistics.csv?type=${model.type}&apiKey=${model.apiKey}&stat=recordType">Export to CVS</a></p>
        <%@ include file="/WEB-INF/jsp/default/admin/content/record-type-description.jspf" %>

        <h4>By month</h4>
        <table class="bordered">
          <thead>
            <tr>
              <th>Month</th>
              <th>Count</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${model.monthStatistics}" var="stat">
              <tr>
                <td><a href="?type=month&month=${stat.month}">${stat.label}</a></td>
                <td>${stat.count}</td>
              </tr>
            </c:forEach>
            <tr class="total">
              <td>Total</td>
              <td>${model.monthStatisticsTotal}</td>
            </tr>
          </tbody>
        </table>

        <p><a href="statistics.csv?type=${model.type}&apiKey=${model.apiKey}&stat=month">Export to CVS</a></p>
      </c:when>

      <c:when test="${model.type == 'recordType'}">
        <h3>Usage info about API call type: <em>${model.recordType}</em></h3>

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

        <table class="bordered">
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
                  <td>
                    <c:choose>
                      <c:when test="${stat.apiKey != '-'}"><a href="?type=apiKey&apiKey=${stat.apiKey}">${stat.apiKey}</a></c:when>
                      <c:otherwise>-</c:otherwise>
                    </c:choose>
                  </td>
                  <td align="right">${stat.count}</td>
                </tr>
              </c:forEach>
            </c:forEach>
            <tr class="total">
              <td>Total</td>
              <td>&mdash;</td>
              <td align="right">${model.userStatisticsTotal}</td>
            </tr>
          </tbody>
        </table>
        <p><a href="statistics.csv?type=${model.type}&recordType=${model.recordType}&stat=apiKey">Export to CVS</a></p>

        <h4>By month</h4>
        <table class="bordered">
          <thead>
            <tr>
              <th>Month</th>
              <th>Count</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${model.monthStatistics}" var="stat">
              <tr>
                <td><a href="?type=month&month=${stat.month}">${stat.label}</a></td>
                <td>${stat.count}</td>
              </tr>
            </c:forEach>
            <tr class="total">
              <td>Total</td>
              <td>${model.monthStatisticsTotal}</td>
            </tr>
          </tbody>
        </table>

        <p><a href="statistics.csv?type=${model.type}&recordType=${model.recordType}&stat=month">Export to CVS</a></p>
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
        <table class="bordered">
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
                  <td>
                    <c:choose>
                      <c:when test="${stat.apiKey != '-'}"><a href="?type=apiKey&apiKey=${stat.apiKey}">${stat.apiKey}</a></c:when>
                      <c:otherwise>-</c:otherwise>
                    </c:choose>
                  </td>
                  <td align="right">${stat.count}</td>
                </tr>
              </c:forEach>
            </c:forEach>
            <tr class="total">
              <td>Total</td>
              <td>&mdash;</td>
              <td align="right">${model.userStatisticsTotal}</td>
            </tr>
          </tbody>
        </table>

        <p><a href="statistics.csv?type=${model.type}&month=${model.month}&stat=apiKey">Export to CVS</a></p>

        <h4><spring:message code="apistat_by_type_t" /></h4>
        <table class="bordered">
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

        <p><a href="statistics.csv?type=${model.type}&month=${model.month}&stat=recordType">Export to CVS</a></p>
        <%@ include file="/WEB-INF/jsp/default/admin/content/record-type-description.jspf" %>

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

