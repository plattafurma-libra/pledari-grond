<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.Locale" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<h1>Infurmaziùns generalas</h1>

<p>
	<strong>Questa versiùn online digl vocabulari sutsilvan sabasa segl Pledari sutsilvan-tudestg/Wörterbuch Deutsch-Sutsilvan c’e vagnieu redigieu da Wolfgang Eichenhofer ad edieu igls 2002 da Mieds d'instrucziùn digl Grischùn. Tutegna sco an lez satgatan las furmas tumleastgegnas trànter parantesas angularas.</strong>
</p>

<p>
	<% NumberFormat nf = NumberFormat.getNumberInstance(locale); %>
	<fmt:message key="maalr.index.entry_count" var="numberOfEntries">
		<fmt:param><%=nf.format(DictionaryStatistics.getStatistics().entryCounter)%></fmt:param>
	</fmt:message>
	${numberOfEntries}.
</p>

<p>Igl vocabulari d’egn lungatg e dantànt mena cumplet a termino. Aschigî c’igl lungatg viva, sasvilupescha a crescha quel, s'adatànd organicameing agls basegns comunicativs da la cuminànza linguistica. Tut las utilisadras a tut igls utilisaders en anvidos da cuntribuir activameing agl svilup digl Pledari sutsilvan online. Tgi ca tgata betg egn pled near egn'expressiùn sa far propostas cugl formular coraspundaint (clicar segl butùn 'far egna proposta' ad igl formular cumpara). Tgi ca veza egn sbagl sa proponer la corectura near scriver egna remartga mademameing cugl formular coraspundaint (clicar segl butùn 'modifitgear' ad igl formular cumpara).</p>

<%-- 
<p>Questa pagina-web utilisescha Google Analytics, in servetsch per analisar il diever da websites da Google Inc. ("Google"). Google Analytics fa diever dad uschenumnads cookies. I sa tracta da datotecas da text che vegnan memorisadas sin Voss computer. Quellas pussibiliteschan d'analisar Voss diever da la pagina-web. Las infurmaziuns davart Voss diever da questa pagina-web (incl. Vossa adressa IP), generadas dal cookie, vegnan transmessas ad in server da Google en ils Stadis Unids da l'America ed arcunadas là. Google vegn a far diever da questas infurmaziuns per analisar co che Vus utilisais la pagina-web, per cumpilar rapports davart las activitads sin la pagina-web per mauns dals administraturs da la pagina-web sco era per porscher ulteriurs servetschs colliads cun l'utilisaziun da la pagina-web e da l'internet. Google po dar vinavant questas infurmaziuns a terzs, premess che quai saja prescrit legalmain ubain uschenavant che terzs elavuran questas datas per incumbensa da Google. Google na vegn en nagin cas a metter en connex Vossa adressa IP cun autras datas da Google. Vus pudais configurar Voss navigatur uschia che l'installaziun da cookies vegn impedida. En quest cas na pudais Vus però eventualmain betg utilisar cumplettamain tut las funcziunalitads da questa pagina-web. Cun utilisar questa pagina-web declerais Vus dad esser d'accord che Google elavura datas davart Vus tenor il proceder descrit e per l'intent numnà.</p>
 --%>
 
<p>
	<b> © Lia Rumantscha, CH-7000 Cuira</b>
</p>

<p>
	<b>Concept a realisaziùn</b>
	<br>Lia Rumantscha, Daniel Telli
</p>

<p>
	<b>Redacziùn actuala</b> 
	<br>Johann Clopath
</p>

<p>
	<b>Programs</b>
	<br>Jürgen Rolshoven
	<br>Claes Neuefeind
	<br>Stephan Schwiebert
	<br>Mihail Atanassov
	<br>(Institut für Linguistik, Sprachliche Informationsverarbeitung, Universität zu Köln)
</p>

<p>
	<b>Design grafic</b>
	<br>Remo Caminada, www.remocaminada.com
</p>
