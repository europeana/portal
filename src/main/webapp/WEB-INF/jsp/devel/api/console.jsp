<%@ include file="/WEB-INF/jsp/devel/_common/include.jsp"%>
<%@ include file="/WEB-INF/jsp/devel/_common/html/doctype.jsp"%>
<%@ include file="/WEB-INF/jsp/devel/_common/variables/variables.jsp"%>

<%@ include file="/WEB-INF/jsp/devel/_common/html/open-html.jsp"%>
<%@ include file="/WEB-INF/jsp/devel/_common/html/header.jsp"%>

<style type="text/css">
<!--
#api-form {width: 250px}

#api-form fieldset {
  border: 1px solid #ccc;
  padding: 5px;
  display: none;
}

#api-form fieldset#${model.function}-panel {
  display: block;
}

#api-form fieldset legend {
  font-weight: bold;
}

#api-form p.section {
  margin-bottom: 5px;
}

#api-result #request-url, #api-result #response {
  font-family: sans-serif;
  font-size: 0.8em;
}

h2 {
  font-family: sans-serif;
  margin: 0 0 5px;
}

#request-url, #response {
  background: none repeat scroll 0 0 #F0F4F5;
  border: 1px solid #999999;
  margin-bottom: 10px;
  padding: 10px;
}
-->
</style>
<script type="text/javascript">
<!--
var selectedPanel = '${model.function}';
//-->
</script>

<table id="api-console" width="100%">
  <tr valign="top">
    <td id="api-form">
      <form>
        <h2><spring:message code="apiconsole_function_t" />:</h2>
        <p class="section">
          <c:forEach items="${model.defaultFunctions}" var="function">
            <input type="radio" name="function" id="api-function-${function}" value="${function}" <c:if test="${model.function == function}">checked="checked"</c:if> />
            <label for="api-function-${function}">${function}</label>
          </c:forEach>
        </p>

        <fieldset id="search-panel">
          <legend><spring:message code="apiconsole_search_parameters_t" /></legend>

          <label for="api-query"><spring:message code="apiconsole_query_t" />:</label><br/>
          <input type="text" id="api-query" name="query" value="${model.query}" /><br/>

          <label for="api-qf"><spring:message code="apiconsole_refinements_t" />:</label><br/>
          <c:forEach items="${model.refinements}" var="qf">
            <input type="text" id="api-qf" name="qf" value="${qf}" /><br/>
          </c:forEach>
          <input type="text" id="api-qf" name="qf" value="${qf}" /><br/>

          <label for="api-profile"><spring:message code="apiconsole_profile_t" />:</label><br/>
          <c:forEach items="${model.defaultProfiles}" var="profile">
            <%-- option value="${profile}" <c:if test="${model.profile == profile}">selected="selected"</c:if>>${profile}</option --%>
            <input type="radio" name="profile" id="api-profile-${profile}" value="${profile}" <c:if test="${model.profile == profile}">checked="checked"</c:if> />
            <label for="api-profile-${profile}">${profile}</label><br/>
          </c:forEach>
          

          <p class="section">
            <label for="api-rows"><spring:message code="apiconsole_rows_t" />:</label><br/>
            <c:forEach items="${model.defaultRows}" var="rows">
              <input type="radio" name="rows" id="api-rows-${rows}" value="${rows}" <c:if test="${model.rows == rows}">checked="checked"</c:if> />
              <label for="api-rows-${rows}">${rows}</label>
            </c:forEach>
          </p>
          
          <p class="section">
            <label for="api-start"><spring:message code="apiconsole_start_t" />:</label><br/>
            <input type="text" name="start" value="${model.start}" />
          </p>
        </fieldset>

        <fieldset id="record-panel">
          <legend><spring:message code="apiconsole_record_parameters_t" /></legend>
          <%--
          <label for="api-collectionId">Collection id:</label><br/>
          <input type="text" id="api-collectionId" name="collectionId" value="${model.collectionId}" /><br/>
           --%>

          <label for="api-recordId"><spring:message code="apiconsole_record_id_t" />:</label><br/>
          <input type="text" id="api-recordId" name="recordId" value="${model.recordId}" /><br/>
        </fieldset>

        <input type="submit" value="get result">
      </form>
    </td>
    <td id="api-result">
      <div id="request-url-title"><h2><spring:message code="apiconsole_request_url_t" /></h2></div>
      <div id="request-url">${model.apiUrl}</div>
      <div id="response-title"><h2><spring:message code="apiconsole_response_t" /></h2></div>
      <div id="response">${model.jsonString}</div>
    </td>
  </tr>
</table>

  <%@ include file="/WEB-INF/jsp/devel/_common/html/footer.jsp"%>
  <%@ include file="/WEB-INF/jsp/devel/_common/html/javascripts.jsp"%>
<%@ include file="/WEB-INF/jsp/devel/_common/html/close-html.jsp"%>
