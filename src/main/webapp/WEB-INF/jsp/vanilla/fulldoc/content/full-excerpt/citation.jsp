<div id="citation">
	<c:forEach items="${model[citeStyles]}" var="citeStyle">
		<div class="header">
			<div class="heading"><spring:message code="Cite_Header_t" />
				<%-- 
				The multiple nbsp; are needed to fix the empty-link-clickable-area bug in IE7.
				The 'background-image' / 'background-color' / 'transparent-border' fixes were ineffective here.
				This blank text is combined with css settings of overflow: hidden and font-size:500%.
				--%>
				<a href="" class="close-button" title="<spring:message code="Close_Button_t" />" rel="nofollow">&nbsp;</a>
			</div>
		</div>
		
		<div id="citations">
			<div class="citation">
				${citeStyle.citeText}
			</div>
			<div class="citation">
				&lt;ref&gt;${citeStyle.citeText}&lt;/ref&gt;
			</div>
		</div>
	</c:forEach>
</div>
