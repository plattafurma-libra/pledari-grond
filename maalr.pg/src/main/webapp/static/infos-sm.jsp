<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.Locale" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<h1>Infurmaziuns generalas</h1>

<p>
	<strong>
		Chesta versiun online digl vocabulari surmiran sa basa sen igl Vocabulari surmiran-tudestg/Wörterbuch Deutsch-Surmiran, versiun disc cumpact, ediziun da licenza © 2004 
		Meds d'instrucziun digl Grischun; sistem dad elavuraziun da datas linguisticas © 2004 Leia Rumantscha; redacziun Faust Signorell ed oters.
	</strong>
</p>
<p>
	Actualmaintg cuntigna igl vocabulari
</p>

<p>
	<%
		// 	String languageTag = (String) session.getAttribute("locale");
		// 	Locale locale = Locale.forLanguageTag(languageTag);
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag(languageTag));
	%>
	<fmt:message key="maalr.index.entry_count" var="numberOfEntries">
		<fmt:param><%=nf.format(DictionaryStatistics.getStatistics().entryCounter)%></fmt:param>
	</fmt:message>
	${numberOfEntries}.
</p>

<p>
	Igl vocabulari d'en lungatg n'è dantant mai cumplet e termino. Schi dei tgi igl lungatg veiva, sa sviluppescha'l e crescha, s'adattond organicamaintg agls basigns communicativs dalla cuminanza linguistica. Tot las utilisadras e tot igls utilisaders èn anvidos da contribueir activamaintg agl svilup digl Vocabulari surmiran online. Tgi tgi na catta betg en pled u en'expressiun pò far propostas cun igl formular correspondent (cliccar sen igl battung 'far ena proposta' ed igl formular cumpara). Tgi tgi vei en sbagl pò proponer la correctura u screiver ena remartga mademamaintg cun igl formular correspondent (cliccar sen igl battung 'modifitgier' ed igl formular cumpara).
</p>

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
