<!DOCTYPE html>

<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
	if(session.getAttribute("pl") == null) {
		String localeCode = Configuration.getInstance().getLocaleCode();
		session.setAttribute("pl", localeCode);
	}
	if(request.getParameter("pl") != null) {
		session.setAttribute("pl", request.getParameter("pl"));
	}
%>

<!--[if lt IE 7]>		<html lang='<%=session.getAttribute("pl")%>' class="lt-ie9 lt-ie8 lt-ie7">	<![endif]-->
<!--[if IE 7]>			<html lang='<%=session.getAttribute("pl")%>' class="lt-ie9 lt-ie8">			<![endif]-->
<!--[if IE 8]>			<html lang='<%=session.getAttribute("pl")%>' class="lt-ie9">				<![endif]-->
<!--[if IE 9]>			<html lang='<%=session.getAttribute("pl")%>' class="ie9">					<![endif]-->
<!--[if gt IE 9]>		<html lang='<%=session.getAttribute("pl")%>'>								<![endif]-->

<html lang='<%=session.getAttribute("pl") %>' />

<fmt:setLocale value='<%=session.getAttribute("pl") %>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />
<head>

<!--[if lt IE 9]> <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script> <![endif]-->
<!--[if IE 7]> <link rel="stylesheet" href="/de.uni_koeln.spinfo.maalr.user/css/font-awesome-ie7.css"> <![endif]-->

<title>${pageTitle}</title>
<meta http-equiv="X-UA-Compatible" content="IE=9">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="Robots" content="INDEX,FOLLOW">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
<link rel="shortcut icon" type="image/x-icon" href="${dictContext}/assets/img/favicon.png">
<style type="text/css">
	#show_results_noscript { display: none; }
</style>
<noscript>
<link href="${dictContext}/assets/style/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="${dictContext}/assets/style/user_style.css" rel="stylesheet" type="text/css">
<style type="text/css">
	#show_results_script { display: none; }
	#show_results_noscript { display: list-item; }
</style>
</noscript>
<link rel="search" type="application/opensearchdescription+xml" title="<%=Configuration.getInstance().getLongName()%>" href="/static/searchplugin.xml">
<script type="text/javascript">
	var site_name = "<%=Configuration.getInstance().getLongName()%>";
</script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="https://login.persona.org/include.js"></script>
<script type="text/javascript" src="${dictContext}/de.uni_koeln.spinfo.maalr.user/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${dictContext}/assets/js/jquery.screwdefaultbuttonsV2.min.js"></script>
<script type="text/javascript" src="${dictContext}/assets/js/pledari.js"></script>
<script type="text/javascript" src="${dictContext}/assets/js/pledari-persona.js"></script>
<script type="text/javascript" src="${dictContext}/de.uni_koeln.spinfo.maalr.user/js/dynamic_locale.js"></script>
<script type="text/javascript" src="${dictContext}/de.uni_koeln.spinfo.maalr.user/de.uni_koeln.spinfo.maalr.user.nocache.js"></script>
<script type="text/javascript" src="${dictContext}/de.uni_koeln.spinfo.maalr.user/js/dynamic_locale.js"></script>
<%-- <script type="text/javascript" src="//use.typekit.net/qvz4srm.js"></script> --%>
<%-- <script type="text/javascript">try{Typekit.load();}catch(e){}</script> --%>

<link href="${dictContext}/de.uni_koeln.spinfo.maalr.user/css/bootstrap.min.css" rel="stylesheet">
<link href="${dictContext}/de.uni_koeln.spinfo.maalr.user/css/gwt-bootstrap.css" rel="stylesheet">
<link href="${dictContext}/de.uni_koeln.spinfo.maalr.user/css/font-awesome.css" rel="stylesheet">
<link href="${dictContext}/de.uni_koeln.spinfo.maalr.user/css/user.css" rel="stylesheet">
</head>
