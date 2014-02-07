// $ANTLR 3.4 de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g 2013-11-28 14:04:02

	package de.uni_koeln.spinfo.maalr.antlr.shared.generated;
	
	import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.BitSet;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class MaalrParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CARRIAGERETURN", "CLOSE_BRACKET", "COMMA", "D_LEFT_CURLY", "D_RIGHT_CURLY", "GENUS", "GRAMMATT", "LEFT_CURLY", "LINEFEED", "NEWLINE", "OPEN_BRACKET", "RIGHT_CURLY", "SEP", "SEP_LEFT", "SEP_RIGHT", "SPACE", "TAB", "TEXT", "WHITESPACE"
    };

    public static final int EOF=-1;
    public static final int CARRIAGERETURN=4;
    public static final int CLOSE_BRACKET=5;
    public static final int COMMA=6;
    public static final int D_LEFT_CURLY=7;
    public static final int D_RIGHT_CURLY=8;
    public static final int GENUS=9;
    public static final int GRAMMATT=10;
    public static final int LEFT_CURLY=11;
    public static final int LINEFEED=12;
    public static final int NEWLINE=13;
    public static final int OPEN_BRACKET=14;
    public static final int RIGHT_CURLY=15;
    public static final int SEP=16;
    public static final int SEP_LEFT=17;
    public static final int SEP_RIGHT=18;
    public static final int SPACE=19;
    public static final int TAB=20;
    public static final int TEXT=21;
    public static final int WHITESPACE=22;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public MaalrParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public MaalrParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return MaalrParser.tokenNames; }
    public String getGrammarFileName() { return "de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g"; }



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



    // $ANTLR start "entry"
    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:53:1: entry returns [Map<String,String> data] : lemma= phrase (lgrammatts= grammatts )? (lsemantic= semantics )? (lsubsemantic= subsemantics )? SEP translat= phrase (tgrammatt= grammatts )? (tsemantic= semantics )? (tsubsemantic= subsemantics )? ;
    public final Map<String,String> entry() throws RecognitionException {
        Map<String,String> data = null;


        MaalrParser.phrase_return lemma =null;

        Map lgrammatts =null;

        String lsemantic =null;

        String lsubsemantic =null;

        MaalrParser.phrase_return translat =null;

        Map tgrammatt =null;

        String tsemantic =null;

        String tsubsemantic =null;



          data = new HashMap<String,String>();
          flag = "D";

        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:58:2: (lemma= phrase (lgrammatts= grammatts )? (lsemantic= semantics )? (lsubsemantic= subsemantics )? SEP translat= phrase (tgrammatt= grammatts )? (tsemantic= semantics )? (tsubsemantic= subsemantics )? )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:59:2: lemma= phrase (lgrammatts= grammatts )? (lsemantic= semantics )? (lsubsemantic= subsemantics )? SEP translat= phrase (tgrammatt= grammatts )? (tsemantic= semantics )? (tsubsemantic= subsemantics )?
            {
            pushFollow(FOLLOW_phrase_in_entry65);
            lemma=phrase();

            state._fsp--;


            data.put(flag+"Stichwort",(lemma!=null?input.toString(lemma.start,lemma.stop):null));

            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:62:2: (lgrammatts= grammatts )?
            int alt1=2;
            switch ( input.LA(1) ) {
                case OPEN_BRACKET:
                    {
                    alt1=1;
                    }
                    break;
            }

            switch (alt1) {
                case 1 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:62:3: lgrammatts= grammatts
                    {
                    pushFollow(FOLLOW_grammatts_in_entry77);
                    lgrammatts=grammatts();

                    state._fsp--;


                    data.putAll(lgrammatts);

                    }
                    break;

            }


            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:66:2: (lsemantic= semantics )?
            int alt2=2;
            switch ( input.LA(1) ) {
                case LEFT_CURLY:
                    {
                    alt2=1;
                    }
                    break;
            }

            switch (alt2) {
                case 1 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:66:3: lsemantic= semantics
                    {
                    pushFollow(FOLLOW_semantics_in_entry93);
                    lsemantic=semantics();

                    state._fsp--;


                    data.put(flag+"Semantik",lsemantic);

                    }
                    break;

            }


            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:70:2: (lsubsemantic= subsemantics )?
            int alt3=2;
            switch ( input.LA(1) ) {
                case D_LEFT_CURLY:
                    {
                    alt3=1;
                    }
                    break;
            }

            switch (alt3) {
                case 1 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:70:3: lsubsemantic= subsemantics
                    {
                    pushFollow(FOLLOW_subsemantics_in_entry108);
                    lsubsemantic=subsemantics();

                    state._fsp--;


                    data.put(flag+"Subsemantik",lsubsemantic);

                    }
                    break;

            }


            match(input,SEP,FOLLOW_SEP_in_entry120); 

            flag = "R";

            pushFollow(FOLLOW_phrase_in_entry130);
            translat=phrase();

            state._fsp--;


            data.put(flag+"Stichwort",(translat!=null?input.toString(translat.start,translat.stop):null));

            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:79:2: (tgrammatt= grammatts )?
            int alt4=2;
            switch ( input.LA(1) ) {
                case OPEN_BRACKET:
                    {
                    alt4=1;
                    }
                    break;
            }

            switch (alt4) {
                case 1 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:79:3: tgrammatt= grammatts
                    {
                    pushFollow(FOLLOW_grammatts_in_entry142);
                    tgrammatt=grammatts();

                    state._fsp--;


                    data.putAll(tgrammatt);

                    }
                    break;

            }


            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:83:2: (tsemantic= semantics )?
            int alt5=2;
            switch ( input.LA(1) ) {
                case LEFT_CURLY:
                    {
                    alt5=1;
                    }
                    break;
            }

            switch (alt5) {
                case 1 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:83:3: tsemantic= semantics
                    {
                    pushFollow(FOLLOW_semantics_in_entry158);
                    tsemantic=semantics();

                    state._fsp--;


                    data.put(flag+"Semantik",tsemantic);

                    }
                    break;

            }


            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:87:3: (tsubsemantic= subsemantics )?
            int alt6=2;
            switch ( input.LA(1) ) {
                case D_LEFT_CURLY:
                    {
                    alt6=1;
                    }
                    break;
            }

            switch (alt6) {
                case 1 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:87:4: tsubsemantic= subsemantics
                    {
                    pushFollow(FOLLOW_subsemantics_in_entry175);
                    tsubsemantic=subsemantics();

                    state._fsp--;


                    data.put(flag+"Subsemantik",tsubsemantic);

                    }
                    break;

            }


            }

        }

        	catch (RecognitionException e1) {
        	    throw e1;
        	}

        finally {
        	// do for sure before leaving
        }
        return data;
    }
    // $ANTLR end "entry"


    public static class phrase_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "phrase"
    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:96:1: phrase : ( options {greedy=false; } : . )+ ;
    public final MaalrParser.phrase_return phrase() throws RecognitionException {
        MaalrParser.phrase_return retval = new MaalrParser.phrase_return();
        retval.start = input.LT(1);


        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:97:5: ( ( options {greedy=false; } : . )+ )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:98:5: ( options {greedy=false; } : . )+
            {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:98:5: ( options {greedy=false; } : . )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                alt7 = dfa7.predict(input);
                switch (alt7) {
            	case 1 :
            	    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:98:33: .
            	    {
            	    matchAny(input); 

            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            }

            retval.stop = input.LT(-1);


        }

        	catch (RecognitionException e1) {
        	    throw e1;
        	}

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "phrase"



    // $ANTLR start "grammatts"
    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:101:1: grammatts returns [Map map] : OPEN_BRACKET a= grammlist CLOSE_BRACKET ;
    public final Map grammatts() throws RecognitionException {
        Map map = null;


        Map a =null;


        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:101:28: ( OPEN_BRACKET a= grammlist CLOSE_BRACKET )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:102:2: OPEN_BRACKET a= grammlist CLOSE_BRACKET
            {
            match(input,OPEN_BRACKET,FOLLOW_OPEN_BRACKET_in_grammatts230); 

            pushFollow(FOLLOW_grammlist_in_grammatts234);
            a=grammlist();

            state._fsp--;


            map = a;

            match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_grammatts238); 

            }

        }

        	catch (RecognitionException e1) {
        	    throw e1;
        	}

        finally {
        	// do for sure before leaving
        }
        return map;
    }
    // $ANTLR end "grammatts"



    // $ANTLR start "semantics"
    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:105:1: semantics returns [String text] : LEFT_CURLY s= phrase RIGHT_CURLY ;
    public final String semantics() throws RecognitionException {
        String text = null;


        MaalrParser.phrase_return s =null;


        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:105:32: ( LEFT_CURLY s= phrase RIGHT_CURLY )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:106:3: LEFT_CURLY s= phrase RIGHT_CURLY
            {
            match(input,LEFT_CURLY,FOLLOW_LEFT_CURLY_in_semantics252); 

            pushFollow(FOLLOW_phrase_in_semantics256);
            s=phrase();

            state._fsp--;


            match(input,RIGHT_CURLY,FOLLOW_RIGHT_CURLY_in_semantics258); 

            text = (s!=null?input.toString(s.start,s.stop):null);

            }

        }

        	catch (RecognitionException e1) {
        	    throw e1;
        	}

        finally {
        	// do for sure before leaving
        }
        return text;
    }
    // $ANTLR end "semantics"



    // $ANTLR start "subsemantics"
    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:111:1: subsemantics returns [String text] : D_LEFT_CURLY s= phrase D_RIGHT_CURLY ;
    public final String subsemantics() throws RecognitionException {
        String text = null;


        MaalrParser.phrase_return s =null;


        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:111:35: ( D_LEFT_CURLY s= phrase D_RIGHT_CURLY )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:112:3: D_LEFT_CURLY s= phrase D_RIGHT_CURLY
            {
            match(input,D_LEFT_CURLY,FOLLOW_D_LEFT_CURLY_in_subsemantics279); 

            pushFollow(FOLLOW_phrase_in_subsemantics283);
            s=phrase();

            state._fsp--;


            match(input,D_RIGHT_CURLY,FOLLOW_D_RIGHT_CURLY_in_subsemantics285); 

            text = (s!=null?input.toString(s.start,s.stop):null);

            }

        }

        	catch (RecognitionException e1) {
        	    throw e1;
        	}

        finally {
        	// do for sure before leaving
        }
        return text;
    }
    // $ANTLR end "subsemantics"



    // $ANTLR start "grammlist"
    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:117:1: grammlist returns [Map atts] : g1= grammatt ( COMMA g2= grammatt )* ;
    public final Map grammlist() throws RecognitionException {
        Map atts = null;


        MaalrParser.grammatt_return g1 =null;

        MaalrParser.grammatt_return g2 =null;



          atts = new HashMap<String,String>();

        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:121:2: (g1= grammatt ( COMMA g2= grammatt )* )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:122:2: g1= grammatt ( COMMA g2= grammatt )*
            {
            pushFollow(FOLLOW_grammatt_in_grammlist312);
            g1=grammatt();

            state._fsp--;


            atts.put(flag+(g1!=null?g1.type:null),(g1!=null?g1.text:null));

            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:123:2: ( COMMA g2= grammatt )*
            loop8:
            do {
                int alt8=2;
                switch ( input.LA(1) ) {
                case COMMA:
                    {
                    alt8=1;
                    }
                    break;

                }

                switch (alt8) {
            	case 1 :
            	    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:123:3: COMMA g2= grammatt
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_grammlist318); 

            	    pushFollow(FOLLOW_grammatt_in_grammlist322);
            	    g2=grammatt();

            	    state._fsp--;


            	    atts.put(flag+(g2!=null?g2.type:null),(g2!=null?g2.text:null));

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            }

        }

        	catch (RecognitionException e1) {
        	    throw e1;
        	}

        finally {
        	// do for sure before leaving
        }
        return atts;
    }
    // $ANTLR end "grammlist"



    // $ANTLR start "text"
    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:126:1: text : ( TEXT | OPEN_BRACKET textlist CLOSE_BRACKET | SEP_LEFT textlist SEP_RIGHT );
    public final void text() throws RecognitionException {
        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:126:5: ( TEXT | OPEN_BRACKET textlist CLOSE_BRACKET | SEP_LEFT textlist SEP_RIGHT )
            int alt9=3;
            switch ( input.LA(1) ) {
            case TEXT:
                {
                alt9=1;
                }
                break;
            case OPEN_BRACKET:
                {
                alt9=2;
                }
                break;
            case SEP_LEFT:
                {
                alt9=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }

            switch (alt9) {
                case 1 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:127:2: TEXT
                    {
                    match(input,TEXT,FOLLOW_TEXT_in_text335); 

                    }
                    break;
                case 2 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:128:4: OPEN_BRACKET textlist CLOSE_BRACKET
                    {
                    match(input,OPEN_BRACKET,FOLLOW_OPEN_BRACKET_in_text340); 

                    pushFollow(FOLLOW_textlist_in_text342);
                    textlist();

                    state._fsp--;


                    match(input,CLOSE_BRACKET,FOLLOW_CLOSE_BRACKET_in_text344); 

                    }
                    break;
                case 3 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:129:4: SEP_LEFT textlist SEP_RIGHT
                    {
                    match(input,SEP_LEFT,FOLLOW_SEP_LEFT_in_text349); 

                    pushFollow(FOLLOW_textlist_in_text351);
                    textlist();

                    state._fsp--;


                    match(input,SEP_RIGHT,FOLLOW_SEP_RIGHT_in_text353); 

                    }
                    break;

            }
        }

        	catch (RecognitionException e1) {
        	    throw e1;
        	}

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "text"



    // $ANTLR start "textlist"
    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:132:1: textlist : ( text )+ ( COMMA ( text )+ )* ;
    public final void textlist() throws RecognitionException {
        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:132:9: ( ( text )+ ( COMMA ( text )+ )* )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:133:2: ( text )+ ( COMMA ( text )+ )*
            {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:133:2: ( text )+
            int cnt10=0;
            loop10:
            do {
                int alt10=2;
                switch ( input.LA(1) ) {
                case OPEN_BRACKET:
                case SEP_LEFT:
                case TEXT:
                    {
                    alt10=1;
                    }
                    break;

                }

                switch (alt10) {
            	case 1 :
            	    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:133:2: text
            	    {
            	    pushFollow(FOLLOW_text_in_textlist362);
            	    text();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt10 >= 1 ) break loop10;
                        EarlyExitException eee =
                            new EarlyExitException(10, input);
                        throw eee;
                }
                cnt10++;
            } while (true);


            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:133:8: ( COMMA ( text )+ )*
            loop12:
            do {
                int alt12=2;
                switch ( input.LA(1) ) {
                case COMMA:
                    {
                    alt12=1;
                    }
                    break;

                }

                switch (alt12) {
            	case 1 :
            	    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:133:9: COMMA ( text )+
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_textlist366); 

            	    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:133:15: ( text )+
            	    int cnt11=0;
            	    loop11:
            	    do {
            	        int alt11=2;
            	        switch ( input.LA(1) ) {
            	        case OPEN_BRACKET:
            	        case SEP_LEFT:
            	        case TEXT:
            	            {
            	            alt11=1;
            	            }
            	            break;

            	        }

            	        switch (alt11) {
            	    	case 1 :
            	    	    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:133:15: text
            	    	    {
            	    	    pushFollow(FOLLOW_text_in_textlist368);
            	    	    text();

            	    	    state._fsp--;


            	    	    }
            	    	    break;

            	    	default :
            	    	    if ( cnt11 >= 1 ) break loop11;
            	                EarlyExitException eee =
            	                    new EarlyExitException(11, input);
            	                throw eee;
            	        }
            	        cnt11++;
            	    } while (true);


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);


            }

        }

        	catch (RecognitionException e1) {
        	    throw e1;
        	}

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "textlist"


    public static class grammatt_return extends ParserRuleReturnScope {
        public String type;
        public String text;
    };


    // $ANTLR start "grammatt"
    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:137:1: grammatt returns [String type, String text] : (g= GENUS |g= GRAMMATT ) ;
    public final MaalrParser.grammatt_return grammatt() throws RecognitionException {
        MaalrParser.grammatt_return retval = new MaalrParser.grammatt_return();
        retval.start = input.LT(1);


        Token g=null;

        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:137:44: ( (g= GENUS |g= GRAMMATT ) )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:138:2: (g= GENUS |g= GRAMMATT )
            {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:138:2: (g= GENUS |g= GRAMMATT )
            int alt13=2;
            switch ( input.LA(1) ) {
            case GENUS:
                {
                alt13=1;
                }
                break;
            case GRAMMATT:
                {
                alt13=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }

            switch (alt13) {
                case 1 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:138:4: g= GENUS
                    {
                    g=(Token)match(input,GENUS,FOLLOW_GENUS_in_grammatt389); 

                    }
                    break;
                case 2 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrParser.g:139:5: g= GRAMMATT
                    {
                    g=(Token)match(input,GRAMMATT,FOLLOW_GRAMMATT_in_grammatt398); 

                    }
                    break;

            }



            		String t = translateTokenType((g!=null?g.getType():0));
            		if(t.equals("GENUS")) retval.type = "Genus";
            		else if(t.equals("GRAMMATT")) retval.type = "Grammatik";
            		else retval.type = "";
            		
            		retval.text = (g!=null?g.getText():null);
            	

            }

            retval.stop = input.LT(-1);


        }

        	catch (RecognitionException e1) {
        	    throw e1;
        	}

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "grammatt"

    // Delegated rules


    protected DFA7 dfa7 = new DFA7(this);
    static final String DFA7_eotS =
        "\16\uffff";
    static final String DFA7_eofS =
        "\1\4\1\7\6\uffff\3\7\1\uffff\2\7";
    static final String DFA7_minS =
        "\2\4\6\uffff\3\4\1\uffff\2\4";
    static final String DFA7_maxS =
        "\2\26\6\uffff\3\26\1\uffff\2\26";
    static final String DFA7_acceptS =
        "\2\uffff\5\2\1\1\3\uffff\1\2\2\uffff";
    static final String DFA7_specialS =
        "\16\uffff}>";
    static final String[] DFA7_transitionS = {
            "\3\7\1\3\1\6\2\7\1\2\2\7\1\1\1\5\1\4\6\7",
            "\5\7\1\10\1\11\14\7",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\7\1\13\1\12\20\7",
            "\1\7\1\13\1\12\20\7",
            "\5\7\1\14\1\15\14\7",
            "",
            "\1\7\1\13\1\12\20\7",
            "\1\7\1\13\1\12\20\7"
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "()+ loopback of 98:5: ( options {greedy=false; } : . )+";
        }
    }
 

    public static final BitSet FOLLOW_phrase_in_entry65 = new BitSet(new long[]{0x0000000000014880L});
    public static final BitSet FOLLOW_grammatts_in_entry77 = new BitSet(new long[]{0x0000000000010880L});
    public static final BitSet FOLLOW_semantics_in_entry93 = new BitSet(new long[]{0x0000000000010080L});
    public static final BitSet FOLLOW_subsemantics_in_entry108 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_SEP_in_entry120 = new BitSet(new long[]{0x00000000007FFFF0L});
    public static final BitSet FOLLOW_phrase_in_entry130 = new BitSet(new long[]{0x0000000000004882L});
    public static final BitSet FOLLOW_grammatts_in_entry142 = new BitSet(new long[]{0x0000000000000882L});
    public static final BitSet FOLLOW_semantics_in_entry158 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_subsemantics_in_entry175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPEN_BRACKET_in_grammatts230 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_grammlist_in_grammatts234 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_grammatts238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_CURLY_in_semantics252 = new BitSet(new long[]{0x00000000007FFFF0L});
    public static final BitSet FOLLOW_phrase_in_semantics256 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_RIGHT_CURLY_in_semantics258 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_D_LEFT_CURLY_in_subsemantics279 = new BitSet(new long[]{0x00000000007FFFF0L});
    public static final BitSet FOLLOW_phrase_in_subsemantics283 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_D_RIGHT_CURLY_in_subsemantics285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_grammatt_in_grammlist312 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_COMMA_in_grammlist318 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_grammatt_in_grammlist322 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_TEXT_in_text335 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPEN_BRACKET_in_text340 = new BitSet(new long[]{0x0000000000224000L});
    public static final BitSet FOLLOW_textlist_in_text342 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_CLOSE_BRACKET_in_text344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEP_LEFT_in_text349 = new BitSet(new long[]{0x0000000000224000L});
    public static final BitSet FOLLOW_textlist_in_text351 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_SEP_RIGHT_in_text353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_text_in_textlist362 = new BitSet(new long[]{0x0000000000224042L});
    public static final BitSet FOLLOW_COMMA_in_textlist366 = new BitSet(new long[]{0x0000000000224000L});
    public static final BitSet FOLLOW_text_in_textlist368 = new BitSet(new long[]{0x0000000000224042L});
    public static final BitSet FOLLOW_GENUS_in_grammatt389 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GRAMMATT_in_grammatt398 = new BitSet(new long[]{0x0000000000000002L});

}