<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List"%>

<%@ page import="javax.security.auth.login.LoginContext" %>

<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.description.UseCase" %>
<%@ page import="de.uni_koeln.spinfo.maalr.login.LoginManager" %>
<%@ page import="de.uni_koeln.spinfo.maalr.lucene.query.QueryResult" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- HTML HEADER --%>
<jsp:include page="/maalr_modules/misc/htmlhead.jsp" />

	<body>
		<div id="top">
			<jsp:include page="/maalr_modules/misc/header.jsp" />
		</div>
		
		<div>
			
			<%@ include file="/maalr_modules/misc/language_widget.jsp"%>
		
			<%@ include file="/maalr_modules/misc/login_widget.jsp"%>
			
			<jsp:include page="/maalr_modules/browse/browse.jsp"/>
		</div>
		
		<div id="bottom"><jsp:include page="/maalr_modules/misc/footer.jsp" /></div>
		<jsp:include page="/analytics.jsp" />
	</body>
</html>

