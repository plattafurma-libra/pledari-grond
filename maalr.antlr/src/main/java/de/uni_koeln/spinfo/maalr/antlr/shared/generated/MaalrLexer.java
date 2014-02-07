// $ANTLR 3.4 de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g 2013-11-28 14:04:01

	package de.uni_koeln.spinfo.maalr.antlr.shared.generated;


import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class MaalrLexer extends Lexer {
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
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public MaalrLexer() {} 
    public MaalrLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public MaalrLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g"; }

    // $ANTLR start "SEP_LEFT"
    public final void mSEP_LEFT() throws RecognitionException {
        try {
            int _type = SEP_LEFT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:11:9: ( '<' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:11:11: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SEP_LEFT"

    // $ANTLR start "SEP_RIGHT"
    public final void mSEP_RIGHT() throws RecognitionException {
        try {
            int _type = SEP_RIGHT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:12:10: ( '>' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:12:12: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SEP_RIGHT"

    // $ANTLR start "SEP"
    public final void mSEP() throws RecognitionException {
        try {
            int _type = SEP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:13:4: ( SEP_LEFT SEP_RIGHT )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:13:6: SEP_LEFT SEP_RIGHT
            {
            mSEP_LEFT(); 


            mSEP_RIGHT(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SEP"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:15:6: ( ',' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:15:8: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "OPEN_BRACKET"
    public final void mOPEN_BRACKET() throws RecognitionException {
        try {
            int _type = OPEN_BRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:16:13: ( '[' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:16:15: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OPEN_BRACKET"

    // $ANTLR start "CLOSE_BRACKET"
    public final void mCLOSE_BRACKET() throws RecognitionException {
        try {
            int _type = CLOSE_BRACKET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:17:14: ( ']' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:17:16: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CLOSE_BRACKET"

    // $ANTLR start "LEFT_CURLY"
    public final void mLEFT_CURLY() throws RecognitionException {
        try {
            int _type = LEFT_CURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:19:11: ( '{' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:19:13: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LEFT_CURLY"

    // $ANTLR start "RIGHT_CURLY"
    public final void mRIGHT_CURLY() throws RecognitionException {
        try {
            int _type = RIGHT_CURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:20:12: ( '}' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:20:14: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "RIGHT_CURLY"

    // $ANTLR start "D_LEFT_CURLY"
    public final void mD_LEFT_CURLY() throws RecognitionException {
        try {
            int _type = D_LEFT_CURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:22:13: ( '{{' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:22:15: '{{'
            {
            match("{{"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "D_LEFT_CURLY"

    // $ANTLR start "D_RIGHT_CURLY"
    public final void mD_RIGHT_CURLY() throws RecognitionException {
        try {
            int _type = D_RIGHT_CURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:23:14: ( '}}' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:23:16: '}}'
            {
            match("}}"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "D_RIGHT_CURLY"

    // $ANTLR start "GENUS"
    public final void mGENUS() throws RecognitionException {
        try {
            int _type = GENUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:25:6: ( 'f' | 'm' | 'f.pl' | 'm.pl' | 'm(f)' )
            int alt1=5;
            switch ( input.LA(1) ) {
            case 'f':
                {
                switch ( input.LA(2) ) {
                case '.':
                    {
                    alt1=3;
                    }
                    break;
                default:
                    alt1=1;
                }

                }
                break;
            case 'm':
                {
                switch ( input.LA(2) ) {
                case '.':
                    {
                    alt1=4;
                    }
                    break;
                case '(':
                    {
                    alt1=5;
                    }
                    break;
                default:
                    alt1=2;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;

            }

            switch (alt1) {
                case 1 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:25:8: 'f'
                    {
                    match('f'); 

                    }
                    break;
                case 2 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:25:12: 'm'
                    {
                    match('m'); 

                    }
                    break;
                case 3 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:25:16: 'f.pl'
                    {
                    match("f.pl"); 



                    }
                    break;
                case 4 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:25:23: 'm.pl'
                    {
                    match("m.pl"); 



                    }
                    break;
                case 5 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:25:30: 'm(f)'
                    {
                    match("m(f)"); 



                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "GENUS"

    // $ANTLR start "GRAMMATT"
    public final void mGRAMMATT() throws RecognitionException {
        try {
            int _type = GRAMMATT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:9: ( 'tr' | 'adj' | 'refl' | 'n.l' | 'adv' | 'tr/int' | 'int' | 'abs/tr' | 'tr/int/refl' | 'interj' | 'cj' | '(refl) int' )
            int alt2=12;
            switch ( input.LA(1) ) {
            case 't':
                {
                switch ( input.LA(2) ) {
                case 'r':
                    {
                    switch ( input.LA(3) ) {
                    case '/':
                        {
                        switch ( input.LA(4) ) {
                        case 'i':
                            {
                            switch ( input.LA(5) ) {
                            case 'n':
                                {
                                switch ( input.LA(6) ) {
                                case 't':
                                    {
                                    switch ( input.LA(7) ) {
                                    case '/':
                                        {
                                        alt2=9;
                                        }
                                        break;
                                    default:
                                        alt2=6;
                                    }

                                    }
                                    break;
                                default:
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 2, 20, input);

                                    throw nvae;

                                }

                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("", 2, 17, input);

                                throw nvae;

                            }

                            }
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("", 2, 12, input);

                            throw nvae;

                        }

                        }
                        break;
                    default:
                        alt2=1;
                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;

                }

                }
                break;
            case 'a':
                {
                switch ( input.LA(2) ) {
                case 'd':
                    {
                    switch ( input.LA(3) ) {
                    case 'j':
                        {
                        alt2=2;
                        }
                        break;
                    case 'v':
                        {
                        alt2=5;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 9, input);

                        throw nvae;

                    }

                    }
                    break;
                case 'b':
                    {
                    alt2=8;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 2, input);

                    throw nvae;

                }

                }
                break;
            case 'r':
                {
                alt2=3;
                }
                break;
            case 'n':
                {
                alt2=4;
                }
                break;
            case 'i':
                {
                switch ( input.LA(2) ) {
                case 'n':
                    {
                    switch ( input.LA(3) ) {
                    case 't':
                        {
                        switch ( input.LA(4) ) {
                        case 'e':
                            {
                            alt2=10;
                            }
                            break;
                        default:
                            alt2=7;
                        }

                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 11, input);

                        throw nvae;

                    }

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 5, input);

                    throw nvae;

                }

                }
                break;
            case 'c':
                {
                alt2=11;
                }
                break;
            case '(':
                {
                alt2=12;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;

            }

            switch (alt2) {
                case 1 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:11: 'tr'
                    {
                    match("tr"); 



                    }
                    break;
                case 2 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:16: 'adj'
                    {
                    match("adj"); 



                    }
                    break;
                case 3 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:22: 'refl'
                    {
                    match("refl"); 



                    }
                    break;
                case 4 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:29: 'n.l'
                    {
                    match("n.l"); 



                    }
                    break;
                case 5 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:35: 'adv'
                    {
                    match("adv"); 



                    }
                    break;
                case 6 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:41: 'tr/int'
                    {
                    match("tr/int"); 



                    }
                    break;
                case 7 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:50: 'int'
                    {
                    match("int"); 



                    }
                    break;
                case 8 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:56: 'abs/tr'
                    {
                    match("abs/tr"); 



                    }
                    break;
                case 9 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:65: 'tr/int/refl'
                    {
                    match("tr/int/refl"); 



                    }
                    break;
                case 10 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:79: 'interj'
                    {
                    match("interj"); 



                    }
                    break;
                case 11 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:88: 'cj'
                    {
                    match("cj"); 



                    }
                    break;
                case 12 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:26:93: '(refl) int'
                    {
                    match("(refl) int"); 



                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "GRAMMATT"

    // $ANTLR start "LINEFEED"
    public final void mLINEFEED() throws RecognitionException {
        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:28:18: ( '\\r' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:28:20: '\\r'
            {
            match('\r'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "LINEFEED"

    // $ANTLR start "CARRIAGERETURN"
    public final void mCARRIAGERETURN() throws RecognitionException {
        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:29:24: ( '\\n' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:29:26: '\\n'
            {
            match('\n'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "CARRIAGERETURN"

    // $ANTLR start "TAB"
    public final void mTAB() throws RecognitionException {
        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:30:13: ( '\\t' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:30:15: '\\t'
            {
            match('\t'); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TAB"

    // $ANTLR start "SPACE"
    public final void mSPACE() throws RecognitionException {
        try {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:31:15: ( ' ' )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:31:17: ' '
            {
            match(' '); 

            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "SPACE"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:33:8: ( ( LINEFEED )? CARRIAGERETURN )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:33:10: ( LINEFEED )? CARRIAGERETURN
            {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:33:10: ( LINEFEED )?
            int alt3=2;
            switch ( input.LA(1) ) {
                case '\r':
                    {
                    alt3=1;
                    }
                    break;
            }

            switch (alt3) {
                case 1 :
                    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:
                    {
                    if ( input.LA(1)=='\r' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }


            mCARRIAGERETURN(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:34:11: ( ( TAB | SPACE )+ )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:34:13: ( TAB | SPACE )+
            {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:34:13: ( TAB | SPACE )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                switch ( input.LA(1) ) {
                case '\t':
                case ' ':
                    {
                    alt4=1;
                    }
                    break;

                }

                switch (alt4) {
            	case 1 :
            	    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


             _channel = HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WHITESPACE"

    // $ANTLR start "TEXT"
    public final void mTEXT() throws RecognitionException {
        try {
            int _type = TEXT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:38:5: ( (~ ( SEP_LEFT | SEP_RIGHT | COMMA | OPEN_BRACKET | CLOSE_BRACKET | LEFT_CURLY | D_LEFT_CURLY | RIGHT_CURLY | D_RIGHT_CURLY | TAB | SPACE ) )+ )
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:39:2: (~ ( SEP_LEFT | SEP_RIGHT | COMMA | OPEN_BRACKET | CLOSE_BRACKET | LEFT_CURLY | D_LEFT_CURLY | RIGHT_CURLY | D_RIGHT_CURLY | TAB | SPACE ) )+
            {
            // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:39:2: (~ ( SEP_LEFT | SEP_RIGHT | COMMA | OPEN_BRACKET | CLOSE_BRACKET | LEFT_CURLY | D_LEFT_CURLY | RIGHT_CURLY | D_RIGHT_CURLY | TAB | SPACE ) )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0 >= '\u0000' && LA5_0 <= '\b')||(LA5_0 >= '\n' && LA5_0 <= '\u001F')||(LA5_0 >= '!' && LA5_0 <= '+')||(LA5_0 >= '-' && LA5_0 <= ';')||LA5_0=='='||(LA5_0 >= '?' && LA5_0 <= 'Z')||LA5_0=='\\'||(LA5_0 >= '^' && LA5_0 <= 'z')||LA5_0=='|'||(LA5_0 >= '~' && LA5_0 <= '\uFFFF')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:39:2: ~ ( SEP_LEFT | SEP_RIGHT | COMMA | OPEN_BRACKET | CLOSE_BRACKET | LEFT_CURLY | D_LEFT_CURLY | RIGHT_CURLY | D_RIGHT_CURLY | TAB | SPACE )
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\b')||(input.LA(1) >= '\n' && input.LA(1) <= '\u001F')||(input.LA(1) >= '!' && input.LA(1) <= '+')||(input.LA(1) >= '-' && input.LA(1) <= ';')||input.LA(1)=='='||(input.LA(1) >= '?' && input.LA(1) <= 'Z')||input.LA(1)=='\\'||(input.LA(1) >= '^' && input.LA(1) <= 'z')||input.LA(1)=='|'||(input.LA(1) >= '~' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "TEXT"

    public void mTokens() throws RecognitionException {
        // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:8: ( SEP_LEFT | SEP_RIGHT | SEP | COMMA | OPEN_BRACKET | CLOSE_BRACKET | LEFT_CURLY | RIGHT_CURLY | D_LEFT_CURLY | D_RIGHT_CURLY | GENUS | GRAMMATT | NEWLINE | WHITESPACE | TEXT )
        int alt6=15;
        alt6 = dfa6.predict(input);
        switch (alt6) {
            case 1 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:10: SEP_LEFT
                {
                mSEP_LEFT(); 


                }
                break;
            case 2 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:19: SEP_RIGHT
                {
                mSEP_RIGHT(); 


                }
                break;
            case 3 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:29: SEP
                {
                mSEP(); 


                }
                break;
            case 4 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:33: COMMA
                {
                mCOMMA(); 


                }
                break;
            case 5 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:39: OPEN_BRACKET
                {
                mOPEN_BRACKET(); 


                }
                break;
            case 6 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:52: CLOSE_BRACKET
                {
                mCLOSE_BRACKET(); 


                }
                break;
            case 7 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:66: LEFT_CURLY
                {
                mLEFT_CURLY(); 


                }
                break;
            case 8 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:77: RIGHT_CURLY
                {
                mRIGHT_CURLY(); 


                }
                break;
            case 9 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:89: D_LEFT_CURLY
                {
                mD_LEFT_CURLY(); 


                }
                break;
            case 10 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:102: D_RIGHT_CURLY
                {
                mD_RIGHT_CURLY(); 


                }
                break;
            case 11 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:116: GENUS
                {
                mGENUS(); 


                }
                break;
            case 12 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:122: GRAMMATT
                {
                mGRAMMATT(); 


                }
                break;
            case 13 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:131: NEWLINE
                {
                mNEWLINE(); 


                }
                break;
            case 14 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:139: WHITESPACE
                {
                mWHITESPACE(); 


                }
                break;
            case 15 :
                // de/uni_koeln/spinfo/maalr/antlr/shared/generated/MaalrLexer.g:1:150: TEXT
                {
                mTEXT(); 


                }
                break;

        }

    }


    protected DFA6 dfa6 = new DFA6(this);
    static final String DFA6_eotS =
        "\1\uffff\1\25\4\uffff\1\30\1\32\2\34\10\24\1\47\10\uffff\1\24\1"+
        "\uffff\2\24\1\54\5\24\1\54\1\24\1\uffff\4\24\1\uffff\2\54\2\24\2"+
        "\54\1\24\3\34\2\24\1\54\6\24\3\54\5\24\1\54";
    static final String DFA6_eofS =
        "\111\uffff";
    static final String DFA6_minS =
        "\1\0\1\76\4\uffff\1\173\1\175\2\0\1\162\1\142\1\145\1\56\1\156\1"+
        "\152\1\162\1\12\1\0\10\uffff\1\160\1\uffff\1\160\1\146\1\0\1\152"+
        "\1\163\1\146\1\154\1\164\1\0\1\145\1\uffff\2\154\1\51\1\151\1\uffff"+
        "\2\0\1\57\1\154\2\0\1\146\3\0\1\156\1\164\1\0\1\162\1\154\1\164"+
        "\1\162\1\152\1\51\3\0\1\40\1\162\1\145\1\146\1\154\1\0";
    static final String DFA6_maxS =
        "\1\uffff\1\76\4\uffff\1\173\1\175\2\uffff\1\162\1\144\1\145\1\56"+
        "\1\156\1\152\1\162\1\12\1\uffff\10\uffff\1\160\1\uffff\1\160\1\146"+
        "\1\uffff\1\166\1\163\1\146\1\154\1\164\1\uffff\1\145\1\uffff\2\154"+
        "\1\51\1\151\1\uffff\2\uffff\1\57\1\154\2\uffff\1\146\3\uffff\1\156"+
        "\1\164\1\uffff\1\162\1\154\1\164\1\162\1\152\1\51\3\uffff\1\40\1"+
        "\162\1\145\1\146\1\154\1\uffff";
    static final String DFA6_acceptS =
        "\2\uffff\1\2\1\4\1\5\1\6\15\uffff\1\16\1\17\1\1\1\3\1\11\1\7\1\12"+
        "\1\10\1\uffff\1\13\12\uffff\1\15\4\uffff\1\14\34\uffff";
    static final String DFA6_specialS =
        "\1\3\7\uffff\1\21\1\17\10\uffff\1\0\14\uffff\1\2\5\uffff\1\14\7"+
        "\uffff\1\5\1\7\2\uffff\1\4\1\16\1\uffff\1\6\1\10\1\11\2\uffff\1"+
        "\1\6\uffff\1\20\1\13\1\15\5\uffff\1\12}>";
    static final String[] DFA6_transitionS = {
            "\11\24\1\23\1\22\2\24\1\21\22\24\1\23\7\24\1\20\3\24\1\3\17"+
            "\24\1\1\1\24\1\2\34\24\1\4\1\24\1\5\3\24\1\13\1\24\1\17\2\24"+
            "\1\10\2\24\1\16\3\24\1\11\1\15\3\24\1\14\1\24\1\12\6\24\1\6"+
            "\1\24\1\7\uff82\24",
            "\1\26",
            "",
            "",
            "",
            "",
            "\1\27",
            "\1\31",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\1\24\1\33\15\24"+
            "\1\uffff\1\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff"+
            "\1\24\1\uffff\uff82\24",
            "\11\24\1\uffff\26\24\1\uffff\7\24\1\36\3\24\1\uffff\1\24\1"+
            "\35\15\24\1\uffff\1\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35"+
            "\24\1\uffff\1\24\1\uffff\uff82\24",
            "\1\37",
            "\1\41\1\uffff\1\40",
            "\1\42",
            "\1\43",
            "\1\44",
            "\1\45",
            "\1\46",
            "\1\22",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff\1\24\1"+
            "\uffff\uff82\24",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\50",
            "",
            "\1\51",
            "\1\52",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\2\24\1\53\14\24"+
            "\1\uffff\1\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff"+
            "\1\24\1\uffff\uff82\24",
            "\1\55\13\uffff\1\56",
            "\1\57",
            "\1\60",
            "\1\61",
            "\1\62",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff\1\24\1"+
            "\uffff\uff82\24",
            "\1\63",
            "",
            "\1\64",
            "\1\65",
            "\1\66",
            "\1\67",
            "",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff\1\24\1"+
            "\uffff\uff82\24",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff\1\24\1"+
            "\uffff\uff82\24",
            "\1\70",
            "\1\71",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff\1\24\1"+
            "\uffff\uff82\24",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\7\24\1\72\25\24\1\uffff"+
            "\1\24\1\uffff\uff82\24",
            "\1\73",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff\1\24\1"+
            "\uffff\uff82\24",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff\1\24\1"+
            "\uffff\uff82\24",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff\1\24\1"+
            "\uffff\uff82\24",
            "\1\74",
            "\1\75",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff\1\24\1"+
            "\uffff\uff82\24",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "\1\103",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\2\24\1\104\14\24"+
            "\1\uffff\1\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff"+
            "\1\24\1\uffff\uff82\24",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff\1\24\1"+
            "\uffff\uff82\24",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff\1\24\1"+
            "\uffff\uff82\24",
            "\1\54",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\11\24\1\uffff\26\24\1\uffff\13\24\1\uffff\17\24\1\uffff\1"+
            "\24\1\uffff\34\24\1\uffff\1\24\1\uffff\35\24\1\uffff\1\24\1"+
            "\uffff\uff82\24"
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( SEP_LEFT | SEP_RIGHT | SEP | COMMA | OPEN_BRACKET | CLOSE_BRACKET | LEFT_CURLY | RIGHT_CURLY | D_LEFT_CURLY | D_RIGHT_CURLY | GENUS | GRAMMATT | NEWLINE | WHITESPACE | TEXT );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA6_18 = input.LA(1);

                        s = -1;
                        if ( ((LA6_18 >= '\u0000' && LA6_18 <= '\b')||(LA6_18 >= '\n' && LA6_18 <= '\u001F')||(LA6_18 >= '!' && LA6_18 <= '+')||(LA6_18 >= '-' && LA6_18 <= ';')||LA6_18=='='||(LA6_18 >= '?' && LA6_18 <= 'Z')||LA6_18=='\\'||(LA6_18 >= '^' && LA6_18 <= 'z')||LA6_18=='|'||(LA6_18 >= '~' && LA6_18 <= '\uFFFF')) ) {s = 20;}

                        else s = 39;

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA6_57 = input.LA(1);

                        s = -1;
                        if ( ((LA6_57 >= '\u0000' && LA6_57 <= '\b')||(LA6_57 >= '\n' && LA6_57 <= '\u001F')||(LA6_57 >= '!' && LA6_57 <= '+')||(LA6_57 >= '-' && LA6_57 <= ';')||LA6_57=='='||(LA6_57 >= '?' && LA6_57 <= 'Z')||LA6_57=='\\'||(LA6_57 >= '^' && LA6_57 <= 'z')||LA6_57=='|'||(LA6_57 >= '~' && LA6_57 <= '\uFFFF')) ) {s = 20;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA6_31 = input.LA(1);

                        s = -1;
                        if ( (LA6_31=='/') ) {s = 43;}

                        else if ( ((LA6_31 >= '\u0000' && LA6_31 <= '\b')||(LA6_31 >= '\n' && LA6_31 <= '\u001F')||(LA6_31 >= '!' && LA6_31 <= '+')||(LA6_31 >= '-' && LA6_31 <= '.')||(LA6_31 >= '0' && LA6_31 <= ';')||LA6_31=='='||(LA6_31 >= '?' && LA6_31 <= 'Z')||LA6_31=='\\'||(LA6_31 >= '^' && LA6_31 <= 'z')||LA6_31=='|'||(LA6_31 >= '~' && LA6_31 <= '\uFFFF')) ) {s = 20;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA6_0 = input.LA(1);

                        s = -1;
                        if ( (LA6_0=='<') ) {s = 1;}

                        else if ( (LA6_0=='>') ) {s = 2;}

                        else if ( (LA6_0==',') ) {s = 3;}

                        else if ( (LA6_0=='[') ) {s = 4;}

                        else if ( (LA6_0==']') ) {s = 5;}

                        else if ( (LA6_0=='{') ) {s = 6;}

                        else if ( (LA6_0=='}') ) {s = 7;}

                        else if ( (LA6_0=='f') ) {s = 8;}

                        else if ( (LA6_0=='m') ) {s = 9;}

                        else if ( (LA6_0=='t') ) {s = 10;}

                        else if ( (LA6_0=='a') ) {s = 11;}

                        else if ( (LA6_0=='r') ) {s = 12;}

                        else if ( (LA6_0=='n') ) {s = 13;}

                        else if ( (LA6_0=='i') ) {s = 14;}

                        else if ( (LA6_0=='c') ) {s = 15;}

                        else if ( (LA6_0=='(') ) {s = 16;}

                        else if ( (LA6_0=='\r') ) {s = 17;}

                        else if ( (LA6_0=='\n') ) {s = 18;}

                        else if ( (LA6_0=='\t'||LA6_0==' ') ) {s = 19;}

                        else if ( ((LA6_0 >= '\u0000' && LA6_0 <= '\b')||(LA6_0 >= '\u000B' && LA6_0 <= '\f')||(LA6_0 >= '\u000E' && LA6_0 <= '\u001F')||(LA6_0 >= '!' && LA6_0 <= '\'')||(LA6_0 >= ')' && LA6_0 <= '+')||(LA6_0 >= '-' && LA6_0 <= ';')||LA6_0=='='||(LA6_0 >= '?' && LA6_0 <= 'Z')||LA6_0=='\\'||(LA6_0 >= '^' && LA6_0 <= '`')||LA6_0=='b'||(LA6_0 >= 'd' && LA6_0 <= 'e')||(LA6_0 >= 'g' && LA6_0 <= 'h')||(LA6_0 >= 'j' && LA6_0 <= 'l')||(LA6_0 >= 'o' && LA6_0 <= 'q')||LA6_0=='s'||(LA6_0 >= 'u' && LA6_0 <= 'z')||LA6_0=='|'||(LA6_0 >= '~' && LA6_0 <= '\uFFFF')) ) {s = 20;}

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA6_49 = input.LA(1);

                        s = -1;
                        if ( ((LA6_49 >= '\u0000' && LA6_49 <= '\b')||(LA6_49 >= '\n' && LA6_49 <= '\u001F')||(LA6_49 >= '!' && LA6_49 <= '+')||(LA6_49 >= '-' && LA6_49 <= ';')||LA6_49=='='||(LA6_49 >= '?' && LA6_49 <= 'Z')||LA6_49=='\\'||(LA6_49 >= '^' && LA6_49 <= 'z')||LA6_49=='|'||(LA6_49 >= '~' && LA6_49 <= '\uFFFF')) ) {s = 20;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA6_45 = input.LA(1);

                        s = -1;
                        if ( ((LA6_45 >= '\u0000' && LA6_45 <= '\b')||(LA6_45 >= '\n' && LA6_45 <= '\u001F')||(LA6_45 >= '!' && LA6_45 <= '+')||(LA6_45 >= '-' && LA6_45 <= ';')||LA6_45=='='||(LA6_45 >= '?' && LA6_45 <= 'Z')||LA6_45=='\\'||(LA6_45 >= '^' && LA6_45 <= 'z')||LA6_45=='|'||(LA6_45 >= '~' && LA6_45 <= '\uFFFF')) ) {s = 20;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA6_52 = input.LA(1);

                        s = -1;
                        if ( ((LA6_52 >= '\u0000' && LA6_52 <= '\b')||(LA6_52 >= '\n' && LA6_52 <= '\u001F')||(LA6_52 >= '!' && LA6_52 <= '+')||(LA6_52 >= '-' && LA6_52 <= ';')||LA6_52=='='||(LA6_52 >= '?' && LA6_52 <= 'Z')||LA6_52=='\\'||(LA6_52 >= '^' && LA6_52 <= 'z')||LA6_52=='|'||(LA6_52 >= '~' && LA6_52 <= '\uFFFF')) ) {s = 20;}

                        else s = 28;

                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA6_46 = input.LA(1);

                        s = -1;
                        if ( ((LA6_46 >= '\u0000' && LA6_46 <= '\b')||(LA6_46 >= '\n' && LA6_46 <= '\u001F')||(LA6_46 >= '!' && LA6_46 <= '+')||(LA6_46 >= '-' && LA6_46 <= ';')||LA6_46=='='||(LA6_46 >= '?' && LA6_46 <= 'Z')||LA6_46=='\\'||(LA6_46 >= '^' && LA6_46 <= 'z')||LA6_46=='|'||(LA6_46 >= '~' && LA6_46 <= '\uFFFF')) ) {s = 20;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA6_53 = input.LA(1);

                        s = -1;
                        if ( ((LA6_53 >= '\u0000' && LA6_53 <= '\b')||(LA6_53 >= '\n' && LA6_53 <= '\u001F')||(LA6_53 >= '!' && LA6_53 <= '+')||(LA6_53 >= '-' && LA6_53 <= ';')||LA6_53=='='||(LA6_53 >= '?' && LA6_53 <= 'Z')||LA6_53=='\\'||(LA6_53 >= '^' && LA6_53 <= 'z')||LA6_53=='|'||(LA6_53 >= '~' && LA6_53 <= '\uFFFF')) ) {s = 20;}

                        else s = 28;

                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA6_54 = input.LA(1);

                        s = -1;
                        if ( ((LA6_54 >= '\u0000' && LA6_54 <= '\b')||(LA6_54 >= '\n' && LA6_54 <= '\u001F')||(LA6_54 >= '!' && LA6_54 <= '+')||(LA6_54 >= '-' && LA6_54 <= ';')||LA6_54=='='||(LA6_54 >= '?' && LA6_54 <= 'Z')||LA6_54=='\\'||(LA6_54 >= '^' && LA6_54 <= 'z')||LA6_54=='|'||(LA6_54 >= '~' && LA6_54 <= '\uFFFF')) ) {s = 20;}

                        else s = 28;

                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA6_72 = input.LA(1);

                        s = -1;
                        if ( ((LA6_72 >= '\u0000' && LA6_72 <= '\b')||(LA6_72 >= '\n' && LA6_72 <= '\u001F')||(LA6_72 >= '!' && LA6_72 <= '+')||(LA6_72 >= '-' && LA6_72 <= ';')||LA6_72=='='||(LA6_72 >= '?' && LA6_72 <= 'Z')||LA6_72=='\\'||(LA6_72 >= '^' && LA6_72 <= 'z')||LA6_72=='|'||(LA6_72 >= '~' && LA6_72 <= '\uFFFF')) ) {s = 20;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA6_65 = input.LA(1);

                        s = -1;
                        if ( ((LA6_65 >= '\u0000' && LA6_65 <= '\b')||(LA6_65 >= '\n' && LA6_65 <= '\u001F')||(LA6_65 >= '!' && LA6_65 <= '+')||(LA6_65 >= '-' && LA6_65 <= ';')||LA6_65=='='||(LA6_65 >= '?' && LA6_65 <= 'Z')||LA6_65=='\\'||(LA6_65 >= '^' && LA6_65 <= 'z')||LA6_65=='|'||(LA6_65 >= '~' && LA6_65 <= '\uFFFF')) ) {s = 20;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA6_37 = input.LA(1);

                        s = -1;
                        if ( ((LA6_37 >= '\u0000' && LA6_37 <= '\b')||(LA6_37 >= '\n' && LA6_37 <= '\u001F')||(LA6_37 >= '!' && LA6_37 <= '+')||(LA6_37 >= '-' && LA6_37 <= ';')||LA6_37=='='||(LA6_37 >= '?' && LA6_37 <= 'Z')||LA6_37=='\\'||(LA6_37 >= '^' && LA6_37 <= 'z')||LA6_37=='|'||(LA6_37 >= '~' && LA6_37 <= '\uFFFF')) ) {s = 20;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA6_66 = input.LA(1);

                        s = -1;
                        if ( ((LA6_66 >= '\u0000' && LA6_66 <= '\b')||(LA6_66 >= '\n' && LA6_66 <= '\u001F')||(LA6_66 >= '!' && LA6_66 <= '+')||(LA6_66 >= '-' && LA6_66 <= ';')||LA6_66=='='||(LA6_66 >= '?' && LA6_66 <= 'Z')||LA6_66=='\\'||(LA6_66 >= '^' && LA6_66 <= 'z')||LA6_66=='|'||(LA6_66 >= '~' && LA6_66 <= '\uFFFF')) ) {s = 20;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA6_50 = input.LA(1);

                        s = -1;
                        if ( (LA6_50=='e') ) {s = 58;}

                        else if ( ((LA6_50 >= '\u0000' && LA6_50 <= '\b')||(LA6_50 >= '\n' && LA6_50 <= '\u001F')||(LA6_50 >= '!' && LA6_50 <= '+')||(LA6_50 >= '-' && LA6_50 <= ';')||LA6_50=='='||(LA6_50 >= '?' && LA6_50 <= 'Z')||LA6_50=='\\'||(LA6_50 >= '^' && LA6_50 <= 'd')||(LA6_50 >= 'f' && LA6_50 <= 'z')||LA6_50=='|'||(LA6_50 >= '~' && LA6_50 <= '\uFFFF')) ) {s = 20;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA6_9 = input.LA(1);

                        s = -1;
                        if ( (LA6_9=='.') ) {s = 29;}

                        else if ( (LA6_9=='(') ) {s = 30;}

                        else if ( ((LA6_9 >= '\u0000' && LA6_9 <= '\b')||(LA6_9 >= '\n' && LA6_9 <= '\u001F')||(LA6_9 >= '!' && LA6_9 <= '\'')||(LA6_9 >= ')' && LA6_9 <= '+')||LA6_9=='-'||(LA6_9 >= '/' && LA6_9 <= ';')||LA6_9=='='||(LA6_9 >= '?' && LA6_9 <= 'Z')||LA6_9=='\\'||(LA6_9 >= '^' && LA6_9 <= 'z')||LA6_9=='|'||(LA6_9 >= '~' && LA6_9 <= '\uFFFF')) ) {s = 20;}

                        else s = 28;

                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA6_64 = input.LA(1);

                        s = -1;
                        if ( (LA6_64=='/') ) {s = 68;}

                        else if ( ((LA6_64 >= '\u0000' && LA6_64 <= '\b')||(LA6_64 >= '\n' && LA6_64 <= '\u001F')||(LA6_64 >= '!' && LA6_64 <= '+')||(LA6_64 >= '-' && LA6_64 <= '.')||(LA6_64 >= '0' && LA6_64 <= ';')||LA6_64=='='||(LA6_64 >= '?' && LA6_64 <= 'Z')||LA6_64=='\\'||(LA6_64 >= '^' && LA6_64 <= 'z')||LA6_64=='|'||(LA6_64 >= '~' && LA6_64 <= '\uFFFF')) ) {s = 20;}

                        else s = 44;

                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA6_8 = input.LA(1);

                        s = -1;
                        if ( (LA6_8=='.') ) {s = 27;}

                        else if ( ((LA6_8 >= '\u0000' && LA6_8 <= '\b')||(LA6_8 >= '\n' && LA6_8 <= '\u001F')||(LA6_8 >= '!' && LA6_8 <= '+')||LA6_8=='-'||(LA6_8 >= '/' && LA6_8 <= ';')||LA6_8=='='||(LA6_8 >= '?' && LA6_8 <= 'Z')||LA6_8=='\\'||(LA6_8 >= '^' && LA6_8 <= 'z')||LA6_8=='|'||(LA6_8 >= '~' && LA6_8 <= '\uFFFF')) ) {s = 20;}

                        else s = 28;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 6, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}