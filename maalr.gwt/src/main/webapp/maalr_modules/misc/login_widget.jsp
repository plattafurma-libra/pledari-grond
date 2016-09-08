<%@ page import="javax.security.auth.login.LoginContext" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.Role" %>
<%@ page import="de.uni_koeln.spinfo.maalr.login.custom.PGAuthenticationProvider" %>
<%@ page import="de.uni_koeln.spinfo.maalr.login.MaalrUserInfo" %>

<%-- LOGIN --%>
<div id="login-widget">
	<ul class="nav">
		<%
			MaalrUserInfo user = (MaalrUserInfo) session.getAttribute("user");
			if (user != null && user.getRole().equals(Role.ADMIN_5)) {
		%>
		<li><a href="${dictContext}/admin/admin.html"><i></i><span class="">Admin Backend </span></a></li>
		<%
			}
		%>
		<%
			if (user != null && user.getRole().equals(Role.TRUSTED_IN_4)) {
		%>
		<li><a href="${dictContext}/editor/editor.html"><i></i><span class="">Editor Backend </span></a></li>
		<%
			}
		%>
		<%
			if (user != null && user.getRole().equals(Role.TRUSTED_EX_3)) {
		%>
		<li><a href="${dictContext}"><i></i><span class="">Editor Backend </span></a></li>
		<%
			}
		%>
		<%-- <li><%@ include file="/maalr_modules/login/loginmodule.jsp"%></li> --%>
	</ul>
</div>