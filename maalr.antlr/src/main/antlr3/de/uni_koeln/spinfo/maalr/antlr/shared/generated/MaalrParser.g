parser grammar MaalrParser;

options {
  language = Java;
  tokenVocab = MaalrLexer;
}

@header{
	package de.uni_koeln.spinfo.maalr.antlr.shared.generated;
	
	import java.util.Map;
	import java.util.HashMap;
	import java.util.List;
	import java.util.ArrayList;
}

@members {

  String flag;
  
	protected void mismatch(IntStream input, int ttype, BitSet follow)
	    throws RecognitionException
	{
	    throw new MismatchedTokenException(ttype, input);
	}
	public Object recoverFromMismatchedSet(IntStream input,
	                                     RecognitionException e,
	                                     BitSet follow)
	    throws RecognitionException
	{
	    throw e;
	}
	
	private String translateTokenType(int tokenNumber){
	  Map<Integer, String> tokenMap = new HashMap<Integer, String>();
	  	  
	  for(int i = 0; i<tokenNames.length; i++){
	    tokenMap.put(i, tokenNames[i]);
	  }
	  
	  return tokenMap.get(tokenNumber);
	}
}

// Alter code generation so catch-clauses get replace with
// this action.
@rulecatch {
	catch (RecognitionException e1) {
	    throw e1;
	}
}

entry returns [Map<String,String> data]
@init{
  $data = new HashMap<String,String>();
  flag = "D";
}
:
	lemma=phrase 
	{$data.put(flag+"Stichwort",$lemma.text);}
	
	(lgrammatts=grammatts 
	{$data.putAll($lgrammatts.map);}
	)?
	
	(lsemantic=semantics
	{$data.put(flag+"Semantik",$lsemantic.text);}
	)?
	
	(lsubsemantic=subsemantics
	{$data.put(flag+"Subsemantik",$lsubsemantic.text);}
	)?
	
	SEP {flag = "R";} 
	
	translat=phrase 
	{$data.put(flag+"Stichwort",$translat.text);}
	
	(tgrammatt=grammatts 
	{$data.putAll($tgrammatt.map);}
	)?
	
	(tsemantic=semantics
	{$data.put(flag+"Semantik",$tsemantic.text);}
	)?
  
	 (tsubsemantic=subsemantics
	 {$data.put(flag+"Subsemantik",$tsubsemantic.text);}
	 )?
;

//phrase:
	//text+
//;

phrase 
:
    (options{ greedy = false; }:.)+
;

grammatts returns [Map map]:
	OPEN_BRACKET a=grammlist {$map = $a.atts;} CLOSE_BRACKET
;

semantics returns [String text]:
  LEFT_CURLY s=phrase RIGHT_CURLY
  
  {$text = $s.text;}
;

subsemantics returns [String text]:
  D_LEFT_CURLY s=phrase D_RIGHT_CURLY
  
  {$text = $s.text;}
;

grammlist returns [Map atts]
@init{
  $atts = new HashMap<String,String>();
}
:
	g1=grammatt {$atts.put(flag+$g1.type,$g1.text);}
	(COMMA g2=grammatt {$atts.put(flag+$g2.type,$g2.text);})*
;

text:
	TEXT
	| OPEN_BRACKET textlist CLOSE_BRACKET
	| SEP_LEFT textlist SEP_RIGHT
;

textlist:
	text+ (COMMA text+)*
;

//TODO: Fehlerbehandlung, wenn Genus doppelt vorkommt?
grammatt returns [String type, String text]:
	(	g=GENUS 
	| 	g=GRAMMATT)
	{
		String t = translateTokenType($g.type);
		if(t.equals("GENUS")) $type = "Genus";
		else if(t.equals("GRAMMATT")) $type = "Grammatik";
		else $type = "";
		
		$text = $g.text;
	}	
;