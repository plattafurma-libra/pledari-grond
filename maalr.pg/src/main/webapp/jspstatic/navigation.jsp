<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration"%>

<%-- 
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.Role"%>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.Constants"%>
<%@ page import="de.uni_koeln.spinfo.maalr.login.MaalrUserInfo"%>
<%@ page import="de.uni_koeln.spinfo.maalr.login.LoginManager"%>
<%@ page import="de.uni_koeln.spinfo.maalr.webapp.i18n.UrlGenerator"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='cr' uri='http://java.sun.com/jstl/core_rt'%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
<fmt:setLocale value="<%=session.getAttribute("pl") %>" />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />
--%>
 
<div id="brand_title">
	<a class="brand active" href="/"><%=Configuration.getInstance().getLongName()%></a>
</div>