<%@page import="de.uni_koeln.spinfo.maalr.webapp.i18n.UrlGenerator"%>
<%@page import="de.uni_koeln.spinfo.maalr.common.shared.Role"%>
<%@page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration"%>
<%@page import="de.uni_koeln.spinfo.maalr.login.MaalrUserInfo"%>
<%@page import="de.uni_koeln.spinfo.maalr.login.LoginManager"%>
<%@ taglib prefix='cr' uri='http://java.sun.com/jstl/core_rt'%>

<div class="navbar" id="navigation">
	<div class="navbar-inner">
		<div class="container">
			<a class="brand active" href="/"><%=Configuration.getInstance().getLongName()%></a>
			<ul class="nav">
				<li><%@ include file="/maalr_modules/login/loginmodule.jsp"%></li>
				<li><a href="/translate.html"><i></i><span class="">
							Search </span></a></li>
				<li><a href="/browse.html"><i></i><span class="">
							Dictionary </span></a></li>
				<li><a href="#" id="propose_navi"><i></i><span class="">
							Suggest New Entry </span></a></li>
				<%
					MaalrUserInfo user = (MaalrUserInfo) session.getAttribute("user");
						if (user != null && user.getRole().equals(Role.ADMIN_5)) {
				%>
			<li><a href="/admin/admin.html"><i></i><span class="">
							Admin Backend </span></a></li>
	<%
}
%>
<%
if (user != null && user.getRole().equals(Role.TRUSTED_IN_4)) {
	%>
			<li><a href="/editor/editor.html"><i></i><span class="">
							Editor Backend </span></a></li>
	<%
}
%>
			</ul>
		</div>
	</div>
</div>



