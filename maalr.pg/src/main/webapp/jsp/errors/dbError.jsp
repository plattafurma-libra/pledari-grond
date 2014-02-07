<%@page import="java.io.PrintStream"%>
<%@page import="org.springframework.security.openid.OpenIDAttribute"%>
<%@page import="java.util.List"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.openid.OpenIDAuthenticationToken"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%-- Header included here --%>
<jsp:include page="/jspstatic/htmlhead.jsp" />
<body>
	<div id="top">
		<jsp:include page="/jspstatic/header_small.jsp" />
	</div>
	<div id="bottom"><jsp:include page="/jspstatic/footer.jsp" /></div>
	<div id="content">
		<div class="error">An error occurred when accessing the database.</div>
		<img src="http://upload.wikimedia.org/wikipedia/commons/thumb/9/96/Josua_Maaler_%281529%E2%80%931599%29.jpg/220px-Josua_Maaler_%281529%E2%80%931599%29.jpg"/>
		<div class="exception">
			${exception}
		</div>
		<div class="solution">Bitte melden Sie sich als Administrator an und <a href="/admin/importDatabase.html">importieren Sie die Datenbank neu.</a></div>
	</div>
</body>
</html>