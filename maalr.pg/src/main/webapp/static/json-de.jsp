<%@page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ page import="java.util.Locale"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page
	import="de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h1>Die Suche mit Pledari Grond auf einer Webseite einbetten</h1>

<h2>iFrame</h2>

<p>Die einfachste Möglichkeit, auf Ihrer Webseite die Suche im Pledari Grond zu ermöglichen, besteht
in der Einbettung eines iFrames. Fügen Sie folgenden HTML-Code an der Stelle, an der Sie die Suchfunktion
einbetten möchten, ein.

<textarea rows="4" style="width:100%;">

&lt;iframe src=&quot;<%=Configuration.getInstance().getServerInetAddress()%>/iframe.html&quot; seamless=&quot;true&quot; width=&quot;350px&quot;&gt width=&quot;350px&quot;&gt;
     &lt;/iframe&gt;
</textarea>

Auf Ihrer Seite wird anschließend folgendes Widget dargestellt, dessen Höhe und Breite Sie durch die Parameter
'height' und 'width' beeinflussen können.

<div class="well" style="width:350px;">
 <iframe src="<%=Configuration.getInstance().getServerInetAddress()%>/iframe.html" seamless="true" width="350px" height="350px">
     </iframe>
</div>

     </p>
     

<h2>JavaScript</h2>
<p>
Sollten Sie das Widget an das Layout Ihrer Seite anpassen wollen, kopieren Sie folgenden
Inhalt an die gewünschte Stelle im HTML-Code der Webseite. 
<textarea rows="8" style="width:100%;">

 &lt;div id=&quot;maalr_query_div&quot; class=&quot;maalr_query_div&quot;
    		data-source=&quot;<%=Configuration.getInstance().getServerInetAddress()%>&quot;
    		data-locale=&quot;st&quot;&gt;
    		data-button=&quot;Suchen&quot;&gt;
    		data-placeholder=&quot;Bitte Suchbegriff eingeben...&quot;&gt;
    		&lt;script type=&quot;text/javascript&quot; src=&quot;<%=Configuration.getInstance().getServerInetAddress()%>/assets/js/embed_pledari.js&quot;&gt;&lt;/script&gt;
 &lt;/div&gt;
 
 </textarea>
</p>
<p>
Anschließend erscheint an der entsprechenden Stelle folgendes Widget:

<div class="well" style="width:350px;">
	<div id="maalr_query_div" class="maalr_query_div"
	    		data-source="<%=Configuration.getInstance().getServerInetAddress()%>"
	    		data-locale="st"
	    		data-button="Suchen"
	    		data-placeholder="Bitte Suchbegriff eingeben...">
	    		<script type="text/javascript" src="<%=Configuration.getInstance().getServerInetAddress()%>/assets/js/embed_pledari.js"></script>
	 </div>
 </div>
</p>

<p>
Sie können das Formular an das Layout Ihrer Seite anpassen, durch Hinzufügen bzw. Modifikation
einzelner Parameter können Sie die Funktionalität ebenso wie das Layout des Widgets an Ihre
Anforderungen anpassen.
<table>
		<col style="width:150px">
        <col style="width:100px">
        <col style="width:150px">
	<thead>
		<tr>
			<td><h4>Parameter</h4></td>
			<td><h4>Optional</h4></td>
			<td><h4>Standard-Wert</h4></td>
			<td><h4>Beschreibung</h4></td>
		</tr>
	</thead>
	<tr>
		<td>data-locale</td>
		<td>nein</td>
		<td>-</td>
		<td>Gibt an, in welcher Sprache das Formular dargestellt werden soll. Entweder "de" oder "st".</td>
	</tr>
	<tr>
		<td>data-button</td>
		<td>ja</td>
		<td>-</td>
		<td>Die Beschriftung des Buttons hinter dem Suchfeld. Wird kein Wert angegeben, wird der Button nicht dargestellt.</td>
	</tr>
	<tr>
		<td>data-placeholder</td>
		<td>ja</td>
		<td>-</td>
		<td>Ein optionaler Platzhalter-Text im Suchfeld.</td>
	</tr>
	<tr>
		<td>data-autoquery</td>
		<td>ja</td>
		<td>true</td>
		<td>Gibt an, ob während der Eingabe Suchanfragen gestellt werden oder nicht. Entweder 'true' oder 'false'.</td>
	</tr>
	<tr>
		<td>data-pagesize</td>
		<td>ja</td>
		<td>5</td>
		<td>Die Anzahl der dargestellten Suchergebnisse. Der Wert muss zwischen 5 und 20 liegen.</td>
	</tr>
	<tr>
		<td>data-embedcss</td>
		<td>ja</td>
		<td>false</td>
		<td>Falls das Design des Widgets an die jeweilige Webseite angepasst werden soll, kann dieser Parameter auf 'false' gesetzt werden.
		In dem Fall müssen eigene CSS-Regeln für die Elemente des Widgets definiert werden - das entsprechende Stylesheet finden
		Sie <a href="/assets/style/maalr_embedded.css">hier</a>.
		</td>
	</tr>
</table>
</p>
<p>

</p>

