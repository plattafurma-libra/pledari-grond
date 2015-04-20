<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.Locale" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="<%=session.getAttribute("pl")%>" />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<h1>Infurmaziuns generalas</h1>

<p>
	<strong>Chesta versiun online digl vocabulari surmiran sa basa sen igl Vocabulari surmiran-tudestg/Wörterbuch Deutsch-Surmiran, versiun disc cumpact, © 2004 <a target="_blank" href="http://www.liarumantscha.ch/">Leia Rumantscha</a>, redigia da Faust Signorell ed oters.
	</strong>
</p>

<p>Actualmaintg cuntigna igl vocabulari</p>

<p>
	<%
		// 	String languageTag = (String) session.getAttribute("pl");
		// 	Locale locale = Locale.forLanguageTag(languageTag);
		NumberFormat nf = NumberFormat.getNumberInstance(locale);
	%>
	<fmt:message key="maalr.index.entry_count" var="numberOfEntries">
		<fmt:param><%=nf.format(DictionaryStatistics.getStatistics().entryCounter)%></fmt:param>
	</fmt:message>
	${numberOfEntries}.
</p>

<p>Igl vocabulari d’en lungatg n’è dantant mai cumplet e termino. Schi dei tgi igl lungatg veiva, sa sviluppescha'l e crescha, s'adattond organicamaintg agls basigns communicativs dalla cuminanza linguistica. Tot las utilisadras e tot igls utilisaders èn anvidos da contribueir activamaintg agl svilup digl Vocabulari surmiran online. Tgi tgi na catta betg en pled u en'expressiun pò far propostas cun igl formular correspondent (cliccar sen igl battung 'far ena proposta' ed igl formular cumpara). Tgi tgi vei en sbagl pò proponer la correctura u screiver ena remartga mademamaintg cun igl formular correspondent (cliccar sen igl battung 'modifitgier' ed igl formular cumpara).</p>

<p>Questa pagina-web utilisescha Google Analytics, in servetsch per analisar il diever da websites da Google Inc. ("Google"). Google Analytics fa diever dad uschenumnads cookies. I sa tracta da datotecas da text che vegnan memorisadas sin Voss computer. Quellas pussibiliteschan d'analisar Voss diever da la pagina-web. Las infurmaziuns davart Voss diever da questa pagina-web (incl. Vossa adressa IP), generadas dal cookie, vegnan transmessas ad in server da Google en ils Stadis Unids da l'America ed arcunadas là. Google vegn a far diever da questas infurmaziuns per analisar co che Vus utilisais la pagina-web, per cumpilar rapports davart las activitads sin la pagina-web per mauns dals administraturs da la pagina-web sco era per porscher ulteriurs servetschs colliads cun l'utilisaziun da la pagina-web e da l'internet. Google po dar vinavant questas infurmaziuns a terzs, premess che quai saja prescrit legalmain ubain uschenavant che terzs elavuran questas datas per incumbensa da Google. Google na vegn en nagin cas a metter en connex Vossa adressa IP cun autras datas da Google. Vus pudais configurar Voss navigatur uschia che l'installaziun da cookies vegn impedida. En quest cas na pudais Vus però eventualmain betg utilisar cumplettamain tut las funcziunalitads da questa pagina-web. Cun utilisar questa pagina-web declerais Vus dad esser d'accord che Google elavura datas davart Vus tenor il proceder descrit e per l'intent numnà.</p>

<p>
	<b> © Leia Rumantscha, CH-7000 Coira </b>
</p>

<p>
	<b>Concept e realisaziun</b>
	<br>
	Leia Rumantscha, Daniel Telli 
</p>

<p>
	<b>Redacziun actuala</b>
	<br>
	Reto Capeder
</p>

<p>
	<b>Anteriurs redacturs </b> <br> Georges Darms <br> Manfred
	Gross
</p>

<p>
	<b>Programs</b>
	<br>
	Jürgen Rolshoven
	<br>
	Claes Neuefeind
	<br>
	Stephan Schwiebert
	<br>
	Mihail Atanassov
	<br>
	(Institut für Linguistik,
	Sprachliche Informationsverarbeitung, Universität zu Köln)
</p>

<p>
	<b>Dessegn grafic</b>
	<br>Remo Caminada, www.remocaminada.com
</p>
