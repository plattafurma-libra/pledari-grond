<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ page import="java.util.Locale"%>
<%@ page import="java.text.NumberFormat"%>

<%@ page import="de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<h1>Infurmaziuns generalas</h1>

<p>
	<strong>Versiun online da la banca da datas linguisticas da la
		Lia Rumantscha (LR) installada en l'internet en collavuraziun cun la
		Giuventetgna Rumantscha (<a href="http://www.giuru.ch/"
		target="_blank">GiuRu</a>).
	</strong>
</p>

<p>
	<%
		// 	String languageTag = (String) session.getAttribute("pl");
		// 	Locale locale = Locale.forLanguageTag(languageTag);
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag(languageTag));
	%>
	<fmt:message key="maalr.index.entry_count" var="numberOfEntries">
		<fmt:param><%=nf.format(DictionaryStatistics.getStatistics().entryCounter)%></fmt:param>
	</fmt:message>
	${numberOfEntries}.
</p>

<p>Quest Pledari Grond online sa basa sin la banca da datas
	linguisticas da la LR, sviluppada dapi il 1982 en connex cun la
	creaziun dal rumantsch grischun, l'innovaziun linguistica e l’ulteriura
	standardisaziun dal rumantsch grischun. Il num "PLEDARI" stat per ina
	collecziun da pleds senza tut las indicaziuns grammaticalas usitadas en
	dicziunaris e vocabularis tradiziunals, dentant cun numerusas propostas
	per il diever dals pleds e modas da dir actualas. Il Pledari Grond
	online è la pli voluminusa collecziun da pleds rumantschs accessibla al
	public. Il Pledari Grond online vegn actualisà mintga di tant da la
	redacziun sco era da ses utilisaders.</p>

<p>In pledari d’ina lingua n’è dentant mai cumplet e terminà. Uschè
	ditg che la lingua viva, sa sviluppa e crescha quel, s'adattond
	organicamain als basegns communicativs da la cuminanza linguistica. Tut
	las utilisadras e tut ils utilisaders èn envidads da contribuir
	activamain al svilup dal Pledari Grond online. Tgi che na chatta betg
	in pled u in'expressiun po far propostas cun il formular correspundent
	(cliccar sin il buttun 'far ina proposta' ed il formular cumpara). Tgi
	che vesa in sbagl po proponer la correctura u scriver ina remartga
	medemamain cun il formular correspundent (cliccar sin il buttun
	'modifitgar' ed il formular cumpara).</p>

<p>Questa pagina-web utilisescha Google Analytics, in servetsch per
	analisar il diever da websites da Google Inc. ("Google"). Google
	Analytics fa diever dad uschenumnads cookies. I sa tracta da datotecas
	da text che vegnan memorisadas sin Voss computer. Quellas
	pussibiliteschan d'analisar Voss diever da la pagina-web. Las
	infurmaziuns davart Voss diever da questa pagina-web (incl. Vossa
	adressa IP), generadas dal cookie, vegnan transmessas ad in server da
	Google en ils Stadis Unids da l'America ed arcunadas là. Google vegn a
	far diever da questas infurmaziuns per analisar co che Vus utilisais la
	pagina-web, per cumpilar rapports davart las activitads sin la
	pagina-web per mauns dals administraturs da la pagina-web sco era per
	porscher ulteriurs servetschs colliads cun l'utilisaziun da la
	pagina-web e da l'internet. Google po dar vinavant questas infurmaziuns
	a terzs, premess che quai saja prescrit legalmain ubain uschenavant che
	terzs elavuran questas datas per incumbensa da Google. Google na vegn
	en nagin cas a metter en connex Vossa adressa IP cun autras datas da
	Google. Vus pudais configurar Voss navigatur uschia che l'installaziun
	da cookies vegn impedida. En quest cas na pudais Vus però eventualmain
	betg utilisar cumplettamain tut las funcziunalitads da questa
	pagina-web. Cun utilisar questa pagina-web declerais Vus dad esser
	d'accord che Google elavura datas davart Vus tenor il proceder descrit
	e per l'intent numnà.</p>

<p>
	<b> © Lia Rumantscha, CH-7000 Cuira</b>
</p>

<p>
	<b>Concept e realisaziun </b> <br> Lia Rumantscha, Anna-Alice
	Dazzi e Daniel Telli
</p>

<p>
	<b>Redacziun actuala dal Pledari Grond online </b> <br> Daniel
	Telli <br> Marietta Cathomas Manetsch <br> Gabriela
	Holderegger Pajarola <br> Angela Schmed Bass <br> Marina Wyss
</p>

<p>
	<b>Anteriuras redacturas ed anteriurs redacturs</b> <br> Georges
	Darms <br> Anna-Alice Dazzi Gross <br> Manfred Gross <br> Annetta Zini
</p>

<p>
	<b>Programs</b> <br>Jürgen Rolshoven <br>Claes Neuefeind <br>Stephan
	Schwiebert <br>Mihail Atanassov <br>(Institut für Linguistik,
	Sprachliche Informationsverarbeitung, Universität zu Köln)
</p>

<p>
	<b>Design grafic</b> <br>Remo Caminada, www.remocaminada.com
</p>
