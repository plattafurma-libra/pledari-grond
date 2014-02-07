<%@page import="de.uni_koeln.spinfo.maalr.common.shared.description.UseCase"%>
<%@page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration"%>
<%@page import="de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription"%>
<%@page import="de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion"%>
<%@page import="java.util.List"%>
<%@page import="de.uni_koeln.spinfo.maalr.lucene.query.QueryResult"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="javax.security.auth.login.LoginContext"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="de.uni_koeln.spinfo.maalr.login.LoginManager"%>
<%-- Header included here --%>
<jsp:include page="/jsp/modules/htmlhead.jsp" />
<body>
	<div id="top">
		<jsp:include page="/jsp/modules/header_small.jsp" />
	</div>
	<div id="content">
		<jsp:include page="/maalr_modules/browse/browse.jsp"/>
	</div>
		
</body>
</html>

