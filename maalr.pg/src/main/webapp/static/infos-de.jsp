<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.Locale" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page
	import="de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<h1>Allgemeine Informationen</h1>

<p>
	<strong>Diese Online-Version des Pledari sutsilvan basiert auf dem Pledari sutsilvan-tudestg/Wörterbuch Deutsch-Sutsilvan, redigiert von Wolfang Eichenhofer, herausgegeben im Jahre 2002 von Lehrmittel Graubünden.</strong>
</p>

<p>
	<% NumberFormat nf = NumberFormat.getNumberInstance(locale); %>
	<fmt:message key="maalr.index.entry_count" var="numberOfEntries">
		<fmt:param><%=nf.format(DictionaryStatistics.getStatistics().entryCounter)%></fmt:param>
	</fmt:message>
	${numberOfEntries}.
</p>

<p>Der Wortschatz einer Sprache hat keine Grenzen. So lange die Sprache lebt, wächst und entwickelt sich dieser weiter und passt sich organisch den Kommunikationsbedürfnissen der Sprachgemeinschaft an. Alle Nutzerinnen und Nutzer sind eingeladen, zur Entwicklung des Pledari sutsilvan online beizutragen. Wer ein Wort oder einen Ausdruck nicht findet, kann über das entsprechende Formular (es erscheint durch Anklicken des Buttons "Neuer Eintrag") Vorschläge machen. Wer einen Fehler entdeckt, kann über ein weiteres Formular (es erscheint durch Anklicken des Buttons "Bearbeiten") eine Änderung vorschlagen oder eine Anmerkung versenden.</p>

<p>Diese Website benutzt Google Analytics, einen Webanalysedienst der Google Inc. („Google“). Google Analytics verwendet sog. „Cookies“, Textdateien, die auf Ihrem Computer gespeichert werden und die eine Analyse der Benutzung der Website durch Sie ermöglicht. Die durch den Cookie erzeugten Informationen über Ihre Benutzung dieser Website (einschliesslich Ihrer IP-Adresse) wird an einen Server von Google in den USA übertragen und dort gespeichert. Google wird diese Informationen benutzen, um Ihre Nutzung der Website auszuwerten, um Reports über die Websiteaktivitäten für die Websitebetreiber zusammenzustellen und um weitere mit der Websitenutzung und der Internetnutzung verbundene Dienstleistungen zu erbringen. Auch wird Google diese Informationen gegebenenfalls an Dritte übertragen, sofern dies gesetzlich vorgeschrieben ist oder soweit Dritte diese Daten im Auftrag von Google verarbeiten. Google wird in keinem Fall Ihre IP-Adresse mit anderen Daten der Google Inc. in Verbindung bringen. Sie können die Installation der Cookies durch eine entsprechende Einstellung Ihrer Browser Software verhindern; wir weisen Sie jedoch darauf hin, dass Sie in diesem Fall gegebenenfalls nicht sämtliche Funktionen dieser Website voll umfänglich nutzen können. Durch die Nutzung dieser Website erklären Sie sich mit der Bearbeitung der über Sie erhobenen Daten durch Google in der zuvor beschriebenen Art und Weise und zu dem zuvor benannten Zweck einverstanden.</p>

<p>
	<b> © Lia Rumantscha, CH-7000 Chur</b>
</p>

<p>
	<b>Konzept und Umsetzung </b>
	<br>Lia Rumantscha, Daniel Telli
</p>

<p>
	<b>Aktuelle Redaktion</b>
	<br>
	Johann Clopath
</p>

<p>
	<b>Programme</b>
	<br>Jürgen Rolshoven
	<br>Claes Neuefeind
	<br>Stephan Schwiebert
	<br>Mihail Atanassov
	<br>(Institut für Linguistik, Sprachliche Informationsverarbeitung, Universität zu Köln)
</p>

<p>
	<b>Grafisches Design</b>
	<br> Remo Caminada, www.remocaminada.com
</p>
