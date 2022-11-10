// Generated from ./src/SysYLexer.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SysYLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ID=1, INT=2, ASSIGN=3, WS=4;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"ID", "INT", "ASSIGN", "WS", "LETTER", "DIGIT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ID", "INT", "ASSIGN", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public SysYLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SysYLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\6\62\b\1\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\3\2\5\2\22\n\2\3\2\3\2\3"+
		"\2\7\2\27\n\2\f\2\16\2\32\13\2\3\3\3\3\3\3\7\3\37\n\3\f\3\16\3\"\13\3"+
		"\5\3$\n\3\3\4\3\4\3\5\6\5)\n\5\r\5\16\5*\3\5\3\5\3\6\3\6\3\7\3\7\2\2\b"+
		"\3\3\5\4\7\5\t\6\13\2\r\2\3\2\6\3\2\63;\3\2\62;\5\2\13\f\17\17\"\"\4\2"+
		"C\\c|\2\66\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\3\21\3\2\2"+
		"\2\5#\3\2\2\2\7%\3\2\2\2\t(\3\2\2\2\13.\3\2\2\2\r\60\3\2\2\2\17\22\5\13"+
		"\6\2\20\22\7a\2\2\21\17\3\2\2\2\21\20\3\2\2\2\22\30\3\2\2\2\23\27\5\13"+
		"\6\2\24\27\5\r\7\2\25\27\7a\2\2\26\23\3\2\2\2\26\24\3\2\2\2\26\25\3\2"+
		"\2\2\27\32\3\2\2\2\30\26\3\2\2\2\30\31\3\2\2\2\31\4\3\2\2\2\32\30\3\2"+
		"\2\2\33$\7\62\2\2\34 \t\2\2\2\35\37\t\3\2\2\36\35\3\2\2\2\37\"\3\2\2\2"+
		" \36\3\2\2\2 !\3\2\2\2!$\3\2\2\2\" \3\2\2\2#\33\3\2\2\2#\34\3\2\2\2$\6"+
		"\3\2\2\2%&\7?\2\2&\b\3\2\2\2\')\t\4\2\2(\'\3\2\2\2)*\3\2\2\2*(\3\2\2\2"+
		"*+\3\2\2\2+,\3\2\2\2,-\b\5\2\2-\n\3\2\2\2./\t\5\2\2/\f\3\2\2\2\60\61\t"+
		"\3\2\2\61\16\3\2\2\2\t\2\21\26\30 #*\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}