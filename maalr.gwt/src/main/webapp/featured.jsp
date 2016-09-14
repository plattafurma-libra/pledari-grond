<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<%-- HTML-HEAD --%>
<%@ include file="/maalr_modules/misc/htmlhead.jsp"%>

<body>
	<%-- NAVIGATION --%>
	<%@ include file="/maalr_modules/misc/header.jsp"%>

	<div>
		<div class="well" id="info-box">
			<%--
			<h5>
				<fmt:message key="info.header.${featured}" />
			</h5>
			<hr>
			 --%>
			<p>
				<fmt:message key="info.for.${featured}" />
			</p>
			<br>
			<p>
				<fmt:message key="anchor.to.${featured}" var="anchor" />
				<span style="color: #848484;">→ </span><a href="${anchor}"
					target="_blank" class="gwt-Anchor simpleLink"> <fmt:message
						key="anchor.to.${featured}" />
				</a>
			</p>
		</div>
	</div>

	<%-- FOOTER --%>
	<jsp:include page="/maalr_modules/misc/footer.jsp" />

	<%-- GWT AJAX BROWSER HISTORY SUPPORT --%>
	<%-- <iframe src="javascript:''" id="__gwt_historyFrame" style="width: 0; height: 0; border: 0"></iframe> --%>

	<jsp:include page="/analytics.jsp" />
	
		<script type="text/javascript">

	$(document).ready(function(){
    if("${featured}" == "puter") {
        document.getElementById("st_brand_title").className = 'dict_navi_item';
        document.getElementById("pt_brand_title").className = 'dict_navi_item selected';
    }
    if("${featured}" == "vallader") {
        document.getElementById("st_brand_title").className = 'dict_navi_item';
        document.getElementById("vl_brand_title").className = 'dict_navi_item selected';
    }
    if("${featured}" == "sursilvan") {
        document.getElementById("st_brand_title").className = 'dict_navi_item';
        document.getElementById("sr_brand_title").className = 'dict_navi_item selected';
    }
});

</script>
	
</body>
</html>