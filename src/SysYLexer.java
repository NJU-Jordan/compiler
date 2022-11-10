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
		IDENT=1, INT=2, ASSIGN=3, L_PAREN=4, R_PAREN=5, L_BRACE=6, R_BRACE=7, 
		SEMICOLON=8, WS=9;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"IDENT", "INT", "ASSIGN", "L_PAREN", "R_PAREN", "L_BRACE", "R_BRACE", 
			"SEMICOLON", "WS", "LETTER", "DIGIT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'='", "'('", "')'", "'{'", "'}'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "IDENT", "INT", "ASSIGN", "L_PAREN", "R_PAREN", "L_BRACE", "R_BRACE", 
			"SEMICOLON", "WS"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\13F\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\3\2\3\2\5\2\34\n\2\3\2\3\2\3\2\7\2!\n\2\f\2\16\2$\13\2\3\3"+
		"\3\3\3\3\7\3)\n\3\f\3\16\3,\13\3\5\3.\n\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7"+
		"\3\7\3\b\3\b\3\t\3\t\3\n\6\n=\n\n\r\n\16\n>\3\n\3\n\3\13\3\13\3\f\3\f"+
		"\2\2\r\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\2\27\2\3\2\6\3\2\63"+
		";\3\2\62;\5\2\13\f\17\17\"\"\4\2C\\c|\2J\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3"+
		"\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2"+
		"\2\23\3\2\2\2\3\33\3\2\2\2\5-\3\2\2\2\7/\3\2\2\2\t\61\3\2\2\2\13\63\3"+
		"\2\2\2\r\65\3\2\2\2\17\67\3\2\2\2\219\3\2\2\2\23<\3\2\2\2\25B\3\2\2\2"+
		"\27D\3\2\2\2\31\34\5\25\13\2\32\34\7a\2\2\33\31\3\2\2\2\33\32\3\2\2\2"+
		"\34\"\3\2\2\2\35!\5\25\13\2\36!\5\27\f\2\37!\7a\2\2 \35\3\2\2\2 \36\3"+
		"\2\2\2 \37\3\2\2\2!$\3\2\2\2\" \3\2\2\2\"#\3\2\2\2#\4\3\2\2\2$\"\3\2\2"+
		"\2%.\7\62\2\2&*\t\2\2\2\')\t\3\2\2(\'\3\2\2\2),\3\2\2\2*(\3\2\2\2*+\3"+
		"\2\2\2+.\3\2\2\2,*\3\2\2\2-%\3\2\2\2-&\3\2\2\2.\6\3\2\2\2/\60\7?\2\2\60"+
		"\b\3\2\2\2\61\62\7*\2\2\62\n\3\2\2\2\63\64\7+\2\2\64\f\3\2\2\2\65\66\7"+
		"}\2\2\66\16\3\2\2\2\678\7\177\2\28\20\3\2\2\29:\7=\2\2:\22\3\2\2\2;=\t"+
		"\4\2\2<;\3\2\2\2=>\3\2\2\2><\3\2\2\2>?\3\2\2\2?@\3\2\2\2@A\b\n\2\2A\24"+
		"\3\2\2\2BC\t\5\2\2C\26\3\2\2\2DE\t\3\2\2E\30\3\2\2\2\t\2\33 \"*->\3\b"+
		"\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}