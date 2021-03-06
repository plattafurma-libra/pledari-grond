<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ page import="java.util.Locale"%>
<%@ page import="java.text.NumberFormat"%>

<%@ page import="de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<h1>Allgemeine Informationen</h1>

<p>
	<strong>Online Version der linguistischen Datenbank der Lia
		Rumantscha (LR) bearbeitet für das Internet in Zusammenarbeit mit der
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

<p>Der Pledari Grond online basiert auf der linguistischen Datenbank
	der LR, entwickelt im Jahre 1982 in Zusammenhang mit der Schaffung des
	Rumantsch Grischun, mit sprachplanerischen Massnahmen sowie der
	weiteren Standardisierung des Rumantsch Grischun. Der Name "PLEDARI"
	steht für eine Wörtersammlung ohne grammatikalische Angaben, wie man
	sie von traditionellen Wörterbüchern gewohnt ist. Dafür bietet das
	Pledari Grond online zahlreiche Vorschläge für den Gebrauch von Wörtern
	und Redewendungen. Es ist die umfangreichste Sammlung romanischer
	Wörter und Ausdrücke, die heute dem Publikum zugänglich ist. Der
	Pledari Grond online wird durch die Redaktion und mit Hilfe der
	Nutzerinnen und Nutzer täglich aktualisiert.</p>

<p>Der Wortschatz hat keine Grenzen. So lange die Sprache lebt,
	wächst und entwickelt sich dieser weiter und passt sich organisch den
	Kommunikationsbedürfnissen der Sprachgemeinschaft an. Alle Nutzerinnen
	und Nutzer sind eingeladen, zur Entwicklung des Pledari Grond online
	beizutragen. Wer ein Wort oder einen Ausdruck nicht findet, kann über
	das entsprechende Formular (es erscheint durch Anklicken des Buttons
	"Neuer Eintrag") Vorschläge machen. Wer einen Fehler entdeckt, kann
	über ein weiteres Formular (es erscheint durch Anklicken des Buttons
	"Bearbeiten") eine Änderung vorschlagen oder eine Anmerkung versenden.</p>

<p>Diese Website benutzt Google Analytics, einen Webanalysedienst
	der Google Inc. („Google“). Google Analytics verwendet sog. „Cookies“,
	Textdateien, die auf Ihrem Computer gespeichert werden und die eine
	Analyse der Benutzung der Website durch Sie ermöglicht. Die durch den
	Cookie erzeugten Informationen über Ihre Benutzung dieser Website
	(einschliesslich Ihrer IP-Adresse) wird an einen Server von Google in
	den USA übertragen und dort gespeichert. Google wird diese
	Informationen benutzen, um Ihre Nutzung der Website auszuwerten, um
	Reports über die Websiteaktivitäten für die Websitebetreiber
	zusammenzustellen und um weitere mit der Websitenutzung und der
	Internetnutzung verbundene Dienstleistungen zu erbringen. Auch wird
	Google diese Informationen gegebenenfalls an Dritte übertragen, sofern
	dies gesetzlich vorgeschrieben ist oder soweit Dritte diese Daten im
	Auftrag von Google verarbeiten. Google wird in keinem Fall Ihre
	IP-Adresse mit anderen Daten der Google Inc. in Verbindung bringen. Sie
	können die Installation der Cookies durch eine entsprechende
	Einstellung Ihrer Browser Software verhindern; wir weisen Sie jedoch
	darauf hin, dass Sie in diesem Fall gegebenenfalls nicht sämtliche
	Funktionen dieser Website voll umfänglich nutzen können. Durch die
	Nutzung dieser Website erklären Sie sich mit der Bearbeitung der über
	Sie erhobenen Daten durch Google in der zuvor beschriebenen Art und
	Weise und zu dem zuvor benannten Zweck einverstanden.</p>

<p>
	<b> © Lia Rumantscha, CH-7000 Chur</b>
</p>

<p>
	<b>Konzept und Umsetzung </b> <br> Lia Rumantscha, Anna-Alice
	Dazzi und Daniel Telli
</p>

<p>
	<b>Aktuelle Redaktion des Pledari Grond online </b> <br> Daniel
	Telli <br> Marietta Cathomas Manetsch <br> Gabriela
	Holderegger Pajarola <br> Angela Schmed Bass <br> Marina Wyss
</p>

<p>
	<b>Ehemalige Redaktorinnen und ehemalige Redaktoren </b> <br>
	Georges Darms <br> Anna-Alice Dazzi Gross <br> Manfred Gross
	<br> Annetta Zini
</p>

<p>
	<b>Programme</b> <br> Jürgen Rolshoven <br> Claes Neuefeind <br>
	Stephan Schwiebert <br> Mihail Atanassov <br> (Institut für
	Linguistik, Sprachliche Informationsverarbeitung, Universität zu Köln)
</p>

<p>
	<b>Grafisches Design</b> <br> Remo Caminada, www.remocaminada.com
</p>
