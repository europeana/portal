<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


	<style type="text/css">


.carousel-wrapper {
    height: 200px;
    overflow: hidden;
    position: relative;
    margin: 1em 0 1em 0;
}

.carousel {
    background-color: #FFFF00;
    margin-bottom: 2em;
    overflow: hidden;
    width: 100%;
}

.carousel-items {
    background-color: #FFA500;
    position: relative;
}

.carousel-items .info {
  -moz-box-sizing: border-box;
  background-color: rgba(0, 0, 0, 0.698);
  border-radius: 0 0 0.5em 0.5em;
  bottom: 0;
  color: #FFFFFF;
  display: block;
  height: 2.7em;
  padding: 0.2em;
  position: absolute;
  text-align: left;
  width: 100%;
}

.carousel-item {
    -moz-box-sizing: border-box;
    background: none repeat scroll 0 0 #FFFFFF;
    border: 1px solid #E4E4E4;
    border-radius: 0.5em;
    display: block;
    float: left;
    height: 200px;
    position: relative;
    text-align: center;
    transition: background-color 1s ease-in-out 0s;
    width: 200px;
}

.carousel-item img{
	position: relative;
	top: 0.25em;
}

.carousel-left,
.carousel-right {
	line-height: 200px;
    font-size:   6em;
    z-index:     1;
    position:    absolute;	
    background-color: rgba(0, 0, 0, 0) !important;
    color: #FFFFFF;
    text-shadow: 0 0 10px #000000;
}


.carousel-left {
    left: -0.12em;
}

.carousel-right {
    right: -0.12em;
}


	</style>



<div class="twelve columns">
	<h3 id="collapse-header-3">
		<span class="left collapse-header-text">
			<spring:message code="latest_on_pinterest_t" />
		</span>

		<%-- without the &nbsp before the icon span this link will not take tab focus in chrome --%>

		<a	title="<spring:message code="europeana-pinterest-title_t" />"
			href="<spring:message code="europeana-pinterest-url" />"
			target="<spring:message code="europeana-pinterest-target" />"
			rel="me">
				&nbsp;<span class="icon-pinterest left"></span>
		</a>

		<span class="collapse-icon"></span>
	</h3>

	<div class="collapse-content">
	</div>
</div>
