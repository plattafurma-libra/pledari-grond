<%@page import="org.springframework.security.openid.OpenIDAttribute"%>
<%@page import="java.util.List"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.openid.OpenIDAuthenticationToken"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%-- Header included here --%>
<jsp:include page="/maalr_modules/misc/htmlhead.jsp" />
<body>
	<div id="top">
		<jsp:include page="/maalr_modules/misc/header.jsp" />
	</div>
	<div id="bottom"><jsp:include page="/maalr_modules/misc/footer.jsp" /></div>
	<div>
	
		<%@ include file="/maalr_modules/misc/language_widget.jsp"%>
		
		<%@ include file="/maalr_modules/misc/login_widget.jsp"%>
		
		<div class="container well">

			<table>
				<col width="70">
				<col width="195">
				<col width="300">
				<tr id="A">
					<td>A</td>
					<td>Austria, austriac</td>
					<td>&Ouml;sterreich, &ouml;sterreichisch</td>
				</tr>
				<tr>
					<td>abrev.</td>
					<td>abreviaziun</td>
					<td>Abk&uuml;rzung</td>
				</tr>
				<tr>
					<td>acc.</td>
					<td>accusativ</td>
					<td>Akkusativ</td>
				</tr>
				<tr>
					<td>accent.</td>
					<td>accentu&agrave;</td>
					<td>betont</td>
				</tr>
				<tr>
					<td>adj.</td>
					<td>adjectiv</td>
					<td>Adjektiv</td>
				</tr>
				<tr>
					<td>adm.</td>
					<td>administraziun</td>
					<td>Verwaltung</td>
				</tr>

				<tr>
					<td>adm.-giur.</td>
					<td>administrativ-giuridic</td>
					<td>verwaltungssprachlich-juristisch</td>
				</tr>
				<tr>
					<td>adv.</td>
					<td>adverb</td>
					<td>Adverb</td>
				</tr>
				<tr>
					<td>aff.</td>
					<td>affectiv</td>
					<td>affektiv</td>

				</tr>
				<tr>
					<td>agr.</td>
					<td>agricultura</td>
					<td>Landwirtschaft</td>
				</tr>
				<tr>

					<td>alpin.</td>
					<td>alpinissem</td>
					<td>Alpinismus, Bergsport</td>
				</tr>
				<tr>
					<td>anat.</td>
					<td>anatomia, corp uman</td>
					<td>Anatomie</td>
				</tr>
				<tr>
					<td>angl.</td>
					<td>anglicissem</td>
					<td>Anglizismus</td>
				</tr>

				<tr>
					<td>ant.</td>
					<td>antiqu&agrave;</td>
					<td>veraltet</td>
				</tr>
				<tr>
					<td>apic.</td>
					<td>apicultura</td>
					<td>Bienenzucht</td>
				</tr>
				<tr>
					<td>arch.</td>
					<td>architectura</td>
					<td>Architektur</td>

				</tr>
				<tr>
					<td>archeol.</td>
					<td>archeologia</td>
					<td>Arch&auml;ologie</td>
				</tr>
				<tr>

					<td>art.</td>
					<td>artitgel</td>
					<td>Artikel</td>
				</tr>
				<tr>
					<td>artis.</td>
					<td>artisanat</td>

					<td>Kunsthandwerk</td>
				</tr>
				<tr>
					<td>assic.</td>
					<td>assicuranzas</td>
					<td>Versicherungswesen</td>
				</tr>

				<tr>
					<td>astrol.</td>
					<td>astrologia</td>
					<td>Astrologie</td>
				</tr>
				<tr>
					<td>astron.</td>

					<td>astronomia</td>
					<td>Astronomie</td>
				</tr>
				<tr>
					<td>attr.</td>
					<td>attributiv</td>
					<td>attributiv</td>

				</tr>
				<tr>
					<td>aut.</td>
					<td>automobilissem</td>
					<td>Automobil, Kraftfahrzeugwesen</td>
				</tr>
				<tr>

					<td>av.</td>
					<td>aviatica</td>
					<td>Luftfahrt</td>
				</tr>
				<tr id="B">
					<td>b.</td>
					<td></td>

					<td>bei, beim</td>
				</tr>
				<tr>
					<td>banc.</td>
					<td>bancas</td>
					<td>Bankwesen</td>
				</tr>

				<tr>
					<td>bibl.</td>
					<td>biblic</td>
					<td>biblisch</td>
				</tr>
				<tr>
					<td>biblio.</td>

					<td>bibliotecas</td>
					<td>Bibliothekswesen</td>
				</tr>
				<tr>
					<td>biol.</td>
					<td>biologia</td>
					<td>Biologie</td>

				</tr>
				<tr>
					<td>bot.</td>
					<td>botanica</td>
					<td>Botanik</td>
				</tr>
				<tr id="C">

					<td>C.</td>
					<td>Grischun Central</td>
					<td>Mittelb&uuml;nden</td>
				</tr>
				<tr>
					<td>cat.</td>

					<td>catolic</td>
					<td>katholisch, katholische Religion</td>
				</tr>
				<tr>
					<td>cf.</td>
					<td>confer</td>
					<td>vergleiche</td>

				</tr>
				<tr>
					<td>CH</td>
					<td>Svizra</td>
					<td>Schweiz</td>
				</tr>
				<tr>

					<td>chatsch.</td>
					<td>chatscha</td>
					<td>Jagd, J&auml;gersprache</td>
				</tr>
				<tr>
					<td>chem.</td>

					<td>chemia</td>
					<td>Chemie</td>
				</tr>
				<tr>
					<td>cin.</td>
					<td>cinematografia</td>
					<td>Film, Kino</td>

				</tr>
				<tr>
					<td>cj.</td>
					<td>conjucziun</td>
					<td>Konjuktion</td>
				</tr>
				<tr>

					<td>coll.</td>
					<td>collectiv</td>
					<td>kollektiv</td>
				</tr>
				<tr>
					<td>comm.</td>
					<td>commerzi</td>

					<td>Handel</td>
				</tr>
				<tr>
					<td>conj.</td>
					<td>conjunctiv</td>
					<td>Konjuktiv</td>
				</tr>

				<tr>
					<td>conjug.</td>
					<td>conjugaziun</td>
					<td>Konjugation</td>
				</tr>
				<tr>
					<td>cons.</td>

					<td>consonant</td>
					<td>Konsonant</td>
				</tr>
				<tr>
					<td>constr.</td>
					<td>construcziuns</td>
					<td>Bauwesen</td>

				</tr>
				<tr>
					<td>cump.</td>
					<td>cumparativ</td>
					<td>Komparativ</td>
				</tr>
				<tr>

					<td>cumpos.</td>
					<td>cumposiziun</td>
					<td>Zusammensetzung</td>
				</tr>
				<tr>
					<td>cund.</td>
					<td>cundiziunal</td>

					<td>Konditional</td>
				</tr>
				<tr>
					<td>d.</td>
					<td></td>
					<td>der, die, das, dem, den, der</td>
				</tr>

				<tr id="D">
					<td>D</td>
					<td>Germania, tudestg</td>
					<td>Deutschland, deutsch</td>
				</tr>
				<tr>
					<td>dat.</td>

					<td>dativ</td>
					<td>Dativ</td>
				</tr>
				<tr>
					<td>def.</td>
					<td>definitiv</td>
					<td>bestimmt</td>

				</tr>
				<tr>
					<td>dem.</td>
					<td>demonstrativ</td>
					<td>Demonstrativ</td>
				</tr>
				<tr>

					<td>dent.</td>
					<td>dentistica, medischina dentala</td>
					<td>Zahnheilkunde, Zahnmedizin</td>
				</tr>
				<tr>
					<td>dial.</td>
					<td>dialectical</td>

					<td>mundartlich</td>
				</tr>
				<tr>
					<td>dim.</td>
					<td>diminuitiv</td>
					<td>Verkleinerung, Diminutivform</td>
				</tr>

				<tr>
					<td>dipl.</td>

					<td>diplomazia</td>
					<td>Diplomatie</td>
				</tr>
				<tr id="E">
					<td>E.</td>

					<td>Engiadina</td>
					<td>Engadin</td>
				</tr>
				<tr>
					<td>e.</td>
					<td></td>
					<td>ein, eine, einen, einem, einer,eines</td>

				</tr>
				<tr>
					<td>econ.</td>
					<td>economia</td>
					<td>Wirtschaft</td>
				</tr>
				<tr>

					<td>edit.</td>
					<td>ediziuns</td>
					<td>Verlagswesen</td>
				</tr>
				<tr>
					<td>eidg.</td>
					<td></td>

					<td>eidgen&ouml;ssisch</td>
				</tr>
				<tr>
					<td>electr.</td>
					<td>electricitad, electronica</td>
					<td>Elektrizit&auml;tswesen, Elektronik</td>

				</tr>
				<tr>
					<td>engl.</td>
					<td>englais</td>
					<td>englisch</td>
				</tr>
				<tr>

					<td>enol.</td>
					<td>enologia</td>
					<td>Weinkunde</td>
				</tr>
				<tr>
					<td>entom.</td>
					<td>entomologia</td>

					<td>Entomologie, Insektenkunde</td>
				</tr>
				<tr>
					<td>equit.</td>
					<td>equitaziun</td>
					<td>Pferdesport</td>
				</tr>

				<tr>
					<td>erald.</td>
					<td>eraldica</td>
					<td>Heraldik, Wappenkunde</td>
				</tr>
				<tr>
					<td>etc.</td>

					<td>et cetera</td>
					<td>et cetera, und so weiter</td>
				</tr>
				<tr>
					<td>etw.</td>
					<td>insatge</td>
					<td>etwas</td>

				</tr>
				<tr>
					<td>euf.</td>
					<td>eufemistic</td>
					<td>euphemistisch, besch&ouml;nigend</td>
				</tr>
				<tr>

					<td>ex.</td>
					<td>exempel</td>
					<td>Beispiel</td>
				</tr>
				<tr id="F">
					<td>f</td>
					<td>feminin</td>

					<td>Femininum</td>
				</tr>
				<tr>
					<td>fam.</td>
					<td>famigliar</td>
					<td>famili&auml;r</td>

				</tr>
				<tr>
					<td>farm.</td>
					<td>farmazia, apoteca</td>
					<td>Pharmazie</td>
				</tr>
				<tr>

					<td>fig.</td>
					<td>figurativ</td>
					<td>&uuml;bertragen, figurativ</td>
				</tr>
				<tr>
					<td>fil.</td>
					<td>filosofia</td>

					<td>Philosophie</td>
				</tr>
				<tr>
					<td>filat.</td>
					<td>filatelia</td>
					<td>Philatelie</td>
				</tr>

				<tr>
					<td>fin.</td>
					<td>finanzas</td>
					<td>Finanzwesen</td>
				</tr>
				<tr>
					<td>fis.</td>

					<td>fisica</td>
					<td>Physik</td>
				</tr>
				<tr>
					<td>fisiol.</td>
					<td>fisiologia</td>
					<td>Physiologie</td>

				</tr>
				<tr>
					<td>fon.</td>
					<td>fonetica, fonologia</td>
					<td>Phonetik, Phonologie</td>
				</tr>
				<tr>

					<td>fot.</td>
					<td>fotografia</td>
					<td>Fotographie</td>
				</tr>
				<tr>
					<td>franz.</td>
					<td>franzos</td>

					<td>franz&ouml;sisch</td>
				</tr>
				<tr id="G">
					<td>gastr.</td>
					<td>gastronomia</td>
					<td>Gastronomie, Kochkunst</td>

				</tr>
				<tr>
					<td>gen.</td>
					<td>genitiv</td>
					<td>Genitiv</td>
				</tr>
				<tr>

					<td>gener.</td>
					<td>general(main)</td>
					<td>allgemein, gew&ouml;hnlich</td>
				</tr>
				<tr>
					<td>geogr.</td>

					<td>geografia</td>
					<td>Geografie</td>
				</tr>
				<tr>
					<td>geol.</td>
					<td>geologia</td>
					<td>Geologie</td>

				</tr>
				<tr>
					<td>geom.</td>
					<td>geometria</td>
					<td>Geometrie</td>
				</tr>
				<tr>

					<td>germ.</td>
					<td>germanissem</td>
					<td>Germanismus</td>
				</tr>
				<tr>
					<td>giur.</td>
					<td>giuridic, giurispudenza</td>

					<td>juristisch; Jurisprudenz, Recht</td>
				</tr>
				<tr>
					<td>giuv.</td>
					<td>giuvenils, lingua da...</td>
					<td>Jugendsprache</td>
				</tr>

				<tr>
					<td>GR</td>
					<td>Grischun</td>
					<td>Graub&uuml;nden</td>
				</tr>
				<tr>
					<td>graf.</td>

					<td>grafica</td>
					<td>Graphik</td>
				</tr>
				<tr>
					<td>gramm.</td>
					<td>grammatica</td>
					<td>Grammatik</td>

				</tr>
				<tr>
					<td>grd.</td>
					<td>gerundi</td>
					<td>Gerundium</td>
				</tr>
				<tr id="I">

					<td>idr.</td>
					<td>idraulica</td>
					<td>Hydraulik</td>
				</tr>
				<tr>
					<td>i-e</td>
					<td>insatge</td>

					<td>etwas</td>
				</tr>
				<tr>
					<td>i-i</td>
					<td>insatgi</td>
					<td>jemand</td>
				</tr>

				<tr>
					<td>impers.</td>
					<td>impersunal</td>
					<td>unpers&ouml;nlich</td>
				</tr>
				<tr>
					<td>indef.</td>

					<td>indefinit</td>
					<td>unbestimmt</td>
				</tr>
				<tr>
					<td>inf.</td>
					<td>infinitiv</td>
					<td>Infinitiv, Grundform</td>

				</tr>
				<tr>
					<td>info.</td>
					<td>informatica</td>
					<td>Informatik, Computerwesen</td>
				</tr>
				<tr>

					<td>int.</td>
					<td>intransitiv</td>
					<td>intransitiv</td>
				</tr>
				<tr>
					<td>interj.</td>
					<td>interjecziun</td>

					<td>Interjektion, Ausruf</td>
				</tr>
				<tr>
					<td>internaz.</td>
					<td>internaziunal</td>
					<td>international</td>
				</tr>

				<tr>
					<td>interr.</td>
					<td>interrogativ</td>
					<td>Interrogativ, Frageform</td>
				</tr>
				<tr>
					<td>inv.</td>

					<td>invariabel</td>
					<td>unver&auml;nderlich</td>
				</tr>
				<tr>
					<td>ipf.</td>
					<td>imperfect</td>

					<td>Imperfekt, Pr&auml;teritum</td>
				</tr>
				<tr>
					<td>ipv.</td>
					<td>imperativ</td>
					<td>Imperativ, Befehlsform</td>

				</tr>
				<tr>
					<td>iron.</td>
					<td>ironic</td>
					<td>ironisch</td>
				</tr>
				<tr>

					<td>irr.</td>
					<td>irregular</td>
					<td>unregelm&auml;ssig</td>
				</tr>
				<tr>
					<td>ist.</td>

					<td>istorgia</td>
					<td>Geschichte</td>
				</tr>
				<tr id="J">
					<td>jm.</td>
					<td></td>
					<td>jemandem</td>

				</tr>
				<tr>
					<td>jn.</td>
					<td></td>
					<td>jemand(en)</td>
				</tr>
				<tr>
					<td>js.</td>

					<td></td>
					<td>jemandes</td>
				</tr>
				<tr id="L">
					<td>lat.</td>
					<td>latin</td>
					<td>lateinisch</td>

				</tr>
				<tr>
					<td>lim.</td>
					<td>limit&agrave;</td>
					<td>beschr&auml;nkt, eingeschr&auml;nkt</td>
				</tr>

				<tr>
					<td>ling.</td>
					<td>linguistica</td>
					<td>Sprachwissenschaft</td>
				</tr>
				<tr>
					<td>litt.</td>

					<td>litterar; litteratura</td>
					<td>literarisch, Literatur</td>
				</tr>
				<tr>
					<td>loc.</td>
					<td>local</td>
					<td>&ouml;rtlich</td>

				</tr>
				<tr id="M">
					<td>m</td>
					<td>masculin</td>
					<td>Maskulinum</td>
				</tr>
				<tr>

					<td>mat.</td>
					<td>matematica</td>
					<td>Mathematik</td>
				</tr>
				<tr>
					<td>med.</td>
					<td>medischina</td>

					<td>Medizin</td>
				</tr>
				<tr>
					<td>mes.</td>
					<td>mesiraziun</td>
					<td>Vermessungswesen, Messwesen</td>
				</tr>

				<tr>
					<td>met.</td>
					<td>metallurgia</td>
					<td>Metallkunde, Metallurgie</td>
				</tr>
				<tr>
					<td>meteor.</td>

					<td>meteorologia</td>
					<td>Meteorologie, Wetterkunde</td>
				</tr>
				<tr>
					<td>metr.</td>
					<td>metrica</td>
					<td>Verslehre, Metrik</td>

				</tr>
				<tr>
					<td>mil.</td>
					<td>militar</td>
					<td>milit&auml;risch, Milit&auml;rwesen</td>
				</tr>

				<tr>
					<td>min.</td>
					<td>minieras</td>
					<td>Bergbau</td>
				</tr>
				<tr>
					<td>mit.</td>

					<td>mitologia</td>
					<td>Mythologie</td>
				</tr>
				<tr>
					<td>mod</td>
					<td>modal</td>
					<td>modal</td>

				</tr>
				<tr>
					<td>mus.</td>
					<td>musica</td>
					<td>Musik</td>
				</tr>
				<tr id="N">

					<td>n</td>
					<td>neutrum</td>
					<td>Neutrum</td>
				</tr>
				<tr>
					<td>n.l</td>
					<td>num local</td>

					<td>Ortsname</td>
				</tr>
				<tr>
					<td>n.p</td>
					<td>num propri</td>
					<td>Eigenname, Personenname</td>
				</tr>

				<tr>
					<td>nav.</td>
					<td>navigaziun</td>
					<td>Schifffahrt, Navigation</td>
				</tr>
				<tr>
					<td>neg.</td>

					<td>negativ; negaziun</td>
					<td>negativ; Verneinung</td>
				</tr>
				<tr>
					<td>nom.</td>
					<td>nominativ</td>
					<td>Nominativ</td>

				</tr>
				<tr>
					<td>num.</td>
					<td>numeral</td>
					<td>Zahlwort</td>
				</tr>
				<tr>

					<td>numis.</td>
					<td>numismatica</td>
					<td>M&uuml;nzkunde, Numismatik</td>
				</tr>
				<tr>
					<td>nunacc.</td>

					<td>nunaccentu&agrave;</td>
					<td>unbetont</td>
				</tr>
				<tr id="O">
					<td>opt.</td>
					<td>optica</td>
					<td>Optik</td>

				</tr>
				<tr>
					<td>ornit.</td>
					<td>ornitologia</td>
					<td>Vogelkunde, Ornithologie</td>
				</tr>
				<tr>

					<td>ortic.</td>
					<td>orticultura</td>
					<td>Gartenbau</td>
				</tr>
				<tr>
					<td>ortop.</td>
					<td>ortopedia</td>

					<td>Orthop&auml;die</td>
				</tr>
				<tr id="P">
					<td>part</td>
					<td>particla</td>
					<td>Partikel</td>

				</tr>
				<tr>
					<td>pass.</td>
					<td>passiv</td>
					<td>Passiv, passivisch</td>
				</tr>
				<tr>

					<td>peg.</td>
					<td>pegiurativ</td>
					<td>pejorativ, absch&auml;tzig</td>
				</tr>
				<tr>
					<td>pers.</td>

					<td>persunal</td>
					<td>pers&ouml;nlich</td>
				</tr>
				<tr>
					<td>pft.</td>
					<td>perfect</td>

					<td>Perfekt</td>
				</tr>
				<tr>
					<td>pisc.</td>
					<td>piscicultura</td>
					<td>Fischzucht</td>
				</tr>

				<tr>
					<td>pl.</td>
					<td>plural</td>
					<td>Plural</td>
				</tr>
				<tr>
					<td>plqpft.</td>

					<td>plusquamperfect</td>
					<td>Plusquamperfekt</td>
				</tr>
				<tr>
					<td>poet.</td>
					<td>poetic; poesia</td>
					<td>poetisch; Poesie, Dichtung</td>

				</tr>
				<tr>
					<td>polit.</td>
					<td>politica</td>
					<td>Politik</td>
				</tr>
				<tr>

					<td>pop.</td>
					<td>popular</td>
					<td>umgangssprachlich, gespr. Sprache, volkst&uuml;mlich</td>
				</tr>
				<tr>
					<td>pos.</td>

					<td>positiv</td>
					<td>positiv</td>
				</tr>
				<tr>
					<td>poss.</td>
					<td>possessiv</td>
					<td>Possessiv</td>

				</tr>
				<tr>
					<td>post.</td>
					<td>posta</td>
					<td>Postwesen</td>
				</tr>
				<tr>

					<td>pp.</td>
					<td>particip perfect</td>
					<td>Partizip Perfect</td>
				</tr>
				<tr>
					<td>pred.</td>
					<td>predicativ</td>

					<td>Pr&auml;dikativ</td>
				</tr>
				<tr>
					<td>pref.</td>
					<td>prefix</td>
					<td>Pr&auml;fix, Vorsilbe</td>

				</tr>
				<tr>
					<td>prep.</td>
					<td>preposiziun</td>
					<td>Pr&auml;position</td>
				</tr>
				<tr>

					<td>prn.</td>
					<td>pronom</td>
					<td>Pronomen, F&uuml;rwort</td>
				</tr>
				<tr>
					<td>prot.</td>

					<td>protestant</td>
					<td>protestantisch, prot. Religion</td>
				</tr>
				<tr>
					<td>prov.</td>
					<td>proverbi</td>
					<td>Sprichwort</td>

				</tr>
				<tr>
					<td>prs.</td>
					<td>preschent</td>
					<td>Pr&auml;sens, Gegenwart</td>
				</tr>
				<tr>

					<td>psic.</td>
					<td>psicologia</td>
					<td>Psychologie</td>
				</tr>
				<tr>
					<td>ptc.</td>
					<td>particip (perfect)</td>

					<td>Partizipium, Partizip Perfekt</td>
				</tr>
				<tr id="R">
					<td>rad.</td>
					<td>radio</td>
					<td>Radio, Rundfunk</td>
				</tr>

				<tr>
					<td>refl.</td>
					<td>reflexiv</td>
					<td>Reflexiv</td>
				</tr>
				<tr>
					<td>reg.</td>

					<td>regiunal</td>
					<td>regional, landschaftlich</td>
				</tr>
				<tr>
					<td>rel.</td>
					<td>relativ</td>
					<td>Relativ</td>

				</tr>
				<tr>
					<td>relig.</td>
					<td>religiun</td>
					<td>Religion</td>
				</tr>
				<tr>

					<td>resp.</td>
					<td>respectivamain</td>
					<td>beziehungsweise (bzw.)</td>
				</tr>
				<tr>
					<td>RG</td>
					<td>rumantsch grischun</td>

					<td>rumantsch grischun</td>
				</tr>
				<tr id="S">
					<td>s.</td>
					<td></td>
					<td>sich</td>
				</tr>

				<tr>
					<td>S.</td>
					<td>Surselva</td>
					<td>B&uuml;ndner Oberland, Vorderrheinregion</td>
				</tr>
				<tr>
					<td>sc.</td>

					<td>scienza, scientific</td>
					<td>Wissenschaft, wissenschaftlich</td>
				</tr>
				<tr>
					<td>schurn.</td>
					<td>schurnalissem</td>
					<td>Journalismus</td>

				</tr>
				<tr>
					<td>scol.</td>
					<td>scola</td>
					<td>Schulwesen</td>
				</tr>
				<tr>

					<td>selv.</td>
					<td>selvicultura</td>
					<td>Forstwesen, Forstwirtschaft</td>
				</tr>
				<tr>
					<td>sex.</td>
					<td>sexualitad</td>

					<td>Sexualit&auml;t</td>
				</tr>
				<tr>
					<td>sg.</td>
					<td>singular</td>
					<td>Singular</td>

				</tr>
				<tr>
					<td>Sm.</td>
					<td>Surmeir</td>
					<td>Oberhalbstein, Albulatal</td>
				</tr>
				<tr>

					<td>socio.</td>
					<td>sociologia</td>
					<td>Soziologie, Sozialwissenschaft</td>
				</tr>
				<tr>
					<td>sp.</td>
					<td>sport</td>

					<td>Sport</td>
				</tr>
				<tr>
					<td>spagn.</td>
					<td>spagnol</td>
					<td>spanisch</td>
				</tr>

				<tr>
					<td>spez.</td>
					<td>spezialmain</td>
					<td>besonders</td>
				</tr>
				<tr>
					<td>St.</td>

					<td>Sutselva</td>
					<td>Hinterrheinregion</td>
				</tr>
				<tr>
					<td>strad.</td>
					<td>stradal, construcziuns da vias</td>
					<td>Strassenbau und Verkehrswesen</td>

				</tr>
				<tr>
					<td>subst.</td>
					<td>substantiv</td>
					<td>Substantiv</td>
				</tr>
				<tr>

					<td>sviz.</td>
					<td>svizzer, Svizra</td>
					<td>schweizerisch, Schweiz</td>
				</tr>
				<tr id="T">
					<td>tal.</td>
					<td>talian</td>

					<td>italienisch</td>
				</tr>
				<tr>
					<td>teat.</td>
					<td>teater</td>
					<td>Theater</td>
				</tr>

				<tr>
					<td>tecn.</td>
					<td>tecnica</td>
					<td>Technik</td>
				</tr>
				<tr>
					<td>tel.</td>

					<td>telefon, telecommunicaziun</td>
					<td>Telefon, Telekommunikation</td>
				</tr>
				<tr>
					<td>temp.</td>
					<td>temporal</td>
					<td>zeitlich</td>

				</tr>
				<tr>
					<td>textil.</td>
					<td>textilias</td>
					<td>Textilindustrie</td>
				</tr>
				<tr>

					<td>tipo.</td>
					<td>tipografia</td>
					<td>Buchdruck, Typografie</td>
				</tr>
				<tr>
					<td>topo.</td>
					<td>topografia</td>

					<td>Topographie</td>
				</tr>
				<tr>
					<td>tr.</td>
					<td>transitiv</td>
					<td>transitiv</td>
				</tr>

				<tr>
					<td>traff.</td>
					<td>traffic</td>
					<td>Verkehrswesen</td>
				</tr>
				<tr>
					<td>tv.</td>

					<td>televisiun</td>
					<td>Fernsehen</td>
				</tr>
				<tr id="U">
					<td>uff.</td>
					<td>uffizial</td>
					<td>amtlich, f&ouml;rmlich</td>

				</tr>
				<tr>
					<td>umor.</td>
					<td>umoristic</td>
					<td>humoristisch, humorvoll, scherzhaft</td>
				</tr>
				<tr>

					<td>univ.</td>
					<td>universitad</td>
					<td>Universit&auml;t, Hochschule</td>
				</tr>
				<tr id="V">
					<td>v.</td>

					<td></td>
					<td>von</td>
				</tr>
				<tr>
					<td>vb.</td>
					<td>verb</td>
					<td>Verb</td>

				</tr>
				<tr>
					<td>vb. irr.</td>
					<td>verb irregular</td>
					<td>unregelm&auml;ssiges Verb</td>
				</tr>
				<tr>

					<td>vb. reg.</td>
					<td>verb regular</td>
					<td>regelm&auml;ssiges Verb</td>
				</tr>
				<tr>
					<td>voc.</td>

					<td>vocal</td>
					<td>Vokal</td>
				</tr>
				<tr>
					<td>vulg.</td>
					<td>vulgar</td>
					<td>vulg&auml;r, derb</td>

				</tr>
				<tr id="Z">
					<td>zool.</td>
					<td>zoologia</td>
					<td>Zoologie</td>
				</tr>
			</table>

		</div>

	</div>
</body>
</html>