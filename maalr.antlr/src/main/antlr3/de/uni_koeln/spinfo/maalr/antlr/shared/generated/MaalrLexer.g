lexer grammar MaalrLexer;

options {
  language = Java;
}

@header{
	package de.uni_koeln.spinfo.maalr.antlr.shared.generated;
}

SEP_LEFT: '<';
SEP_RIGHT: '>';
SEP: SEP_LEFT SEP_RIGHT;

COMMA: ',';
OPEN_BRACKET: '[';
CLOSE_BRACKET: ']';

LEFT_CURLY: '{';
RIGHT_CURLY: '}';

D_LEFT_CURLY: '{{';
D_RIGHT_CURLY: '}}';

GENUS: 'f'|'m'|'f.pl'|'m.pl'|'m(f)';
GRAMMATT: 'tr'|'adj'|'refl'|'n.l'|'adv'|'tr/int'|'int'|'abs/tr'|'tr/int/refl'|'interj'|'cj'|'(refl) int';

fragment LINEFEED: '\r';
fragment CARRIAGERETURN: '\n';
fragment TAB: '\t'; 
fragment SPACE: ' ';

NEWLINE: LINEFEED? CARRIAGERETURN;
WHITESPACE: (TAB|SPACE)+ /*{ skip(); }*/  { $channel = HIDDEN;};

//TEXT: (~(SEP_LEFT|SEP_RIGHT|COMMA|OPEN_BRACKET|CLOSE_BRACKET|TAB|SPACE))+;

TEXT:
(~(SEP_LEFT|SEP_RIGHT|COMMA|OPEN_BRACKET|CLOSE_BRACKET
|LEFT_CURLY|D_LEFT_CURLY|RIGHT_CURLY|D_RIGHT_CURLY
|TAB|SPACE))+
;