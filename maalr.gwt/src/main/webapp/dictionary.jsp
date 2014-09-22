<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/maalr_modules/misc/htmlhead.jsp" />
	<body>
		<jsp:include page="/maalr_modules/misc/header.jsp" />
		<%@ include file="/maalr_modules/misc/language_widget.jsp"%>
		<%@ include file="/maalr_modules/misc/login_widget.jsp"%>
	
		<div id="content" class="container container_entry">
		
			<jsp:include page="/maalr_modules/browse/entry.jsp" />
		
		</div>
		
		<div id="bottom">
		
			<jsp:include page="/maalr_modules/misc/footer.jsp" />
			
		</div>
		
		<iframe src="javascript:''" id="__gwt_historyFrame" style="width: 0; height: 0; border: 0"></iframe>
		
		<jsp:include page="/analytics.jsp" />
	</body>
</html>