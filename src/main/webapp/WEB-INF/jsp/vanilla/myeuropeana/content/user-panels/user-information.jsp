<h3><a  href="#user-information" title="<spring:message code='UserInformation_t' />"><spring:message code='UserInformation_t'/></a></h3>
<spring:message 'Username_t'/>: ${model.user.userName}
<spring:message code='EmailAddress_t'/>: ${model.user.email}
<c:if test="${model.user.firstName}"><spring:message code='FirstName_t'/>: ${model.user.firstName}</c:if>
<c:if test="${model.user.lastName}"><spring:message code='lastName_t'/>:	${model.user.lastName}</c:if>