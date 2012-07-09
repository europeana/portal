<spring:message code='MyEuropeanaExplain_t' />
<%@ include file="/WEB-INF/jsp/vanilla/myeuropeana/content/login/login-form.ftl" %>
<%@ include file="/WEB-INF/jsp/vanilla/myeuropeana/content/login/register-form.ftl" %>
<%@ include file="/WEB-INF/jsp/vanilla/myeuropeana/content/login/request-password-form.ftl" %>
<c:if test="model.errorMessage}">${model.errorMessage}</c:if>
<c:if test="${model.forgotSuccess}"><spring:message code='AnEmailHasBeenSentTo_t' />: ${model.email}.<spring:message code='PleaseFollowTheLinkProvided_t' />.</c:if>
<c:if test="${model.failureForgotFormat}"><spring:message code='Error_t' />!<spring:message code='EmailFormatError_t' />.</c:if>
<c:if test="${model.failureForgotDoesntExist}"><spring:message code='Error_t' />!<spring:message code='EmailDoesntExist_t' /></c:if>
<c:if test="${model.success}"><spring:message code='AnEmailHasBeenSentTo_t' />: <b>${model.email}</b>.<spring:message code='PleaseFollowTheLinkProvided_t' />.</c:if>
<c:if test="${model.failureFormat}"><spring:message code='Error_t' />!</b> <spring:message code='EmailFormatError_t' />.</c:if>
<c:if test="${model.failureExists}"><spring:message code='Error_t' />!</b> <spring:message code='EmailAlreadyRegistered_t' />.</c:if>