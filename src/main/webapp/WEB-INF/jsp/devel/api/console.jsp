<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">
<!--
#api-result {
  font-family: sans-serif;
  font-size: 0.8em;
}
-->
</style>
<table id="api-console">
  <tr valign="top">
    <td width="30%" id="api-form">
      <form>
        <label for="function">API function:</label><br/>
          <c:forEach items="${model.defaultFunctions}" var="function">
            <input type="radio" name="function" id="function_${function}" value="${function}" <c:if test="${model.function == function}">checked="checked"</c:if> />
            <label for="function_${function}">${function}</label><br />
          </c:forEach>

        <fieldset id="search_params">
          <legend>Search parameters</legend>

          <label for="query">Search term:</label><br/>
          <input type="text" id="query" name="query" value="${model.query}" /><br/>

          <label for="qf">Refinement term(s):</label><br/>
          <c:forEach items="${model.refinements}" var="qf">
            <input type="text" id="qf" name="qf" value="${qf}" /><br/>
          </c:forEach>
          <input type="text" id="qf" name="qf" value="${qf}" /><br/>

          <label for="profile">profile:</label><br/>
          <select id="profile" name="profile">
            <c:forEach items="${model.defaultProfiles}" var="profile">
              <option value="${profile}" <c:if test="${model.profile == profile}">selected="selected"</c:if>>${profile}</option>
            </c:forEach>
          </select><br/>

          <label for="rows">number of records:</label><br/>
          <select id="rows" name="rows">
            <c:forEach items="${model.defaultRows}" var="rows">
              <option value="${rows}" <c:if test="${model.rows == rows}">selected="selected"</c:if>>${rows}</option>
            </c:forEach>
          </select><br/>

          <label for="start">first record to retrieve:</label><br/>
          <input type="text" name="start" value="${model.start}" /><br/>
        </fieldset>

        <fieldset id="record_params">
          <legend>Record parameters</legend>

          <label for="collectionId">Collection id:</label><br/>
          <input type="text" id="collectionId" name="collectionId" value="${model.collectionId}" /><br/>

          <label for="recordId">Record id:</label><br/>
          <input type="text" id="recordId" name="recordId" value="${model.recordId}" /><br/>
        </fieldset>

        <input type="submit" value="get result">
      </form>
    </td>
    <td id="api-result">${model.jsonString}</td>
  </tr>
</table>